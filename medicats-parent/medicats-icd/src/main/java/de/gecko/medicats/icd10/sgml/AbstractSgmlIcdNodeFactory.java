package de.gecko.medicats.icd10.sgml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.gecko.medicats.icd10.AbstractIcdNodeFactory;
import de.gecko.medicats.icd10.FileSource;
import de.gecko.medicats.icd10.IcdNode.IcdNodeType;
import de.gecko.medicats.icd10.IcdNode.IcdNodeUsage;

public abstract class AbstractSgmlIcdNodeFactory extends AbstractIcdNodeFactory
{
	private Element[] chapters;
	private SgmlIcdNodeRoot root;

	protected abstract int getChapterCount();

	protected abstract Stream<FileSource> getChapterFiles();

	/**
	 * @param chapter
	 *            [1 .. {@link #chapterCount()}]
	 * @return
	 */
	protected synchronized Element getChapter(int chapter)
	{
		if (chapter < 1 || chapter > getChapterCount())
			throw new IndexOutOfBoundsException();

		if (chapters == null)
			chapters = getChapterFiles().map(FileSource::getInputStream).map(SgmlChapterReader::read)
					.toArray(Element[]::new);

		return chapters[chapter - 1];
	}

	@Override
	public synchronized SgmlIcdNodeRoot getRootNode()
	{
		if (root == null)
		{
			SgmlIcdNodeRoot root = new SgmlIcdNodeRoot(getVersion(), getSortIndex(), getPreviousCodes(),
					getPreviousVersion());

			parseChapters(root);

			this.root = root;
		}
		return root;
	}

	private void parseChapters(SgmlIcdNodeRoot root)
	{
		try
		{
			for (int i = 1; i <= getChapterCount(); i++)
				parseKap(root, getChapter(i));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void parseKap(SgmlIcdNode parent, Element chapter) throws IOException
	{
		Element knr = getElementByTagName(chapter, "KNR");
		Element kti = getElementByTagName(chapter, "KTI");

		String code = getTextContentCleaned(knr);
		String label = getTextContentCleaned(kti);

		List<String> exclusions = new ArrayList<>();
		List<String> inclusions = new ArrayList<>();

		Element kInhalt = getElementByTagNameOrNull(chapter, "KINHALT");
		if (kInhalt != null)
		{
			Element exlusiva = getElementByTagNameOrNull(kInhalt, "E");
			if (exlusiva != null)
				exclusions.addAll(parseExclusiva(exlusiva));

			Element inclusiva1 = getElementByTagNameOrNull(kInhalt, "I1");
			Element inclusiva2 = getElementByTagNameOrNull(kInhalt, "I2");
			if (inclusiva1 != null || inclusiva2 != null)
				inclusions.addAll(parseInclusiva(inclusiva1, inclusiva2));
		}

		SgmlIcdNode node = SgmlIcdNode.createNode(parent, chapter, label, code, IcdNodeType.CHAPTER,
				IcdNodeUsage.UNDEFINED, inclusions, exclusions);

		for (Element g : getElementsByTagName(chapter, "GRUPPE"))
			parseGruppe(node, g);
	}

	private void parseGruppe(SgmlIcdNode parent, Element gruppe) throws IOException
	{
		Element grahm = getElementByTagName(gruppe, "GRRAHM");
		Element grti = getElementByTagName(gruppe, "GRTI");

		String code = parseVonBis(grahm);
		String label = getTextContentCleaned(grti);

		List<String> exclusions = new ArrayList<>();
		List<String> inclusions = new ArrayList<>();

		Element inhalt = getElementByTagNameOrNull(gruppe, "INHALT");
		if (inhalt != null)
		{
			Element exlusiva = getElementByTagNameOrNull(inhalt, "E");
			if (exlusiva != null)
				exclusions.addAll(parseExclusiva(exlusiva));

			Element inclusiva1 = getElementByTagNameOrNull(inhalt, "I1");
			Element inclusiva2 = getElementByTagNameOrNull(inhalt, "I2");
			if (inclusiva1 != null || inclusiva2 != null)
				inclusions.addAll(parseInclusiva(inclusiva1, inclusiva2));
		}

		if (skipGruppe(parent, gruppe, label, code, IcdNodeType.BLOCK, IcdNodeUsage.UNDEFINED, inclusions, exclusions))
			return;

		SgmlIcdNode node = SgmlIcdNode.createNode(parent, gruppe, label, code, IcdNodeType.BLOCK,
				IcdNodeUsage.UNDEFINED, inclusions, exclusions);

		List<Element> ugruppe = getElementsByTagName(gruppe, "UGRUPPE");
		if (!ugruppe.isEmpty())
		{
			for (Element u : ugruppe)
				parseUGruppe(node, u);
		}
		else
		{
			for (Element d : getElementsByTagName(gruppe, "D"))
				parseD(node, d);
		}
	}

	protected boolean skipGruppe(SgmlIcdNode parent, Element element, String label, String code, IcdNodeType nodeType,
			IcdNodeUsage nodeUsage, List<String> inclusionCodes, List<String> exclusionCodes)
	{
		return false;
	}

	private void parseUGruppe(SgmlIcdNode parent, Element uGruppe) throws IOException
	{
		Element ugrrahm = getElementByTagName(uGruppe, "UGRRAHM");
		Element ugrti = getElementByTagName(uGruppe, "UGRTI");

		String code = parseVonBis(ugrrahm);
		String label = getTextContentCleaned(ugrti);

		List<String> exclusions = new ArrayList<>();
		List<String> inclusions = new ArrayList<>();

		Element inhalt = getElementByTagNameOrNull(uGruppe, "INHALT");
		if (inhalt != null)
		{
			Element exlusiva = getElementByTagNameOrNull(inhalt, "E");
			if (exlusiva != null)
				exclusions.addAll(parseExclusiva(exlusiva));

			Element inclusiva1 = getElementByTagNameOrNull(inhalt, "I1");
			Element inclusiva2 = getElementByTagNameOrNull(inhalt, "I2");
			if (inclusiva1 != null || inclusiva2 != null)
				inclusions.addAll(parseInclusiva(inclusiva1, inclusiva2));
		}

		SgmlIcdNode node = SgmlIcdNode.createNode(parent, uGruppe, label, code, IcdNodeType.BLOCK,
				IcdNodeUsage.UNDEFINED, inclusions, exclusions);

		for (Element d : getElementsByTagName(uGruppe, "D"))
			parseD(node, d);
	}

	private void parseD(SgmlIcdNode parent, Element d) throws IOException
	{
		String codeAttribute = d.getAttribute("CODE");
		if (!skipDButParseV(codeAttribute))
		{
			Element dbasis = getElementByTagName(d, "DBASIS");
			Element dcode = getElementByTagName(d, "DCODE");
			Element dti = getElementByTagName(dbasis, "DTI");

			String code = getTextContentCleaned(dcode);
			if (code == null || code.isEmpty())
				code = codeAttribute;

			String typ = dcode.getAttribute("TYP");
			String label = getTextContentCleaned(dti);

			List<String> exclusions = new ArrayList<>();
			List<String> inclusions = new ArrayList<>();

			Element inhalt = getElementByTagNameOrNull(d, "INHALT");
			if (inhalt != null)
			{
				Element exlusiva = getElementByTagNameOrNull(inhalt, "E");
				if (exlusiva != null)
					exclusions.addAll(parseExclusiva(exlusiva));

				Element inclusiva1 = getElementByTagNameOrNull(inhalt, "I1");
				Element inclusiva2 = getElementByTagNameOrNull(inhalt, "I2");
				if (inclusiva1 != null || inclusiva2 != null)
					inclusions.addAll(parseInclusiva(inclusiva1, inclusiva2));
			}

			SgmlIcdNode node = SgmlIcdNode.createNode(parent, d, label, cleanCode(code), IcdNodeType.CATEGORY,
					nodeUsageFromCode(typ), inclusions, exclusions);

			for (Element v : getElementsByTagName(d, "V"))
				parseV(node, v);
		}
		else
		{
			for (Element v : getElementsByTagName(d, "V"))
				parseV(parent, v);
		}
	}

	protected boolean skipDButParseV(String codeAttribute)
	{
		switch (codeAttribute)
		{
			case "DUMW49":
			case "DUMW64":
			case "DUMW87":
			case "DUMW91":
			case "DUMW92":
			case "DUMW93":
			case "DUMW94":
			case "DUMX19":
			case "DUMX29":
			case "DUMX49":
			case "DUMX59":
			case "DUMX84":
			case "DUMY09":
			case "DUMY34":
			case "DUMY35":
			case "DUMY36":
			case "DUMY57":
			case "DUMY59":
			case "DUMY82":
			case "DUMY84":
				return true;
			default:
				return false;
		}
	}

	private void parseV(SgmlIcdNode parent, Element v) throws IOException
	{
		Element vbasis = getElementByTagName(v, "VBASIS");
		Element vcode = getElementByTagName(vbasis, "VCODE");
		Element vti = getElementByTagName(vbasis, "VTI");

		String code = getTextContentCleaned(vcode);
		String typ = vcode.getAttribute("TYP");
		String label = getTextContentCleaned(vti);

		List<String> exclusions = new ArrayList<>();
		List<String> inclusions = new ArrayList<>();

		Element inhalt = getElementByTagNameOrNull(v, "INHALT");
		if (inhalt != null)
		{
			Element exlusiva = getElementByTagNameOrNull(inhalt, "E");
			if (exlusiva != null)
				exclusions.addAll(parseExclusiva(exlusiva));

			Element inclusiva1 = getElementByTagNameOrNull(inhalt, "I1");
			Element inclusiva2 = getElementByTagNameOrNull(inhalt, "I2");
			if (inclusiva1 != null || inclusiva2 != null)
				inclusions.addAll(parseInclusiva(inclusiva1, inclusiva2));
		}

		SgmlIcdNode node = SgmlIcdNode.createNode(parent, v, label, cleanCode(code), IcdNodeType.CATEGORY,
				nodeUsageFromCode(typ), inclusions, exclusions);

		for (Element f : getElementsByTagName(v, "F"))
			parseF(node, f);
	}

	private void parseF(SgmlIcdNode parent, Element f) throws IOException
	{
		Element fbasis = getElementByTagName(f, "FBASIS");
		Element fcode = getElementByTagName(fbasis, "FCODE");
		Element fti = getElementByTagName(fbasis, "FTI");

		String code = getTextContentCleaned(fcode);
		String typ = fcode.getAttribute("TYP");
		String label = getTextContentCleaned(fti);

		List<String> exclusions = new ArrayList<>();
		List<String> inclusions = new ArrayList<>();

		Element inhalt = getElementByTagNameOrNull(f, "INHALT");
		if (inhalt != null)
		{
			Element exlusiva = getElementByTagNameOrNull(inhalt, "E");
			if (exlusiva != null)
				exclusions.addAll(parseExclusiva(exlusiva));

			Element inclusiva1 = getElementByTagNameOrNull(inhalt, "I1");
			Element inclusiva2 = getElementByTagNameOrNull(inhalt, "I2");
			if (inclusiva1 != null || inclusiva2 != null)
				inclusions.addAll(parseInclusiva(inclusiva1, inclusiva2));
		}

		SgmlIcdNode.createNode(parent, f, label, cleanCode(code), IcdNodeType.CATEGORY, nodeUsageFromCode(typ),
				inclusions, exclusions);
	}

	private String parseVonBis(Element rrahm) throws IOException
	{
		Element von = getElementByTagName(rrahm, "VON");
		String code = getTextContentCleaned(von);

		Element bis = getElementByTagNameOrNull(rrahm, "BIS");
		if (bis != null)
			code += ("-" + getTextContentCleaned(bis));

		return code;
	}

	private List<String> parseExclusiva(Element exlusiva) throws IOException
	{
		List<Element> ls = getElementsByTagName(exlusiva, "L");
		if (ls.isEmpty())
			return Collections.emptyList();

		return ls.stream().map(l -> getTextContentCleaned(l)).collect(Collectors.toList());
	}

	private List<String> parseInclusiva(Element inclusiva1, Element inclusiva2) throws IOException
	{
		List<Element> ls = new ArrayList<>();

		if (inclusiva1 != null)
			ls.addAll(getElementsByTagName(inclusiva1, "L"));
		if (inclusiva2 != null)
			ls.addAll(getElementsByTagName(inclusiva2, "L"));

		if (ls.isEmpty())
			return Collections.emptyList();

		return ls.stream().map(l -> getTextContentCleaned(l)).collect(Collectors.toList());
	}

	private List<Element> getElementsByTagName(Element e, String tagname) throws IOException
	{
		NodeList elements = e.getElementsByTagName(tagname);

		List<Element> list = new ArrayList<>(elements.getLength());
		for (int i = 0; i < elements.getLength(); i++)
		{
			Node n = elements.item(i);
			if (n instanceof Element)
				list.add((Element) n);
		}

		return list;
	}

	private Element getElementByTagNameOrNull(Element e, String tagname) throws IOException
	{
		NodeList elements = e.getElementsByTagName(tagname);
		if (elements.getLength() != 1 && !(elements.item(0) instanceof Element))
			return null;
		return (Element) elements.item(0);
	}

	private Element getElementByTagName(Element e, String tagname) throws IOException
	{
		Element element = getElementByTagNameOrNull(e, tagname);
		throwIOExceptionIf(element == null, "One element " + tagname + " expected");
		return element;
	}

	private void throwIOExceptionIf(boolean b, String message) throws IOException
	{
		if (b)
			throw new IOException(message);
	}

	private String getTextContentCleaned(Element e)
	{
		return e.getTextContent().trim().replaceAll("\\s+", " ");
	}

	private IcdNodeUsage nodeUsageFromCode(String typ)
	{
		if ("!".equals(typ))
			return IcdNodeUsage.OPTIONAL;
		else if ("*".equals(typ))
			return IcdNodeUsage.ASTER;
		else if ("+".equals(typ))
			return IcdNodeUsage.DAGGER;
		else
			return IcdNodeUsage.UNDEFINED;
	}

	private String cleanCode(String code)
	{
		if (code.endsWith(".-"))
			return code.substring(0, code.length() - 2);
		else if (code.endsWith("-") || code.endsWith("."))
			return code.substring(0, code.length() - 1);
		else
			return code;
	}
}

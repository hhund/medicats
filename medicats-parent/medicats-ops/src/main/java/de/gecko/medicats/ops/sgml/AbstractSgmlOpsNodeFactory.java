package de.gecko.medicats.ops.sgml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ops.AbstractOpsNodeFactory;
import de.gecko.medicats.ops.OpsNode;
import de.gecko.medicats.ops.OpsNode.OpsNodeType;

public abstract class AbstractSgmlOpsNodeFactory extends AbstractOpsNodeFactory
{
	private SgmlOpsNodeRoot root;

	protected abstract FileSource getSgml();

	@Override
	public synchronized OpsNode getRootNode()
	{
		if (root == null)
		{
			SgmlOpsNodeRoot root = new SgmlOpsNodeRoot(getVersion(), getPreviousCodes(), getPreviousVersion());

			try
			{
				Element rootElement = OpsSgmlReader.read(getSgml().getInputStream());
				List<Element> kaps = getElementsByTagName(rootElement, "KAP");

				for (Element kap : kaps)
					parseKap(root, kap);
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}

			this.root = root;
		}

		return root;
	}

	private Map<String, String> getExclusionsForNode(Element node) throws IOException {
		if (node == null) return Collections.emptyMap();
		Element exclusiva = getElementByTagNameOrNull(node, "VEXKL");
		if (exclusiva == null) return Collections.emptyMap();
		List<Element> texts = getElementsByTagName(exclusiva, "VTXT");
	}

	private void parseKap(SgmlOpsNodeRoot root, Element kap) throws IOException
	{
		Element knr = getElementByTagName(kap, "KNR");
		Element kti = getElementByTagName(kap, "KTI");

		String code = getTextContentCleaned(knr);
		String label = getTextContentCleaned(kti);

		SgmlOpsNode node = SgmlOpsNode.createNode(root, kap, label, code, OpsNodeType.CHAPTER, Collections.emptyMap(),
				Collections.emptyMap());

		NodeList childs = kap.getChildNodes();
		for (int c = 0; c < childs.getLength(); c++)
		{
			Node child = childs.item(c);
			if ("DST".equals(child.getNodeName()))
				parseDst(node, (Element) child);
			else if ("DSTGRUP".equals(child.getNodeName()))
				parseDstgrup(node, (Element) child);
		}

		// List<Element> dststemp = getElementsByTagName(kap, "DST");
		// System.out.println(dststemp.size());
		// List<Element> dstgrups = getElementsByTagName(kap, "DSTGRUP");
		// if (!dstgrups.isEmpty())
		// {
		// for (Element dstgrup : dstgrups)
		// parseDstgrup(node, dstgrup);
		// }
		// else
		// {
		// List<Element> dsts = getElementsByTagName(kap, "DST");
		// for (Element dst : dsts)
		// parseDst(node, dst);
		// }
	}

	private void parseDstgrup(SgmlOpsNode parent, Element dstgrup) throws IOException
	{
		Element dgti = getElementByTagName(dstgrup, "DGTI");
		Element dgrrahm = getElementByTagName(dstgrup, "DGRRAHM");

		String label = getTextContentCleaned(dgti);
		String code = parseVonBis(dgrrahm);

		List<String> inclusions = new ArrayList<>();
		List<String> exclusions = new ArrayList<>();

		Element dginhalt = getElementByTagNameOrNull(dstgrup, "DGINHALT");
		if (dginhalt != null)
		{
			Element dginkl = getElementByTagNameOrNull(dginhalt, "DGINKL");
			Element dgexkl = getElementByTagNameOrNull(dginhalt, "DGEXKL");

			inclusions.addAll(parseInclusions(dginkl));
			exclusions.addAll(parseExclusions(dgexkl));
		}

		SgmlOpsNode node = SgmlOpsNode.createNode(parent, dstgrup, label, code, OpsNodeType.BLOCK, inclusions,
				exclusions);

		List<Element> dsts = getElementsByTagName(dstgrup, "DST");
		for (Element dst : dsts)
			parseDst(node, dst);
	}

	private void parseDst(SgmlOpsNode parent, Element dst) throws IOException
	{
		Element dti = getElementByTagName(dst, "DTI");
		Element dcode = getElementByTagName(dst, "DCODE");

		String label = getTextContentCleaned(dti);
		String code = getTextContentCleaned(dcode);

		List<String> inclusions = new ArrayList<>();
		List<String> exclusions = new ArrayList<>();

		Element dginhalt = getElementByTagNameOrNull(dst, "DINHALT");
		if (dginhalt != null)
		{
			Element dinkl = getElementByTagNameOrNull(dginhalt, "DINKL");
			Element dexkl = getElementByTagNameOrNull(dginhalt, "DEXKL");

			inclusions.addAll(parseInclusions(dinkl));
			exclusions.addAll(parseExclusions(dexkl));
		}

		SgmlOpsNode node = SgmlOpsNode.createNode(parent, dst, label, code, OpsNodeType.CATEGORY, inclusions,
				exclusions);

		List<Element> vsts = getElementsByTagName(dst, "VST");
		for (Element vst : vsts)
			parseVst(node, vst);
	}

	private void parseVst(SgmlOpsNode parent, Element vst) throws IOException
	{
		Element vti = getElementByTagName(vst, "VTI");
		Element vcode = getElementByTagName(vst, "VCODE");

		String label = getTextContentCleaned(vti);
		String code = getTextContentCleaned(vcode);

		List<String> inclusions = new ArrayList<>();
		List<String> exclusions = new ArrayList<>();

		Element vinhalt = getElementByTagNameOrNull(vst, "VINHALT");
		if (vinhalt != null)
		{
			Element vinkl = getElementByTagNameOrNull(vinhalt, "VINKL");
			Element vexkl = getElementByTagNameOrNull(vinhalt, "VEXKL");

			inclusions.addAll(parseInclusions(vinkl));
			exclusions.addAll(parseExclusions(vexkl));
		}

		SgmlOpsNode node = SgmlOpsNode.createNode(parent, vst, label, code, OpsNodeType.CATEGORY, inclusions,
				exclusions);

		List<Element> fsts = getElementsByTagName(vst, "FST");
		for (Element fst : fsts)
			parseFst(node, fst);
	}

	private void parseFst(SgmlOpsNode parent, Element fst) throws IOException
	{
		Element fti = getElementByTagName(fst, "FTI");
		Element fcode = getElementByTagName(fst, "FCODE");

		String label = getTextContentCleaned(fti);
		String code = getTextContentCleaned(fcode);

		label = fixFstLabel(label, code, parent);

		List<String> inclusions = new ArrayList<>();
		List<String> exclusions = new ArrayList<>();

		Element finhalt = getElementByTagNameOrNull(fst, "FINHALT");
		if (finhalt != null)
		{
			Element finkl = getElementByTagNameOrNull(finhalt, "FINKL");
			Element fexkl = getElementByTagNameOrNull(finhalt, "FEXKL");

			inclusions.addAll(parseInclusions(finkl));
			exclusions.addAll(parseExclusions(fexkl));
		}

		SgmlOpsNode node = SgmlOpsNode.createNode(parent, fst, label, code, OpsNodeType.CATEGORY, inclusions,
				exclusions);

		List<Element> ssts = getElementsByTagName(fst, "SST");
		for (Element sst : ssts)
			parseSst(node, sst);
	}

	protected String fixFstLabel(String label, String code, SgmlOpsNode parent)
	{
		return label;
	}

	private void parseSst(SgmlOpsNode parent, Element sst) throws IOException
	{
		Element sti = getElementByTagName(sst, "STI");
		Element scode = getElementByTagName(sst, "SCODE");

		String label = getTextContentCleaned(sti);
		String code = getTextContentCleaned(scode);

		label = fixSstLabel(label, code, parent);

		List<String> inclusions = new ArrayList<>();
		List<String> exclusions = new ArrayList<>();

		Element sinhalt = getElementByTagNameOrNull(sst, "SINHALT");
		if (sinhalt != null)
		{
			Element sinkl = getElementByTagNameOrNull(sinhalt, "SINKL");
			Element sexkl = getElementByTagNameOrNull(sinhalt, "SEXKL");

			inclusions.addAll(parseInclusions(sinkl));
			exclusions.addAll(parseExclusions(sexkl));
		}

		SgmlOpsNode.createNode(parent, sst, label, code, OpsNodeType.CATEGORY, inclusions, exclusions);
	}

	protected String fixSstLabel(String label, String code, SgmlOpsNode parent)
	{
		return label;
	}

	private List<String> parseExclusions(Element exlusiva) throws IOException
	{
		if (exlusiva == null)
			return Collections.emptyList();

		List<Element> ls = getElementsByTagName(exlusiva, "L");
		if (ls.isEmpty())
			return Collections.emptyList();

		return ls.stream().map(l -> getTextContentCleaned(l)).collect(Collectors.toList());
	}

	private List<String> parseInclusions(Element inclusiva) throws IOException
	{
		if (inclusiva == null)
			return Collections.emptyList();

		List<Element> ls = getElementsByTagName(inclusiva, "L");
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

	private String parseVonBis(Element dgrrahm) throws IOException
	{
		Element von = getElementByTagName(dgrrahm, "DVON");
		String code = getTextContentCleaned(von);

		Element bis = getElementByTagNameOrNull(dgrrahm, "DBIS");
		if (bis != null)
			code += ("..." + getTextContentCleaned(bis));

		return code;
	}
}

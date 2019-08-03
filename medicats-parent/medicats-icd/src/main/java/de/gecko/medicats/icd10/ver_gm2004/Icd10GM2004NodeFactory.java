package de.gecko.medicats.icd10.ver_gm2004;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.w3c.dom.Element;

import de.gecko.medicats.icd10.IcdNode.IcdNodeType;
import de.gecko.medicats.icd10.IcdNode.IcdNodeUsage;
import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.sgml.AbstractSgmlIcdNodeFactory;
import de.gecko.medicats.icd10.sgml.SgmlIcdNode;

public class Icd10GM2004NodeFactory extends AbstractSgmlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm2004.zip", 961469066L);

	private FileSource[] chapterFiles = new FileSource[] { new FileSource(zip, "x1ges2004", "KAP01.sgm"),
			new FileSource(zip, "x1ges2004", "KAP02.sgm"), new FileSource(zip, "x1ges2004", "KAP03.sgm"),
			new FileSource(zip, "x1ges2004", "KAP04.sgm"), new FileSource(zip, "x1ges2004", "KAP05.sgm"),
			new FileSource(zip, "x1ges2004", "KAP06.sgm"), new FileSource(zip, "x1ges2004", "KAP07.sgm"),
			new FileSource(zip, "x1ges2004", "KAP08.sgm"), new FileSource(zip, "x1ges2004", "KAP09.sgm"),
			new FileSource(zip, "x1ges2004", "KAP10.sgm"), new FileSource(zip, "x1ges2004", "KAP11.sgm"),
			new FileSource(zip, "x1ges2004", "KAP12.sgm"), new FileSource(zip, "x1ges2004", "KAP13.sgm"),
			new FileSource(zip, "x1ges2004", "KAP14.sgm"), new FileSource(zip, "x1ges2004", "KAP15.sgm"),
			new FileSource(zip, "x1ges2004", "KAP16.sgm"), new FileSource(zip, "x1ges2004", "KAP17.sgm"),
			new FileSource(zip, "x1ges2004", "KAP18.sgm"), new FileSource(zip, "x1ges2004", "KAP19.sgm"),
			new FileSource(zip, "x1ges2004", "KAP20.sgm"), new FileSource(zip, "x1ges2004", "KAP21.sgm"),
			new FileSource(zip, "x1ges2004", "KAP22.sgm") };

	private FileSource transitionFile = new FileSource(zip, "x1ueb20_2004", "Umsteiger.txt");
	private FileSource systFile = new FileSource(zip, "x1ueb20_2004", "icd10v2004.txt");

	@Override
	public String getName()
	{
		return "ICD-10-GM 2004";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.302";
	}

	@Override
	public String getPreviousVersion()
	{
		return "icd10sgbv20";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2004";
	}

	@Override
	public int getSortIndex()
	{
		return 2004;
	}

	@Override
	protected int getChapterCount()
	{
		return chapterFiles.length;
	}

	@Override
	protected Stream<FileSource> getChapterFiles()
	{
		return Arrays.stream(chapterFiles);
	}

	@Override
	protected FileSource getSystFile()
	{
		return systFile;
	}

	@Override
	protected FileSource getTransitionFile()
	{
		return transitionFile;
	}

	@Override
	public IcdNodeWalker createNodeWalker()
	{
		return new Icd10GM2004NodeWalker(getRootNode());
	}

	@Override
	protected boolean skipGruppe(SgmlIcdNode parent, Element element, String label, String code, IcdNodeType nodeType,
			IcdNodeUsage nodeUsage, List<String> inclusionCodes, List<String> exclusionCodes)
	{
		return "I".equals(parent.getCode()) && "U80-U85".equals(code) ? true
				: super.skipGruppe(parent, element, label, code, nodeType, nodeUsage, inclusionCodes, exclusionCodes);
	}
}

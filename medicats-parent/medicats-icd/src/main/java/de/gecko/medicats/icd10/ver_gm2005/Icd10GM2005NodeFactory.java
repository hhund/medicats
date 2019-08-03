package de.gecko.medicats.icd10.ver_gm2005;

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

public class Icd10GM2005NodeFactory extends AbstractSgmlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm2005.zip", 34792544L);

	private FileSource[] chapterFiles = new FileSource[] { new FileSource(zip, "x1ges2005", "KAP01.sgm"),
			new FileSource(zip, "x1ges2005", "KAP02.sgm"), new FileSource(zip, "x1ges2005", "KAP03.sgm"),
			new FileSource(zip, "x1ges2005", "KAP04.sgm"), new FileSource(zip, "x1ges2005", "KAP05.sgm"),
			new FileSource(zip, "x1ges2005", "KAP06.sgm"), new FileSource(zip, "x1ges2005", "KAP07.sgm"),
			new FileSource(zip, "x1ges2005", "KAP08.sgm"), new FileSource(zip, "x1ges2005", "KAP09.sgm"),
			new FileSource(zip, "x1ges2005", "KAP10.sgm"), new FileSource(zip, "x1ges2005", "KAP11.sgm"),
			new FileSource(zip, "x1ges2005", "KAP12.sgm"), new FileSource(zip, "x1ges2005", "KAP13.sgm"),
			new FileSource(zip, "x1ges2005", "KAP14.sgm"), new FileSource(zip, "x1ges2005", "KAP15.sgm"),
			new FileSource(zip, "x1ges2005", "KAP16.sgm"), new FileSource(zip, "x1ges2005", "KAP17.sgm"),
			new FileSource(zip, "x1ges2005", "KAP18.sgm"), new FileSource(zip, "x1ges2005", "KAP19.sgm"),
			new FileSource(zip, "x1ges2005", "KAP20.sgm"), new FileSource(zip, "x1ges2005", "KAP21.sgm"),
			new FileSource(zip, "x1ges2005", "KAP22.sgm") };

	private FileSource transitionFile = new FileSource(zip, "x1ueb2004_2005", "umsteiger.txt");
	private FileSource systFile = new FileSource(zip, "x1ueb2004_2005", "ICD10V2005.txt");

	@Override
	public String getName()
	{
		return "ICD-10-GM 2005";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.304";
	}

	@Override
	public String getPreviousVersion()
	{
		return "icd10gm2004";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2005";
	}

	@Override
	public int getSortIndex()
	{
		return 2005;
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
		return new Icd10GM2005NodeWalker(getRootNode());
	}

	@Override
	protected boolean skipGruppe(SgmlIcdNode parent, Element element, String label, String code, IcdNodeType nodeType,
			IcdNodeUsage nodeUsage, List<String> inclusionCodes, List<String> exclusionCodes)
	{
		return "I".equals(parent.getCode()) && "U80-U85".equals(code) ? true
				: super.skipGruppe(parent, element, label, code, nodeType, nodeUsage, inclusionCodes, exclusionCodes);
	}
}

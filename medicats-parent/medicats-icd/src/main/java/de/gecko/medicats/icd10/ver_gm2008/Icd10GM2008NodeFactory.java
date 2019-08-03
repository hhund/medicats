package de.gecko.medicats.icd10.ver_gm2008;

import java.util.Arrays;
import java.util.stream.Stream;

import de.gecko.medicats.icd10.FileSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.ZipSource;
import de.gecko.medicats.icd10.sgml.AbstractSgmlIcdNodeFactory;

public class Icd10GM2008NodeFactory extends AbstractSgmlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm2008.zip", 2496678461L);

	private FileSource[] chapterFiles = new FileSource[] {
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP01.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP02.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP03.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP04.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP05.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP06.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP07.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP08.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP09.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP10.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP11.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP12.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP13.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP14.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP15.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP16.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP17.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP18.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP19.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP20.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP21.sgm"),
			new FileSource(zip, "x1ges2008", "Klassifikationsdateien", "KAP22.sgm") };

	private FileSource transitionFile = new FileSource(zip, "x1ueb2007_2008", "Klassifikationsdateien",
			"umsteiger20072008.txt");
	private FileSource systFile = new FileSource(zip, "x1ueb2007_2008", "Klassifikationsdateien", "icd10v2008.txt");

	@Override
	public String getName()
	{
		return "ICD-10-GM 2008";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.330";
	}

	@Override
	public String getPreviousVersion()
	{
		return "icd10gm2007";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2008";
	}

	@Override
	public int getSortIndex()
	{
		return 2008;
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
		return new Icd10GM2008NodeWalker(getRootNode());
	}
}

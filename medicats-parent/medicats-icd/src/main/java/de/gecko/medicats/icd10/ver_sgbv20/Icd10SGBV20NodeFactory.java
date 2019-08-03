package de.gecko.medicats.icd10.ver_sgbv20;

import java.util.Arrays;
import java.util.stream.Stream;

import de.gecko.medicats.icd10.FileSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.ZipSource;
import de.gecko.medicats.icd10.sgml.AbstractSgmlIcdNodeFactory;

public class Icd10SGBV20NodeFactory extends AbstractSgmlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm20.zip", 1573979968L);

	private FileSource[] chapterFiles = new FileSource[] { new FileSource(zip, "x1ses2_0", "KAP01.sgm"),
			new FileSource(zip, "x1ses2_0", "kap02.sgm"), new FileSource(zip, "x1ses2_0", "KAP03.sgm"),
			new FileSource(zip, "x1ses2_0", "kap04.sgm"), new FileSource(zip, "x1ses2_0", "KAP05.sgm"),
			new FileSource(zip, "x1ses2_0", "KAP06.sgm"), new FileSource(zip, "x1ses2_0", "KAP07.sgm"),
			new FileSource(zip, "x1ses2_0", "KAP08.sgm"), new FileSource(zip, "x1ses2_0", "KAP09.sgm"),
			new FileSource(zip, "x1ses2_0", "KAP10.sgm"), new FileSource(zip, "x1ses2_0", "KAP11.sgm"),
			new FileSource(zip, "x1ses2_0", "KAP12.sgm"), new FileSource(zip, "x1ses2_0", "KAP13.sgm"),
			new FileSource(zip, "x1ses2_0", "KAP14.sgm"), new FileSource(zip, "x1ses2_0", "KAP15.sgm"),
			new FileSource(zip, "x1ses2_0", "KAP16.sgm"), new FileSource(zip, "x1ses2_0", "KAP17.sgm"),
			new FileSource(zip, "x1ses2_0", "KAP18.sgm"), new FileSource(zip, "x1ses2_0", "kap19.sgm"),
			new FileSource(zip, "x1ses2_0", "KAP20.sgm"), new FileSource(zip, "x1ses2_0", "KAP21.sgm") };

	private FileSource transitionFile = new FileSource(zip, "x1ueb13_20_v11", "Umsteiger.txt");
	private FileSource systFile = new FileSource(zip, "x1ueb13_20_v11", "icd10v20.txt");

	@Override
	public String getName()
	{
		return "ICD-10 SGB V Version 2.0";
	}

	@Override
	public String getOid()
	{
		return "";
	}

	@Override
	public String getPreviousVersion()
	{
		return "icd10sgbv13";
	}

	@Override
	public String getVersion()
	{
		return "icd10sgbv20";
	}

	@Override
	public int getSortIndex()
	{
		return 20;
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
		return new Icd10SGBV20NodeWalker(getRootNode());
	}
}

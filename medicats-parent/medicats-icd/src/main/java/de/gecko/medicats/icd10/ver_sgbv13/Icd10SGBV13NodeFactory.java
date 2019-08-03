package de.gecko.medicats.icd10.ver_sgbv13;

import java.util.Arrays;
import java.util.stream.Stream;

import de.gecko.medicats.icd10.FileSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.ZipSource;
import de.gecko.medicats.icd10.sgml.AbstractSgmlIcdNodeFactory;

public class Icd10SGBV13NodeFactory extends AbstractSgmlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm13.zip", 4027743070L);

	private FileSource[] chapterFiles = new FileSource[] { new FileSource(zip, "x1ses1_3", "kap01.sgm"),
			new FileSource(zip, "x1ses1_3", "kap02.sgm"), new FileSource(zip, "x1ses1_3", "kap03.sgm"),
			new FileSource(zip, "x1ses1_3", "kap04.sgm"), new FileSource(zip, "x1ses1_3", "kap05.sgm"),
			new FileSource(zip, "x1ses1_3", "kap06.sgm"), new FileSource(zip, "x1ses1_3", "kap07.sgm"),
			new FileSource(zip, "x1ses1_3", "kap08.sgm"), new FileSource(zip, "x1ses1_3", "kap09.sgm"),
			new FileSource(zip, "x1ses1_3", "kap10.sgm"), new FileSource(zip, "x1ses1_3", "kap11.sgm"),
			new FileSource(zip, "x1ses1_3", "kap12.sgm"), new FileSource(zip, "x1ses1_3", "kap13.sgm"),
			new FileSource(zip, "x1ses1_3", "kap14.sgm"), new FileSource(zip, "x1ses1_3", "kap15.sgm"),
			new FileSource(zip, "x1ses1_3", "kap16.sgm"), new FileSource(zip, "x1ses1_3", "kap17.sgm"),
			new FileSource(zip, "x1ses1_3", "kap18.sgm"), new FileSource(zip, "x1ses1_3", "kap19.sgm"),
			new FileSource(zip, "x1ses1_3", "kap20.sgm"), new FileSource(zip, "x1ses1_3", "kap21.sgm") };

	private final ZipSource zip20 = new ZipSource(ZipSource.getBasePath(), "icd10gm20.zip", 1573979968L);

	private FileSource systFile = new FileSource(zip20, "x1ueb13_20_v11", "icd10v13.txt");

	@Override
	public String getName()
	{
		return "ICD-10 SGB V Version 1.3";
	}

	@Override
	public String getOid()
	{
		return "";
	}

	@Override
	public String getPreviousVersion()
	{
		return "icd10sgbv12";
	}

	@Override
	public String getVersion()
	{
		return "icd10sgbv13";
	}

	@Override
	public int getSortIndex()
	{
		return 13;
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
		return null;// No previous version
	}

	@Override
	public IcdNodeWalker createNodeWalker()
	{
		return new Icd10SGBV13NodeWalker(getRootNode());
	}
}

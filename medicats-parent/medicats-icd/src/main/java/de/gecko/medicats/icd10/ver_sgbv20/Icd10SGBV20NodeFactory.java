package de.gecko.medicats.icd10.ver_sgbv20;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.sgml.AbstractSgmlIcdNodeFactory;

public class Icd10SGBV20NodeFactory extends AbstractSgmlIcdNodeFactory implements IcdNodeFactory
{
	private static final String[] XML_CHAPTER_FILENAMES = { "KAP01.sgm", "kap02.sgm", "KAP03.sgm", "kap04.sgm",
			"KAP05.sgm", "KAP06.sgm", "KAP07.sgm", "KAP08.sgm", "KAP09.sgm", "KAP10.sgm", "KAP11.sgm", "KAP12.sgm",
			"KAP13.sgm", "KAP14.sgm", "KAP15.sgm", "KAP16.sgm", "KAP17.sgm", "KAP18.sgm", "kap19.sgm", "KAP20.sgm",
			"KAP21.sgm" };
	private static final String UMSTEIGER_RESOURCE_FILENAME = "Umsteiger.txt";
	private static final String PREVIOUS_VERSION = "icd10sgbv13";
	private static final String VERSION = "icd10sgbv20";

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
		return PREVIOUS_VERSION;
	}

	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public int getSortIndex()
	{
		return 20;
	}

	@Override
	protected String getPreviousCodesFileName()
	{
		return UMSTEIGER_RESOURCE_FILENAME;
	}

	@Override
	protected String[] getChapterFileNames()
	{
		return XML_CHAPTER_FILENAMES;
	}

	@Override
	public IcdNodeWalker createNodeWalker()
	{
		return new Icd10SGBV20NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("x1ses2_0.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 3242996000L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("x1ueb13_20_v11.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 2397915864L;
	}

	@Override
	protected Stream<Path> getChapterFileNamePaths(FileSystem taxonomyZip)
	{
		return Arrays.stream(getChapterFileNames()).map(f -> taxonomyZip.getPath(f));
	}

	@Override
	protected Path getTransitionFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath(getPreviousCodesFileName());
	}
}

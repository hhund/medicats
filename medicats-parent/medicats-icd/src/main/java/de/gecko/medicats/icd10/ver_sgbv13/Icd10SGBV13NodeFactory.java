package de.gecko.medicats.icd10.ver_sgbv13;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.sgml.AbstractSgmlIcdNodeFactory;

public class Icd10SGBV13NodeFactory extends AbstractSgmlIcdNodeFactory implements IcdNodeFactory
{
	private static final String[] XML_CHAPTER_FILENAMES = { "kap01.sgm", "kap02.sgm", "kap03.sgm", "kap04.sgm",
			"kap05.sgm", "kap06.sgm", "kap07.sgm", "kap08.sgm", "kap09.sgm", "kap10.sgm", "kap11.sgm", "kap12.sgm",
			"kap13.sgm", "kap14.sgm", "kap15.sgm", "kap16.sgm", "kap17.sgm", "kap18.sgm", "kap19.sgm", "kap20.sgm",
			"kap21.sgm" };
	private static final String UMSTEIGER_RESOURCE_FILENAME = null;
	private static final String PREVIOUS_VERSION = "icd10sgbv12";
	private static final String VERSION = "icd10sgbv13";

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
		return 13;
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
		return new Icd10SGBV13NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("x1ses1_3.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 4435210L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return null; // No previous version
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return Long.MIN_VALUE; // No previous version
	}

	@Override
	protected Stream<Path> getChapterFileNamePaths(FileSystem taxonomyZip)
	{
		return Arrays.stream(getChapterFileNames()).map(f -> taxonomyZip.getPath(f));
	}

	@Override
	protected Path getTransitionFilePath(FileSystem transitionZip)
	{
		return null; // No previous version
	}
}

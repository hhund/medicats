package de.gecko.medicats.icd10.ver_gm2007;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.sgml.AbstractSgmlIcdNodeFactory;

public class Icd10GM2007NodeFactory extends AbstractSgmlIcdNodeFactory implements IcdNodeFactory
{
	private static final String[] XML_CHAPTER_FILENAMES = { "KAP01.sgm", "KAP02.sgm", "KAP03.sgm", "KAP04.sgm",
			"KAP05.sgm", "KAP06.sgm", "KAP07.sgm", "KAP08.sgm", "KAP09.sgm", "KAP10.sgm", "KAP11.sgm", "KAP12.sgm",
			"KAP13.sgm", "KAP14.sgm", "KAP15.sgm", "KAP16.sgm", "KAP17.sgm", "KAP18.sgm", "KAP19.sgm", "KAP20.sgm",
			"KAP21.sgm", "KAP22.sgm" };
	private static final String UMSTEIGER_RESOURCE_FILENAME = "Umsteiger.txt";
	private static final String PREVIOUS_VERSION = "icd10gm2006";
	private static final String VERSION = "icd10gm2007";

	@Override
	public String getName()
	{
		return "ICD-10-GM 2007";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.318";
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
		return 2007;
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
		return new Icd10GM2007NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("x1ges2007.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 4149559148L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("x1ueb2006_2007.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 3439882982L;
	}

	@Override
	protected Stream<Path> getChapterFileNamePaths(FileSystem taxonomyZip)
	{
		return Arrays.stream(getChapterFileNames()).map(f -> taxonomyZip.getPath("Klassifikationsdateien", f));
	}

	@Override
	protected Path getTransitionFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", getPreviousCodesFileName());
	}
}

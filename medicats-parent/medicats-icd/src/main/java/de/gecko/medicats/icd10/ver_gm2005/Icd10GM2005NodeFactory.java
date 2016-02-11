package de.gecko.medicats.icd10.ver_gm2005;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.w3c.dom.Element;

import de.gecko.medicats.icd10.IcdNode.IcdNodeType;
import de.gecko.medicats.icd10.IcdNode.IcdNodeUsage;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.sgml.AbstractSgmlIcdNodeFactory;
import de.gecko.medicats.icd10.sgml.SgmlIcdNode;

public class Icd10GM2005NodeFactory extends AbstractSgmlIcdNodeFactory implements IcdNodeFactory
{
	private static final String[] XML_CHAPTER_FILENAMES = { "KAP01.sgm", "KAP02.sgm", "KAP03.sgm", "KAP04.sgm",
			"KAP05.sgm", "KAP06.sgm", "KAP07.sgm", "KAP08.sgm", "KAP09.sgm", "KAP10.sgm", "KAP11.sgm", "KAP12.sgm",
			"KAP13.sgm", "KAP14.sgm", "KAP15.sgm", "KAP16.sgm", "KAP17.sgm", "KAP18.sgm", "KAP19.sgm", "KAP20.sgm",
			"KAP21.sgm", "KAP22.sgm" };
	private static final String UMSTEIGER_RESOURCE_FILENAME = "umsteiger.txt";
	private static final String PREVIOUS_VERSION = "icd10gm2004";
	private static final String VERSION = "icd10gm2005";

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
		return 2005;
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
		return new Icd10GM2005NodeWalker(getRootNode());
	}

	@Override
	protected boolean skipGruppe(SgmlIcdNode parent, Element element, String label, String code, IcdNodeType nodeType,
			IcdNodeUsage nodeUsage, List<String> inclusionCodes, List<String> exclusionCodes)
	{
		return "I".equals(parent.getCode()) && "U80-U85".equals(code) ? true
				: super.skipGruppe(parent, element, label, code, nodeType, nodeUsage, inclusionCodes, exclusionCodes);
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("x1ges2005.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 3224403286L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("x1ueb2004_2005.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 3290291258L;
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

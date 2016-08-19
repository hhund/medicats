package de.gecko.medicats.icd10.ver_gm2012;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.claml.AbstractClaMLIcdNodeFactory;

public class Icd10GM2012NodeFactory extends AbstractClaMLIcdNodeFactory implements IcdNodeFactory
{
	private static final String XML_RESOURCE_FILENAME = "icd10gm2012syst_claml_20110923.xml";
	private static final String UMSTEIGER_RESOURCE_FILENAME = "umsteiger_icd10gmsyst2011_icd10gmsyst2012.txt";
	private static final String PREVIOUS_VERSION = "icd10gm2011";
	private static final String VERSION = "icd10gm2012";

	@Override
	public String getName()
	{
		return "ICD-10-GM 2012";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.409";
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
		return 2012;
	}

	@Override
	protected String getXmlResourceFileName()
	{
		return XML_RESOURCE_FILENAME;
	}

	@Override
	protected String getPreviousCodesFileName()
	{
		return UMSTEIGER_RESOURCE_FILENAME;
	}

	@Override
	public IcdNodeWalker createNodeWalker()
	{
		return new Icd10GM2012NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("x1gec2012.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 2726738754L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("x1ueb2011_2012.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 317253308L;
	}

	@Override
	protected Path getClaMLDtdPath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath("Klassifikationsdateien", "claml.dtd");
	}

	@Override
	protected Path getXmlResourcePath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath("Klassifikationsdateien", getXmlResourceFileName());
	}

	@Override
	protected Path getTransitionFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", getPreviousCodesFileName());
	}
}

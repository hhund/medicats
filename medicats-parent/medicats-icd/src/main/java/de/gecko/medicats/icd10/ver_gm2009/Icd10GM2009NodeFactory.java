package de.gecko.medicats.icd10.ver_gm2009;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.claml.AbstractClaMLIcdNodeFactory;

public class Icd10GM2009NodeFactory extends AbstractClaMLIcdNodeFactory implements IcdNodeFactory
{
	private static final String XML_RESOURCE_FILENAME = "icd10gm2009syst_claml_20080929.xml";
	private static final String UMSTEIGER_RESOURCE_FILENAME = "umsteiger_icd10gmsyst2008_icd10gmsyst2009.txt";
	private static final String PREVIOUS_VERSION = "icd10gm2008";
	private static final String VERSION = "icd10gm2009";

	@Override
	public String getName()
	{
		return "ICD-10-GM 2009";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.356";
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
		return 2009;
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
		return new Icd10GM2009NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("x1gex2009.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 3020984043L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("x1ueb2008_2009.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 558452344L;
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

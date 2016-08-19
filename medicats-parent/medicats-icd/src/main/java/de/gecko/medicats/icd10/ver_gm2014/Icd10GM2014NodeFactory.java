package de.gecko.medicats.icd10.ver_gm2014;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.claml.AbstractClaMLIcdNodeFactory;

public class Icd10GM2014NodeFactory extends AbstractClaMLIcdNodeFactory implements IcdNodeFactory
{
	private static final String XML_RESOURCE_FILENAME = "icd10gm2014syst_claml_20131015.xml";
	private static final String UMSTEIGER_RESOURCE_FILENAME = "icd10gm2014syst_umsteiger_2013_2014.txt";
	private static final String PREVIOUS_VERSION = "icd10gm2013";
	private static final String VERSION = "icd10gm2014";

	@Override
	public String getName()
	{
		return "ICD-10-GM 2014";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.417";
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
		return 2014;
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
		return new Icd10GM2014NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("x1gec2014-20131015.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 881871563L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("x1gua2014.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 906440156L;
	}

	@Override
	protected Path getClaMLDtdPath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath("Klassifikationsdateien", "ClaML.dtd");
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

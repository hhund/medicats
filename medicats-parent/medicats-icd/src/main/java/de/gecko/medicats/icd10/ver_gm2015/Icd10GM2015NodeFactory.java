package de.gecko.medicats.icd10.ver_gm2015;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.claml.AbstractClaMLIcdNodeFactory;

public class Icd10GM2015NodeFactory extends AbstractClaMLIcdNodeFactory implements IcdNodeFactory
{
	private static final String XML_RESOURCE_FILENAME = "icd10gm2015syst_claml_20140919.xml";
	private static final String UMSTEIGER_RESOURCE_FILENAME = "icd10gm2015syst_umsteiger_2014_2015.txt";
	private static final String PREVIOUS_VERSION = "icd10gm2014";
	private static final String VERSION = "icd10gm2015";

	@Override
	public String getName()
	{
		return "ICD-10-GM 2015";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.424";
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
		return 2015;
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
		return new Icd10GM2015NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("x1gec2015.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 2109013139L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("x1gut2015.zip");
	}
	
	@Override
	protected long getTransitionZipChecksum()
	{
		return 962446093L;
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

package de.gecko.medicats.icd10.ver_gm2010;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.claml.AbstractClaMLIcdNodeFactory;

public class Icd10GM2010NodeFactory extends AbstractClaMLIcdNodeFactory implements IcdNodeFactory
{
	private static final String XML_RESOURCE_FILENAME = "icd10gm2010syst_claml_20091019.xml";
	private static final String UMSTEIGER_RESOURCE_FILENAME = "umsteiger_icd10gmsyst2009_icd10gmsyst2010.txt";
	private static final String PREVIOUS_VERSION = "icd10gm2009";
	private static final String VERSION = "icd10gm2010";

	@Override
	public String getName()
	{
		return "ICD-10-GM 2010";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.384";
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
		return 2010;
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
		return new Icd10GM2010NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("x1gec2010.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 2082537388L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("x1ueb2009_2010.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 1705729767L;
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

package de.gecko.medicats.icd10.ver_gm2009;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.claml.AbstractClamlIcdNodeFactory;

public class Icd10GM2009NodeFactory extends AbstractClamlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm2009.zip", 394157295L);

	private final FileSource clamlDtd = new FileSource(zip, "x1gex2009", "Klassifikationsdateien", "claml.dtd");
	private final FileSource clamlXml = new FileSource(zip, "x1gex2009", "Klassifikationsdateien",
			"icd10gm2009syst_claml_20080929.xml");
	private FileSource transitionFile = new FileSource(zip, "x1ueb2008_2009", "Klassifikationsdateien",
			"umsteiger_icd10gmsyst2008_icd10gmsyst2009.txt");
	private FileSource systFile = new FileSource(zip, "x1ueb2008_2009", "Klassifikationsdateien",
			"icd10gmsyst2009.txt");

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
		return "icd10gm2008";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2009";
	}

	@Override
	public int getSortIndex()
	{
		return 2009;
	}

	@Override
	protected FileSource getClamlXml()
	{
		return clamlXml;
	}

	@Override
	protected FileSource getClamlDtd()
	{
		return clamlDtd;
	}

	@Override
	protected FileSource getSystFile()
	{
		return systFile;
	}

	@Override
	protected FileSource getTransitionFile()
	{
		return transitionFile;
	}

	@Override
	public IcdNodeWalker createNodeWalker()
	{
		return new Icd10GM2009NodeWalker(getRootNode());
	}
}

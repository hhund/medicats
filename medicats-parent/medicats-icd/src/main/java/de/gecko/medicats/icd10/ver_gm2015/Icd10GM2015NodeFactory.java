package de.gecko.medicats.icd10.ver_gm2015;

import de.gecko.medicats.icd10.FileSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.ZipSource;
import de.gecko.medicats.icd10.claml.AbstractClamlIcdNodeFactory;

public class Icd10GM2015NodeFactory extends AbstractClamlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm2015.zip", 626148197L);

	private final FileSource clamlDtd = new FileSource(zip, "x1gec2015", "Klassifikationsdateien", "ClaML.dtd");
	private final FileSource clamlXml = new FileSource(zip, "x1gec2015", "Klassifikationsdateien",
			"icd10gm2015syst_claml_20140919.xml");
	private FileSource transitionFile = new FileSource(zip, "x1gut2015", "Klassifikationsdateien",
			"icd10gm2015syst_umsteiger_2014_2015.txt");
	private FileSource systFile = new FileSource(zip, "x1gut2015", "Klassifikationsdateien", "icd10gm2015syst.txt");

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
		return "icd10gm2014";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2015";
	}

	@Override
	public int getSortIndex()
	{
		return 2015;
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
		return new Icd10GM2015NodeWalker(getRootNode());
	}
}

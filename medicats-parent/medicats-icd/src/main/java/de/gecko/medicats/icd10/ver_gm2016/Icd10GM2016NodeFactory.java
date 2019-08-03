package de.gecko.medicats.icd10.ver_gm2016;

import de.gecko.medicats.icd10.FileSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.ZipSource;
import de.gecko.medicats.icd10.claml.AbstractClamlIcdNodeFactory;

public class Icd10GM2016NodeFactory extends AbstractClamlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm2016.zip", 2765423985L);

	private final FileSource clamlDtd = new FileSource(zip, "x1gec2016", "Klassifikationsdateien", "ClaML.dtd");
	private final FileSource clamlXml = new FileSource(zip, "x1gec2016", "Klassifikationsdateien",
			"icd10gm2016syst_claml_20150925.xml");
	private FileSource transitionFile = new FileSource(zip, "x1gut2016", "Klassifikationsdateien",
			"icd10gm2016syst_umsteiger_2015_2016.txt");
	private FileSource systFile = new FileSource(zip, "x1gut2016", "Klassifikationsdateien", "icd10gm2016syst.txt");

	@Override
	public String getName()
	{
		return "ICD-10-GM 2016";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.430";
	}

	@Override
	public String getPreviousVersion()
	{
		return "icd10gm2015";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2016";
	}

	@Override
	public int getSortIndex()
	{
		return 2016;
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
		return new Icd10GM2016NodeWalker(getRootNode());
	}
}

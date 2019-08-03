package de.gecko.medicats.icd10.ver_gm2017;

import de.gecko.medicats.icd10.FileSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.ZipSource;
import de.gecko.medicats.icd10.claml.AbstractClamlIcdNodeFactory;

public class Icd10GM2017NodeFactory extends AbstractClamlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm2017.zip", 2559393847L);

	private final FileSource clamlDtd = new FileSource(zip, "x1gec2017", "x1gec2017", "Klassifikationsdateien",
			"ClaML.dtd");
	private final FileSource clamlXml = new FileSource(zip, "x1gec2017", "x1gec2017", "Klassifikationsdateien",
			"icd10gm2017syst_claml_20160923.xml");
	private FileSource transitionFile = new FileSource(zip, "x1gut2017", "Klassifikationsdateien",
			"icd10gm2017syst_umsteiger_2016_2017.txt");
	private FileSource systFile = new FileSource(zip, "x1gut2017", "Klassifikationsdateien", "icd10gm2017syst.txt");

	@Override
	public String getName()
	{
		return "ICD-10-GM 2017";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.463";
	}

	@Override
	public String getPreviousVersion()
	{
		return "icd10gm2016";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2017";
	}

	@Override
	public int getSortIndex()
	{
		return 2017;
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
	protected FileSource getTransitionFile()
	{
		return transitionFile;
	}

	@Override
	protected FileSource getSystFile()
	{
		return systFile;
	}

	@Override
	public IcdNodeWalker createNodeWalker()
	{
		return new Icd10GM2017NodeWalker(getRootNode());
	}
}

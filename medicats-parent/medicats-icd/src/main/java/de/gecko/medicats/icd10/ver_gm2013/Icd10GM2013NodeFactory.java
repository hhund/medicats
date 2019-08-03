package de.gecko.medicats.icd10.ver_gm2013;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.claml.AbstractClamlIcdNodeFactory;

public class Icd10GM2013NodeFactory extends AbstractClamlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm2013.zip", 2422325611L);

	private final FileSource clamlDtd = new FileSource(zip, "x1gec2013", "Klassifikationsdateien", "ClaML.dtd");
	private final FileSource clamlXml = new FileSource(zip, "x1gec2013", "Klassifikationsdateien",
			"icd10gm2013syst_claml_20120921.xml");
	private FileSource transitionFile = new FileSource(zip, "x1gua2013", "Klassifikationsdateien",
			"icd10gm2013syst_umsteiger_2012_2013.txt");
	private FileSource systFile = new FileSource(zip, "x1gua2013", "Klassifikationsdateien", "icd10gm2013syst.txt");

	@Override
	public String getName()
	{
		return "ICD-10-GM 2013";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.413";
	}

	@Override
	public String getPreviousVersion()
	{
		return "icd10gm2012";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2013";
	}

	@Override
	public int getSortIndex()
	{
		return 2013;
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
		return new Icd10GM2013NodeWalker(getRootNode());
	}
}

package de.gecko.medicats.icd10.ver_gm2010;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.claml.AbstractClamlIcdNodeFactory;

public class Icd10GM2010NodeFactory extends AbstractClamlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm2010.zip", 3836652669L);

	private final FileSource clamlDtd = new FileSource(zip, "x1gec2010", "Klassifikationsdateien", "claml.dtd");
	private final FileSource clamlXml = new FileSource(zip, "x1gec2010", "Klassifikationsdateien",
			"icd10gm2010syst_claml_20091019.xml");
	private FileSource transitionFile = new FileSource(zip, "x1ueb2009_2010", "Klassifikationsdateien",
			"umsteiger_icd10gmsyst2009_icd10gmsyst2010.txt");
	private FileSource systFile = new FileSource(zip, "x1ueb2009_2010", "Klassifikationsdateien",
			"icd10gmsyst2010.txt");

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
		return "icd10gm2009";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2010";
	}

	@Override
	public int getSortIndex()
	{
		return 2010;
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
		return new Icd10GM2010NodeWalker(getRootNode());
	}
}

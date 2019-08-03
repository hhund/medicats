package de.gecko.medicats.icd10.ver_gm2011;

import de.gecko.medicats.icd10.FileSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.ZipSource;
import de.gecko.medicats.icd10.claml.AbstractClamlIcdNodeFactory;

public class Icd10GM2011NodeFactory extends AbstractClamlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm2011.zip", 230891575L);

	private final FileSource clamlDtd = new FileSource(zip, "x1gec2011", "Klassifikationsdateien", "claml.dtd");
	private final FileSource clamlXml = new FileSource(zip, "x1gec2011", "Klassifikationsdateien",
			"icd10gm2011syst_claml_20100924.xml");
	private FileSource transitionFile = new FileSource(zip, "x1ueb2010_2011", "Klassifikationsdateien",
			"umsteiger_icd10gmsyst2010_icd10gmsyst2011.txt");
	private FileSource systFile = new FileSource(zip, "x1ueb2010_2011", "Klassifikationsdateien",
			"icd10gmsyst2011.txt");

	@Override
	public String getName()
	{
		return "ICD-10-GM 2011";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.388";
	}

	@Override
	public String getPreviousVersion()
	{
		return "icd10gm2010";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2011";
	}

	@Override
	public int getSortIndex()
	{
		return 2011;
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
		return new Icd10GM2011NodeWalker(getRootNode());
	}
}

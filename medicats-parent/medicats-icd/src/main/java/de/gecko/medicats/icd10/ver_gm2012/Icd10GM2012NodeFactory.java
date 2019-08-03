package de.gecko.medicats.icd10.ver_gm2012;

import de.gecko.medicats.icd10.FileSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.ZipSource;
import de.gecko.medicats.icd10.claml.AbstractClamlIcdNodeFactory;

public class Icd10GM2012NodeFactory extends AbstractClamlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm2012.zip", 2907309684L);

	private final FileSource clamlDtd = new FileSource(zip, "x1gec2012", "Klassifikationsdateien", "claml.dtd");
	private final FileSource clamlXml = new FileSource(zip, "x1gec2012", "Klassifikationsdateien",
			"icd10gm2012syst_claml_20110923.xml");
	private FileSource transitionFile = new FileSource(zip, "x1ueb2011_2012", "Klassifikationsdateien",
			"umsteiger_icd10gmsyst2011_icd10gmsyst2012.txt");
	private FileSource systFile = new FileSource(zip, "x1ueb2011_2012", "Klassifikationsdateien",
			"icd10gmsyst2012.txt");

	@Override
	public String getName()
	{
		return "ICD-10-GM 2012";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.409";
	}

	@Override
	public String getPreviousVersion()
	{
		return "icd10gm2011";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2012";
	}

	@Override
	public int getSortIndex()
	{
		return 2012;
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
		return new Icd10GM2012NodeWalker(getRootNode());
	}
}

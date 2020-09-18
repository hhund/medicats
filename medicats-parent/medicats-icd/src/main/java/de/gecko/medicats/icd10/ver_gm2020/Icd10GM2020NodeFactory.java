package de.gecko.medicats.icd10.ver_gm2020;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.claml.AbstractClamlIcdNodeFactory;

public class Icd10GM2020NodeFactory extends AbstractClamlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource clamlZip = new ZipSource(ZipSource.getBasePath(), "icd10gm2020syst-claml.zip", 3288234048L);
	private final ZipSource ueberlZip = new ZipSource(ZipSource.getBasePath(), "icd10gm2020syst-ueberl.zip",
			635233128L);

	private final FileSource clamlDtd = new FileSource(clamlZip, "Klassifikationsdateien", "ClaML.dtd");
	private final FileSource clamlXml = new FileSource(clamlZip, "Klassifikationsdateien",
			"icd10gm2020syst_claml_20190920.xml");
	private FileSource transitionFile = new FileSource(ueberlZip, "Klassifikationsdateien",
			"icd10gm2020syst_umsteiger_2019_2020.txt");
	private FileSource systFile = new FileSource(ueberlZip, "Klassifikationsdateien", "icd10gm2020syst.txt");

	@Override
	public String getName()
	{
		return "ICD-10-GM 2020";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.486";
	}

	@Override
	public String getPreviousVersion()
	{
		return "icd10gm2019";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2020";
	}

	@Override
	public int getSortIndex()
	{
		return 2020;
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
		return new Icd10GM2020NodeWalker(getRootNode());
	}
}

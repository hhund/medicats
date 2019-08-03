package de.gecko.medicats.icd10.ver_gm2019;

import de.gecko.medicats.icd10.FileSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.ZipSource;
import de.gecko.medicats.icd10.claml.AbstractClamlIcdNodeFactory;

public class Icd10GM2019NodeFactory extends AbstractClamlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource clamlZip = new ZipSource(ZipSource.getBasePath(), "icd10gm2019syst-claml.zip", 3111705888L);
	private final ZipSource ueberlZip = new ZipSource(ZipSource.getBasePath(), "icd10gm2019syst-ueberl.zip",
			924198468L);

	private final FileSource clamlDtd = new FileSource(clamlZip, "Klassifikationsdateien", "ClaML.dtd");
	private final FileSource clamlXml = new FileSource(clamlZip, "Klassifikationsdateien",
			"icd10gm2019syst_claml_20180921.xml");
	private FileSource transitionFile = new FileSource(ueberlZip, "Klassifikationsdateien",
			"icd10gm2019syst_umsteiger_2018_2019.txt");
	private FileSource systFile = new FileSource(ueberlZip, "Klassifikationsdateien", "icd10gm2019syst.txt");

	@Override
	public String getName()
	{
		return "ICD-10-GM 2018";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.477";
	}

	@Override
	public String getPreviousVersion()
	{
		return "icd10gm2018";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2019";
	}

	@Override
	public int getSortIndex()
	{
		return 2019;
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
		return new Icd10GM2019NodeWalker(getRootNode());
	}
}

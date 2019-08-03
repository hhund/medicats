package de.gecko.medicats.alphaid.ver_2016;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;

public class AlphaId2016NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "alphaid2016.zip", 3586277876L);
	private final FileSource dataFile = new FileSource(zip, "icd10gm2016_alphaidse_edvtxt_20151002.txt");

	@Override
	public String getName()
	{
		return "Alpha-ID 2016";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.432";
	}

	@Override
	public String getVersion()
	{
		return "alphaid2016";
	}

	@Override
	public String getPreviousVersion()
	{
		return "alphaid2015";
	}

	@Override
	public String getIcdVersion()
	{
		return "icd10gm2016";
	}

	@Override
	public int getSortIndex()
	{
		return 2016;
	}

	@Override
	protected FileSource getDataFile()
	{
		return dataFile;
	}
}

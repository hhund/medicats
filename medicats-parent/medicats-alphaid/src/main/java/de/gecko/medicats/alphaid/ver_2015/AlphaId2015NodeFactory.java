package de.gecko.medicats.alphaid.ver_2015;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;

public class AlphaId2015NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "alphaid2015.zip", 3141918889L);
	private final FileSource dataFile = new FileSource(zip, "icd10gm2015_alphaidse_edvtxt_20140930.txt");

	@Override
	public String getName()
	{
		return "Alpha-ID 2015";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.426";
	}

	@Override
	public String getVersion()
	{
		return "alphaid2015";
	}

	@Override
	public String getPreviousVersion()
	{
		return "alphaid2014";
	}

	@Override
	public String getIcdVersion()
	{
		return "icd10gm2015";
	}

	@Override
	public int getSortIndex()
	{
		return 2015;
	}

	@Override
	protected FileSource getDataFile()
	{
		return dataFile;
	}
}

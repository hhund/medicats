package de.gecko.medicats.alphaid.ver_2017;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;

public class AlphaId2017NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "alphaid2017.zip", 2711103775L);
	private final FileSource dataFile = new FileSource(zip, "icd10gm2017_alphaidse_edvtxt_20161005.txt");

	@Override
	public String getName()
	{
		return "Alpha-ID 2017";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.465";
	}

	@Override
	public String getVersion()
	{
		return "alphaid2017";
	}

	@Override
	public String getPreviousVersion()
	{
		return "alphaid2016";
	}

	@Override
	public String getIcdVersion()
	{
		return "icd10gm2017";
	}

	@Override
	public int getSortIndex()
	{
		return 2017;
	}

	@Override
	protected FileSource getDataFile()
	{
		return dataFile;
	}

	@Override
	protected Charset getDataFileEncoding()
	{
		return StandardCharsets.UTF_8;
	}
}

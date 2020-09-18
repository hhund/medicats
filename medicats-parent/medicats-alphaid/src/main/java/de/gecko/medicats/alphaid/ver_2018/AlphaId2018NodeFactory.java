package de.gecko.medicats.alphaid.ver_2018;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;

public class AlphaId2018NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "alphaid2018.zip", 1407048992L,
			StandardCharsets.ISO_8859_1);
	private final FileSource dataFile = new FileSource(zip, "icd10gm2018_alphaidse_edvtxt_20171004.txt");

	@Override
	public String getName()
	{
		return "Alpha-ID 2018";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.473";
	}

	@Override
	public String getVersion()
	{
		return "alphaid2018";
	}

	@Override
	public String getPreviousVersion()
	{
		return "alphaid2017";
	}

	@Override
	public String getIcdVersion()
	{
		return "icd10gm2018";
	}

	@Override
	public int getSortIndex()
	{
		return 2018;
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

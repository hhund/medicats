package de.gecko.medicats.alphaid.ver_2019;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;

public class AlphaId2019NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "alphaid2019.zip", 2597901585L);
	private final FileSource dataFile = new FileSource(zip, "Klassifikationsdateien",
			"icd10gm2019_alphaidse_edvtxt_20181005.txt");

	@Override
	public String getName()
	{
		return "Alpha-ID 2019";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.479";
	}

	@Override
	public String getVersion()
	{
		return "alphaid2019";
	}

	@Override
	public String getPreviousVersion()
	{
		return "alphaid2018";
	}

	@Override
	public String getIcdVersion()
	{
		return "icd10gm2019";
	}

	@Override
	public int getSortIndex()
	{
		return 2019;
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

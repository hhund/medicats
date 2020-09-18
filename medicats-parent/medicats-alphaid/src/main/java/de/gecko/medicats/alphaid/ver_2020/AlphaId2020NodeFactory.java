package de.gecko.medicats.alphaid.ver_2020;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;

public class AlphaId2020NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "alphaid2020.zip", 1257909204L);
	private final FileSource dataFile = new FileSource(zip, "Klassifikationsdateien",
			"icd102020_alphaidse_edvtxt_20191004.txt");

	@Override
	public String getName()
	{
		return "Alpha-ID 2020";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.488";
	}

	@Override
	public String getVersion()
	{
		return "alphaid2020";
	}

	@Override
	public String getPreviousVersion()
	{
		return "alphaid2019";
	}

	@Override
	public String getIcdVersion()
	{
		return "icd10gm2020";
	}

	@Override
	public int getSortIndex()
	{
		return 2020;
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

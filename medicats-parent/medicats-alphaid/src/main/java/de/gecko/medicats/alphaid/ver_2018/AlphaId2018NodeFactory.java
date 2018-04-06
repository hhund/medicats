package de.gecko.medicats.alphaid.ver_2018;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;

public class AlphaId2018NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
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
	protected String getDataFileName()
	{
		return "icd10gm2018_alphaidse_edvtxt_20171004.txt";
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("alphaid2018.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 1407048992L;
	}

	@Override
	protected Path getDataFileResourcePath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath(getDataFileName());
	}

	@Override
	protected Charset getDataFileEncoding()
	{
		return StandardCharsets.UTF_8;
	}
}

package de.gecko.medicats.alphaid.ver_2017;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;

public class AlphaId2017NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
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
	protected String getDataFileName()
	{
		return "icd10gm2017_alphaidse_edvtxt_20161005.txt";
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("alphaid2017.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 2711103775L;
	}

	@Override
	protected Path getDataFileResourcePath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath(getDataFileName());
	}
}

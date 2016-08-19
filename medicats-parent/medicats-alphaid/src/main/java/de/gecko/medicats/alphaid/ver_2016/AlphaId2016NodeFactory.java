package de.gecko.medicats.alphaid.ver_2016;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;

public class AlphaId2016NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
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
	protected String getDataFileName()
	{
		return "icd10gm2016_alphaidse_edvtxt_20151002.txt";
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("alphaid2016.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 3586277876L;
	}

	@Override
	protected Path getDataFileResourcePath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath(getDataFileName());
	}
}

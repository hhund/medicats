package de.gecko.medicats.alphaid.vers_2015;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;

public class AlphaId2015NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
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
	protected String getDataFileName()
	{
		return "icd10gm2015_alphaidse_edvtxt_20140930.txt";
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("alphaid2015.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 3141918889L;
	}

	@Override
	protected Path getDataFileResourcePath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath(getDataFileName());
	}
}

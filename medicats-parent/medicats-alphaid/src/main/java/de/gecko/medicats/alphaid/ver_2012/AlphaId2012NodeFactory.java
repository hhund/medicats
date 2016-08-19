package de.gecko.medicats.alphaid.ver_2012;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import org.apache.commons.csv.CSVRecord;

import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNode;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;

public class AlphaId2012NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
	@Override
	public String getName()
	{
		return "Alpha-ID 2012";
	}
	
	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.408";
	}
	
	@Override
	public String getVersion()
	{
		return "alphaid2012";
	}

	@Override
	public String getPreviousVersion()
	{
		return "alphaid2011";
	}

	@Override
	public String getIcdVersion()
	{
		return "icd10gm2012";
	}

	@Override
	public int getSortIndex()
	{
		return 2012;
	}

	@Override
	protected String getDataFileName()
	{
		return "icd10gm2012_alphaid_edv_ascii_20110930.txt";
	}

	@Override
	protected AlphaIdNode createNode(AlphaIdNode root, CSVRecord r)
	{
		boolean valid = validFromString(r.get(0));
		String alphaId = r.get(1);
		String primaryIcdCode = nullIfEmpty(r.get(2));
		String asterixIcdCode = nullIfEmpty(r.get(3));
		String additionalIcdCode = nullIfEmpty(r.get(4));
		String label = r.get(5);

		return new AlphaIdNode(root, alphaId, valid, primaryIcdCode, asterixIcdCode, additionalIcdCode, null, label);
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("alphaid2012.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 1521062171L;
	}

	@Override
	protected Path getDataFileResourcePath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath(getDataFileName());
	}
}

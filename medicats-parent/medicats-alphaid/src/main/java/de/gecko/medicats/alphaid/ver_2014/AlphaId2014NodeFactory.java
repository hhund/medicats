package de.gecko.medicats.alphaid.ver_2014;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVRecord;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNode;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;

public class AlphaId2014NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "alphaid2014.zip", 863994774L);
	private final FileSource dataFile = new FileSource(zip, "icd10gm2014_alphaid_edvascii_20130930.txt");

	@Override
	public String getName()
	{
		return "Alpha-ID 2014";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.419";
	}

	@Override
	public String getVersion()
	{
		return "alphaid2014";
	}

	@Override
	public String getPreviousVersion()
	{
		return "alphaid2013";
	}

	@Override
	public String getIcdVersion()
	{
		return "icd10gm2014";
	}

	@Override
	public int getSortIndex()
	{
		return 2014;
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

}

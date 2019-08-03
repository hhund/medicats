package de.gecko.medicats.alphaid.ver_2010;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNode;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;

public class AlphaId2010NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "alphaid2010.zip", 2138830206L);
	private final FileSource dataFile = new FileSource(zip, "icd10gm2010_alphaid_edv_ascii20091030.txt");

	@Override
	public String getName()
	{
		return "Alpha-ID 2010";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.383";
	}

	@Override
	public String getVersion()
	{
		return "alphaid2010";
	}

	@Override
	public String getPreviousVersion()
	{
		return "alphaid2009";
	}

	@Override
	public String getIcdVersion()
	{
		return "icd10gm2010";
	}

	@Override
	public int getSortIndex()
	{
		return 2010;
	}

	@Override
	protected FileSource getDataFile()
	{
		return dataFile;
	}

	@Override
	protected CSVFormat createCsvFormat()
	{
		return CSVFormat.newFormat(';');
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

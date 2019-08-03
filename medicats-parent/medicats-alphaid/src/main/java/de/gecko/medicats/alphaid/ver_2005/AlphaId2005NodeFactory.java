package de.gecko.medicats.alphaid.ver_2005;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNode;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeRoot;

public class AlphaId2005NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "dimdi-idt2005.zip", 2007174286L);
	private final FileSource dataFile = new FileSource(zip, "dimdi-idt2005.txt");

	@Override
	public String getName()
	{
		return "Alpha-ID 2005";
	}

	@Override
	public String getOid()
	{
		return "";
	}

	@Override
	public String getVersion()
	{
		return "alphaid2005";
	}

	@Override
	public String getPreviousVersion()
	{
		return "alphaid2004";
	}

	@Override
	public String getIcdVersion()
	{
		return "icd10gm2005";
	}

	@Override
	public int getSortIndex()
	{
		return 2005;
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
	protected boolean skipNode(AlphaIdNodeRoot root, CSVRecord t)
	{
		if (t.size() < 6)
			return true;
		else
			return super.skipNode(root, t);
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

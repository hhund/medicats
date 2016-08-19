package de.gecko.medicats.alphaid.ver_2005;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import de.gecko.medicats.alphaid.AbstractAlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNode;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeRoot;

public class AlphaId2005NodeFactory extends AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
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
	protected String getDataFileName()
	{
		return "dimdi-idt2005.txt";
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

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("dimdi-idt2005.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 2007174286L;
	}

	@Override
	protected Path getDataFileResourcePath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath(getDataFileName());
	}
}

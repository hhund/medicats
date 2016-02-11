package de.gecko.medicats.ops;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public final class PreviousOpsCodesReader
{
	private PreviousOpsCodesReader()
	{
	}

	public static Map<String, String> read(InputStream transitionFileStream, int previousColumn, int currentColumn)
	{
		Map<String, String> umsteiger = new HashMap<>();

		try (Reader reader = new InputStreamReader(transitionFileStream, StandardCharsets.UTF_8);
				CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter(';')))
		{
			for (final CSVRecord record : parser)
			{
				String previousCode = record.get(previousColumn);
				String currentCode = record.get(currentColumn);

				umsteiger.put(currentCode, previousCode.equals("None") ? null : previousCode);
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		return umsteiger;
	}
}

package de.gecko.medicats.ops;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.gecko.medicats.PreviousCodeMapping;
import de.gecko.medicats.PreviousCodeMapping.Compatible;
import de.gecko.medicats.PreviousCodeMappings;

public final class PreviousOpsCodesReader
{
	private PreviousOpsCodesReader()
	{
	}

	public static PreviousCodeMappings read(InputStream transitionFileStream, int previousCodesColumn,
			int currentCodesColumn, int previousCodesForwardsCompatibleColumn,
			int currentCodesBackwardsCompatibleColumn)
	{
		List<PreviousCodeMapping> mappings = new ArrayList<>();

		try (Reader reader = new InputStreamReader(transitionFileStream, StandardCharsets.UTF_8);
				CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.builder().setDelimiter(';').build()))
		{
			for (final CSVRecord record : parser)
			{
				String previousCode = record.get(previousCodesColumn);
				String currentCode = record.get(currentCodesColumn);

				Compatible previousCodeForwardsCompatible = previousCodesForwardsCompatibleColumn >= record.size()
						|| previousCodesForwardsCompatibleColumn < 0 ? Compatible.UNKNOWN
								: ("A".equals(record.get(previousCodesForwardsCompatibleColumn)) ? Compatible.YES
										: Compatible.NO);
				Compatible currentCodeBackwardsCompatible = currentCodesBackwardsCompatibleColumn >= record.size()
						|| currentCodesBackwardsCompatibleColumn < 0 ? Compatible.UNKNOWN
								: ("A".equals(record.get(currentCodesBackwardsCompatibleColumn)) ? Compatible.YES
										: Compatible.NO);

				if (!previousCode.equals("None"))
					mappings.add(new PreviousCodeMapping(previousCode, currentCode, previousCodeForwardsCompatible,
							currentCodeBackwardsCompatible));
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		return new PreviousCodeMappings(mappings);
	}
}

package de.gecko.medicats.icd10;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.gecko.medicats.PreviousCodeMapping;
import de.gecko.medicats.PreviousCodeMappings;
import de.gecko.medicats.PreviousCodeMapping.Compatible;

public final class PreviousIcdCodesReader
{
	private static final String UNDEFINED = "UNDEF";

	private PreviousIcdCodesReader()
	{
	}

	public static PreviousCodeMappings read(InputStream transitionFileStream)
	{
		return read(transitionFileStream, StandardCharsets.UTF_8);
	}

	public static PreviousCodeMappings read(InputStream transitionFileStream, Charset charset)
	{
		List<PreviousCodeMapping> mappings = new ArrayList<>();

		try (Reader reader = new InputStreamReader(transitionFileStream, charset);
				CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter(';')))
		{
			for (final CSVRecord record : parser)
			{
				String previousCode = record.get(0);
				String currentCode = record.get(1);

				Compatible previousCodeForwardsCompatible = "A".equals(record.get(2)) ? Compatible.YES : Compatible.NO;
				Compatible currentCodeBackwardsCompatible = "A".equals(record.get(3)) ? Compatible.YES : Compatible.NO;

				if (!UNDEFINED.equals(previousCode))
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

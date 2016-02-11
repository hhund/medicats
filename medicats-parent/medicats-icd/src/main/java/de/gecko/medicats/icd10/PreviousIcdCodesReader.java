package de.gecko.medicats.icd10;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public final class PreviousIcdCodesReader
{
	private static final String UNDEFINED = "UNDEF";

	private PreviousIcdCodesReader()
	{
	}

	public static Map<String, String> read(InputStream transitionFileStream)
	{
		return read(transitionFileStream, StandardCharsets.UTF_8);
	}

	public static Map<String, String> read(InputStream transitionFileStream, Charset charset)
	{
		Map<String, String> umsteiger = new HashMap<>();

		try (Reader reader = new InputStreamReader(transitionFileStream, charset);
				CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter(';')))
		{
			for (final CSVRecord record : parser)
			{
				String previousCode = record.get(0);
				String code = record.get(1);

				umsteiger.put(code, UNDEFINED.equals(previousCode) ? null : previousCode);
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		return umsteiger;
	}
}

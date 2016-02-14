package de.gecko.medicats.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import de.gecko.medicats.ops.OpsNode.OpsNodeType;

public abstract class AbstractOpsTest
{
	protected static class OpsEntry implements Comparable<OpsEntry>
	{
		final String code, label;

		OpsEntry(String code, String label)
		{
			this.code = Objects.requireNonNull(code, "code");
			this.label = Objects.requireNonNull(label, "label");
		}

		@Override
		public int compareTo(OpsEntry o)
		{
			return code.compareTo(o.code);
		}
	}

	private List<OpsEntry> opsCodeSorted;
	private OpsNodeWalker opsNodeWalker;

	private OpsNodeWalker getOpsNodeWalker()
	{
		if (opsNodeWalker == null)
		{
			OpsNodeFactory factory = OpsService.getService().getNodeFactory(getVersion());
			opsNodeWalker = factory.createNodeWalker();
		}

		return opsNodeWalker;
	}

	protected List<OpsEntry> getOpsCodesSorted()
	{
		if (opsCodeSorted == null)
		{
			OpsNodeFactory factory = OpsService.getService().getNodeFactory(getVersion());
			assertTrue(factory instanceof AbstractOpsNodeFactory);
			AbstractOpsNodeFactory f = (AbstractOpsNodeFactory) factory;

			opsCodeSorted = readOpsCodes(getSystFileInputStream(getTransitionZip(f))).stream().sorted()
					.collect(Collectors.toList());
		}

		return opsCodeSorted;
	}

	protected FileSystem getTransitionZip(AbstractOpsNodeFactory f)
	{
		return f.getTransitionZip();
	}

	protected InputStream getSystFileInputStream(FileSystem fileSystem)
	{
		try
		{
			return Files.newInputStream(getSystFilePath(fileSystem));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected abstract Path getSystFilePath(FileSystem transitionZip);

	protected abstract String getVersion();

	private List<OpsEntry> readOpsCodes(InputStream systFileInputstream)
	{
		List<OpsEntry> codes = new ArrayList<>();

		try (Reader reader = new InputStreamReader(systFileInputstream, getCharset());
				CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter(';')))
		{
			for (final CSVRecord record : parser)
			{
				parseRow(codes, record);
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.err.println("Error ~ at " + codes.size());
			throw e;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		return codes;
	}

	protected void parseRow(List<OpsEntry> codes, final CSVRecord record)
	{
		String code = record.get(0);
		String label = record.get(1);

		if (code != null && !code.equals("UNDEF") && !code.equals("None") && !code.equals("KOMBI") && label != null
				&& !label.equals("Undefined") && !label.equals("Nicht definiert")
				&& !label.equals("Kombinationsschlüsselnummer erforderlich"))
		{
			OpsEntry o = createOpsEntry(code, label);
			if (o != null)
				codes.add(o);
		}
	}

	protected OpsEntry createOpsEntry(String code, String label)
	{
		return new OpsEntry(code, label);
	}

	protected Charset getCharset()
	{
		return StandardCharsets.UTF_8;
	}

	@Test
	public void testCodesOk() throws Exception
	{
		List<OpsNode> categories = getOpsNodeWalker().preOrderStream()
				.filter(n -> OpsNodeType.CATEGORY.equals(n.getNodeType()) && n.getCodeLength() >= 5)
				.sorted((n1, n2) -> n1.getCode().compareTo(n2.getCode())).collect(Collectors.toList());

		categories = filterCategories(categories);

		List<OpsEntry> opsCodes = getOpsCodesSorted();

		for (int i = 0; i < categories.size(); i++)
		{
			try
			{
				assertEquals(opsCodes.get(i).code, categories.get(i).getCode());
			}
			catch (AssertionError e)
			{
				System.err.println("txt -> tree");
				for (int j = Math.max(0, i - 10); j < Math.min(i + 10, categories.size()); j++)
				{
					if (j == i)
						System.err.println();
					System.err.println(opsCodes.get(j).code + " -> " + categories.get(j).getCode());
					if (j == i)
						System.err.println();
				}
				throw e;
			}
		}

		if (opsCodes.size() > categories.size())
		{
			System.err.println("icdCodes > categories");
			for (int i = categories.size(); i < opsCodes.size(); i++)
				System.err.println(opsCodes.get(i).code + " -> ?");
		}
		else if (categories.size() > opsCodes.size())
		{
			System.err.println("categories > icdCodes");
			for (int i = opsCodes.size(); i < categories.size(); i++)
				System.err.println("? -> " + categories.get(i).getCode());
		}

		assertEquals(opsCodes.size(), categories.size());
	}

	protected List<OpsNode> filterCategories(List<OpsNode> categories)
	{
		return categories;
	}

	@Test
	public void testLabelsOk() throws Exception
	{
		List<OpsNode> categories = getOpsNodeWalker().preOrderStream()
				.filter(n -> OpsNodeType.CATEGORY.equals(n.getNodeType()) && n.getCodeLength() >= 5)
				.sorted((n1, n2) -> n1.getCode().compareTo(n2.getCode())).collect(Collectors.toList());

		categories = filterCategories(categories);

		List<OpsEntry> opsCodes = getOpsCodesSorted();

		for (int i = 0; i < categories.size(); i++)
		{
			try
			{
				if (opsCodes.get(i).label.length() < categories.get(i).getLabel().length())
					System.err.println("WARNING our label for " + opsCodes.get(i).code + " is longer");
				else
					assertEquals(opsCodes.get(i).label, categories.get(i).getLabel());
			}
			catch (AssertionError e)
			{
				System.err.println("txt\ntree");
				for (int j = Math.max(0, i - 10); j < Math.min(i + 10, categories.size()); j++)
				{
					if (j == i)
						System.err.println();
					System.err.println(opsCodes.get(j).code + ": " + opsCodes.get(j).label);
					System.err.println(categories.get(j).getCode() + ": " + categories.get(j).getLabel());
					if (j == i)
						System.err.println();
				}
				throw e;
			}
		}

		if (opsCodes.size() > categories.size())
		{
			System.err.println("opsEntries > categories");
			for (int i = categories.size(); i < opsCodes.size(); i++)
				System.err.println(opsCodes.get(i).label + " -> ?");
		}
		else if (categories.size() > opsCodes.size())
		{
			System.err.println("categories > opsEntries");
			for (int i = opsCodes.size(); i < categories.size(); i++)
				System.err.println("? -> " + categories.get(i).getLabel());
		}

		assertEquals(opsCodes.size(), categories.size());
	}
}

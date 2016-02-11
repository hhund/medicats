package de.gecko.medicats.icd10;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import de.gecko.medicats.icd10.IcdNode.IcdNodeType;

public abstract class AbstractIcd10GMTest
{
	protected abstract String getVersion();

	protected List<String> readCodes(InputStream systFileInputstream)
	{
		List<String> codes = new ArrayList<>();

		try (Reader reader = new InputStreamReader(systFileInputstream, StandardCharsets.UTF_8);
				CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter(';')))
		{
			for (final CSVRecord record : parser)
			{
				String code = record.get(0);
				// String text = record.get(1);

				if (code != null && !code.equals("UNDEF") && !code.equals("\uFEFF" + "UNDEF"))
					codes.add(code);
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		return codes;
	}

	@Test
	public void testCategoriesOk() throws Exception
	{
		IcdNodeFactory factory = IcdService.getService().getNodeFactory(getVersion());

		IcdNodeWalker nodeWalker = factory.createNodeWalker();

		List<IcdNode> categories = nodeWalker.preOrderStream().filter(n -> IcdNodeType.CATEGORY.equals(n.getNodeType()))
				.sorted((n1, n2) -> n1.getCode().compareTo(n2.getCode())).collect(Collectors.toList());

		assertTrue(factory instanceof AbstractIcdNodeFactory);
		AbstractIcdNodeFactory f = (AbstractIcdNodeFactory) factory;

		List<String> icdCodes = readCodes(getSystFileInputStream(getTransitionZip(f))).stream().sorted()
				.collect(Collectors.toList());

		for (int i = 0; i < categories.size(); i++)
		{
			try
			{
				assertEquals(icdCodes.get(i), categories.get(i).getCode());
			}
			catch (AssertionError e)
			{
				System.err.println("txt -> tree");
				for (int j = Math.max(0, i - 10); j < Math.min(i + 10, categories.size()); j++)
				{
					if (j == i)
						System.err.println();
					System.err.println(icdCodes.get(j) + " -> " + categories.get(j).getCode());
					if (j == i)
						System.err.println();
				}

				throw e;
			}
		}

		if (icdCodes.size() > categories.size())
		{
			System.err.println("icdCodes > categories");
			for (int i = categories.size(); i < icdCodes.size(); i++)
				System.err.println(icdCodes.get(i) + " -> ?");
		}
		else if (categories.size() > icdCodes.size())
		{
			System.err.println("categories > icdCodes");
			for (int i = icdCodes.size(); i < categories.size(); i++)
				System.err.println("? -> " + categories.get(i).getCode());
		}

		assertEquals(icdCodes.size(), categories.size());
	}

	protected FileSystem getTransitionZip(AbstractIcdNodeFactory f)
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

	@Test
	public void testExclusionsDistinct()
	{
		IcdNodeFactory factory = IcdService.getService().getNodeFactory(getVersion());

		IcdNodeWalker nodeWalker = factory.createNodeWalker();

		nodeWalker.walkNodesPreOrder((node, exclusions, inclusions) ->
		{
			List<IcdNode> ex = exclusions.collect(Collectors.toList());

			long distinctCount = ex.stream().filter(distinctByKey(IcdNode::getPath)).count();

			String message = ex.stream().map(IcdNode::getPath).collect(Collectors.joining(", "));
			assertEquals(message, ex.size(), distinctCount);
		});
	}

	@Test
	public void testInclusionsDistinct()
	{
		IcdNodeFactory factory = IcdService.getService().getNodeFactory(getVersion());

		IcdNodeWalker nodeWalker = factory.createNodeWalker();

		nodeWalker.walkNodesPreOrder((node, exclusions, inclusions) ->
		{
			List<IcdNode> in = inclusions.collect(Collectors.toList());

			long distinctCount = in.stream().filter(distinctByKey(IcdNode::getPath)).count();

			String message = in.stream().map(IcdNode::getPath).collect(Collectors.joining(", "));
			assertEquals(message, in.size(), distinctCount);
		});
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
	{
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}

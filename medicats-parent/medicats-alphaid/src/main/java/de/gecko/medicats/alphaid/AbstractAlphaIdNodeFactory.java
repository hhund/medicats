package de.gecko.medicats.alphaid;

import static de.gecko.medicats.VersionedNodeFactory.testChecksum;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public abstract class AbstractAlphaIdNodeFactory implements AlphaIdNodeFactory
{
	public static final String DIMDI_FILES_BASE_PATH_PARAM = "dimdi.files.path";
	public static final String DIMDI_FILES_BASE_PATH_DEFAULT_VALUE = "dimdi";

	private AlphaIdNodeRoot root;

	private FileSystem taxonomyZip;

	private Path getBasePath()
	{
		return Paths.get(System.getProperty(DIMDI_FILES_BASE_PATH_PARAM, DIMDI_FILES_BASE_PATH_DEFAULT_VALUE));
	}

	protected abstract Path getTaxonomyZipFileName(Path basePath);

	protected abstract long getTaxonomyZipChecksum();

	protected FileSystem getTaxonomyZip()
	{
		try
		{
			if (taxonomyZip == null || !taxonomyZip.isOpen())
				taxonomyZip = FileSystems.newFileSystem(
						testChecksum(getTaxonomyZipChecksum(), getTaxonomyZipFileName(getBasePath())), null);

			return taxonomyZip;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected abstract String getDataFileName();

	protected InputStream getDataFileResource()
	{
		try
		{
			return Files.newInputStream(getDataFileResourcePath(getTaxonomyZip()));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected abstract Path getDataFileResourcePath(FileSystem taxonomyZip);

	protected Charset getDataFileEncoding()
	{
		return Charset.forName("Cp1252");
	}

	@Override
	public AlphaIdNodeRoot getRootNode()
	{
		if (root == null)
		{
			root = new AlphaIdNodeRoot(getVersion(), getPreviousVersion(), getIcdVersion());

			try (CSVParser parser = new CSVParser(new InputStreamReader(getDataFileResource(), getDataFileEncoding()),
					createCsvFormat()))
			{
				parser.forEach(t ->
				{
					if (!skipNode(root, t))
					{
						AlphaIdNode node = createNode(root, t);
						node.addToParent();
					}
				});
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}

		return root;
	}

	protected CSVFormat createCsvFormat()
	{
		return CSVFormat.newFormat('|');
	}

	protected boolean skipNode(AlphaIdNodeRoot root, CSVRecord t)
	{
		return false;
	}

	protected AlphaIdNode createNode(AlphaIdNode root, CSVRecord r)
	{
		boolean valid = validFromString(r.get(0));
		String alphaId = r.get(1);
		String primaryIcdCode = removeSuffix(nullIfEmpty(r.get(2)));
		String asterixIcdCode = removeSuffix(nullIfEmpty(r.get(3)));
		String additionalIcdCode = removeSuffix(nullIfEmpty(r.get(4)));
		String orphaId = r.get(5);
		String label = r.get(6);

		return new AlphaIdNode(root, alphaId, valid, primaryIcdCode, asterixIcdCode, additionalIcdCode, orphaId, label);
	}

	protected boolean validFromString(String s)
	{
		switch (s)
		{
			case "1":
				return true;
			case "0":
				return false;
			default:
				throw new IllegalArgumentException("Unexpected value '" + s + "' in valid field");
		}
	}

	protected String nullIfEmpty(String s)
	{
		return s == null || s.isEmpty() ? null : s;
	}

	protected String removeSuffix(String s)
	{
		if (s == null)
			return null;

		if (s.endsWith("!") || s.endsWith("+") || s.endsWith("*"))
			return s.substring(0, s.length() - 1);

		return s;
	}

	@Override
	public boolean supportsVersion(String version)
	{
		return getVersion().equals(version);
	}

	@Override
	public AlphaIdNodeWalker createNodeWalker()
	{
		return new AlphaIdNodeWalkerImpl(getRootNode());
	}
}

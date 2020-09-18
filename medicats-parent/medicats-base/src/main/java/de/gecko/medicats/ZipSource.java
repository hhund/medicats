package de.gecko.medicats;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class ZipSource implements AutoCloseable
{
	public static final String DIMDI_FILES_BASE_PATH_PARAM = "dimdi.files.path";
	public static final String DIMDI_FILES_BASE_PATH_DEFAULT_VALUE = "dimdi";

	public static Path getBasePath()
	{
		return Paths.get(System.getProperty(DIMDI_FILES_BASE_PATH_PARAM, DIMDI_FILES_BASE_PATH_DEFAULT_VALUE));
	}

	private final Path file;
	private final long checksum;
	private final Charset filenameEncoding;

	private FileSystem fileSystem;

	public ZipSource(Path basePath, String filename, long checksum, Charset filenameEncoding)
	{
		this(basePath.resolve(filename), checksum, filenameEncoding);
	}

	public ZipSource(Path basePath, String filename, long checksum)
	{
		this(basePath, filename, checksum, null);
	}

	public ZipSource(Path file, long checksum, Charset filenameEncoding)
	{
		this.file = file;
		this.checksum = checksum;
		this.filenameEncoding = filenameEncoding;

		try
		{
			testChecksum();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public Path getFile()
	{
		return file;
	}

	public long getChecksum()
	{
		return checksum;
	}

	public FileSystem getFileSystem()
	{
		try
		{
			if (fileSystem == null || !fileSystem.isOpen())
			{
				for (FileSystemProvider provider : FileSystemProvider.installedProviders())
				{
					try
					{
						if (filenameEncoding != null)
							return provider.newFileSystem(file, Map.of("encoding", filenameEncoding.name()));
						else
							return provider.newFileSystem(file, Collections.emptyMap());
					}
					catch (UnsupportedOperationException uoe)
					{
					}
				}
			}

			return fileSystem;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void testChecksum() throws IOException
	{
		long calculatedChecksum = FileUtils.checksumCRC32(file.toFile());

		if (calculatedChecksum != getChecksum())
			throw new IOException("CRC32 checksum error for file " + file.toString() + ": " + getChecksum()
					+ " (expected) vs. " + calculatedChecksum + " (calculated)");
	}

	@Override
	public void close() throws Exception
	{
		if (fileSystem != null)
			fileSystem.close();
	}
}

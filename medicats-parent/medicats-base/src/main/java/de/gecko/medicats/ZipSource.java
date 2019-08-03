package de.gecko.medicats;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

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

	private FileSystem fileSystem;

	public ZipSource(Path basePath, String filename, long checksum)
	{
		this(basePath.resolve(filename), checksum);

		try
		{
			testChecksum();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public ZipSource(Path file, long checksum)
	{
		this.file = file;
		this.checksum = checksum;
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
				fileSystem = FileSystems.newFileSystem(file, null);

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
			throw new IOException("CRC32 checksum error, checksum for file " + file.toString() + " unexpected: "
					+ calculatedChecksum);
	}

	@Override
	public void close() throws Exception
	{
		if (fileSystem != null)
			fileSystem.close();
	}
}

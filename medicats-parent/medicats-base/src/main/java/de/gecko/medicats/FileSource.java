package de.gecko.medicats;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSource
{
	private final ZipSource zip;
	private final String contentFilenameFirst;
	private final String[] contentFilenameMore;

	public FileSource(ZipSource zip, String contentFilenameFirst, String... contentFilenameMore)
	{
		this.zip = zip;
		this.contentFilenameFirst = contentFilenameFirst;
		this.contentFilenameMore = contentFilenameMore;
	}

	public InputStream getInputStream()
	{
		try
		{
			Path file = zip.getFileSystem().getPath(contentFilenameFirst, contentFilenameMore);
			return Files.newInputStream(file);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}

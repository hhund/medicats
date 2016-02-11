package de.gecko.medicats;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;

public interface VersionedNodeFactory<N extends Node<N>, W extends NodeWalker<N>> extends NodeFactory<N, W>
{
	String getVersion();

	int getSortIndex();

	boolean supportsVersion(String version);
	
	String getPreviousVersion();
	
	strictfp static Path testChecksum(long expectedChecksum, Path file)
	{
		try
		{
			long calculatedChecksum = FileUtils.checksumCRC32(file.toFile());

			if (calculatedChecksum != expectedChecksum)
				throw new IOException("CRC32 checksum error, checksum for file " + file.toString() + " unexpected: "
						+ calculatedChecksum);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		return file;
	}
}
package de.gecko.medicats.ops;

import static de.gecko.medicats.VersionedNodeFactory.testChecksum;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.gecko.medicats.PreviousCodeMappings;

public abstract class AbstractOpsNodeFactory implements OpsNodeFactory
{
	public static final String DIMDI_FILES_BASE_PATH_PARAM = "dimdi.files.path";
	public static final String DIMDI_FILES_BASE_PATH_DEFAULT_VALUE = "dimdi";

	private PreviousCodeMappings mappings;

	private FileSystem taxonomyZip, transitionZip;

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

	protected abstract Path getTransitionZipFileName(Path basePath);

	protected abstract long getTransitionZipChecksum();

	protected FileSystem getTransitionZip()
	{
		try
		{
			if (transitionZip == null || !transitionZip.isOpen())
			{
				Path transitionZipFileName = getTransitionZipFileName(getBasePath());

				if (transitionZipFileName == null)
					return null;

				transitionZip = FileSystems
						.newFileSystem(testChecksum(getTransitionZipChecksum(), transitionZipFileName), null);
			}

			return transitionZip;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected InputStream getTransitionFileStream()
	{
		if (getTransitionZip() == null)
			return null;

		try
		{
			return Files.newInputStream(getTransitionFilePath(getTransitionZip()));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected abstract Path getTransitionFilePath(FileSystem transitionZip);

	/**
	 * @return 0-based index
	 */
	protected int getPreviousCodesColumn()
	{
		return 0;
	}

	/**
	 * @return 0-based index
	 */
	protected int getCurrentCodesColumn()
	{
		return 2;
	}

	/**
	 * @return 0-based index
	 */
	protected int getCurrentCodesBackwardsCompatibleColumn()
	{
		return 6;
	}

	/**
	 * @return 0-based index
	 */
	protected int getPreviousCodesForwardsCompatibleColumn()
	{
		return 5;
	}

	protected abstract String getPreviousCodesFileName();

	protected PreviousCodeMappings getPreviousCodes()
	{
		if (mappings == null)
		{
			InputStream transitionFileStream = getTransitionFileStream();

			if (transitionFileStream == null)
				mappings = new PreviousCodeMappings(null);
			else
				mappings = PreviousOpsCodesReader.read(transitionFileStream, getPreviousCodesColumn(),
						getCurrentCodesColumn(), getPreviousCodesForwardsCompatibleColumn(), getCurrentCodesBackwardsCompatibleColumn());
		}

		return mappings;
	}

	@Override
	public boolean supportsVersion(String version)
	{
		return getVersion().equals(version);
	}
}

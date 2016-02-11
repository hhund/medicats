package de.gecko.medicats.ops;

import static de.gecko.medicats.VersionedNodeFactory.testChecksum;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

public abstract class AbstractOpsNodeFactory implements OpsNodeFactory
{
	public static final String DIMDI_FILES_BASE_PATH_PARAM = "dimdi.files.path";
	public static final String DIMDI_FILES_BASE_PATH_DEFAULT_VALUE = "dimdi";

	private Map<String, String> previousCodes;

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
	protected int getPreviousCodesPreviousColumn()
	{
		return 0;
	}

	/**
	 * @return 0-based index
	 */
	protected int getPreviousCodesCurrentColumn()
	{
		return 2;
	}

	protected abstract String getPreviousCodesFileName();

	protected Map<String, String> getPreviousCodes()
	{
		if (previousCodes == null)
		{
			InputStream transitionFileStream = getTransitionFileStream();

			if (transitionFileStream == null)
				previousCodes = Collections.emptyMap();
			else
				previousCodes = PreviousOpsCodesReader.read(transitionFileStream, getPreviousCodesPreviousColumn(),
						getPreviousCodesCurrentColumn());
		}

		return previousCodes;
	}

	@Override
	public boolean supportsVersion(String version)
	{
		return getVersion().equals(version);
	}
}

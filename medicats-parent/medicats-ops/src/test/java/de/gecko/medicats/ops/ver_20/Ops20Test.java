package de.gecko.medicats.ops.ver_20;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import de.gecko.medicats.ops.AbstractOpsNodeFactory;
import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops20Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops20";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Opsv20.txt");
	}

	@Override
	protected Charset getCharset()
	{
		return Charset.forName("Cp1252");
	}

	@Override
	protected FileSystem getTransitionZip(AbstractOpsNodeFactory f)
	{
		Path basePath = Paths.get(System.getProperty(AbstractOpsNodeFactory.DIMDI_FILES_BASE_PATH_PARAM,
				AbstractOpsNodeFactory.DIMDI_FILES_BASE_PATH_DEFAULT_VALUE));

		Path zip = basePath.resolve("p1ueb11_20_v11.zip");

		try
		{
			return FileSystems.newFileSystem(zip, null);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	protected OpsEntry createOpsEntry(String code, String label)
	{
		List<String> filterDoubleSpace = Arrays.asList("1-339", "1-460", "1-460.0", "1-460.1", "1-460.2", "1-460.3",
				"1-460.4", "1-460.x", "1-460.y", "1-461", "1-461.0", "1-461.1", "1-461.2", "1-461.3", "1-461.x",
				"1-461.y", "1-462", "1-462.0", "1-462.1", "1-462.2", "1-462.3", "1-462.4", "1-462.x", "1-462.y",
				"1-463", "1-463.0", "1-463.1", "1-463.2", "1-463.3", "1-463.4", "1-463.5", "1-463.6", "1-463.7",
				"1-463.8", "1-463.9", "1-463.x", "1-463.y", "1-464", "1-464.0", "1-464.1", "1-464.2", "1-464.x",
				"1-464.y", "1-465", "1-465.0", "1-465.1", "1-465.2", "1-465.x", "1-465.y", "1-466", "1-466.0",
				"1-466.1", "1-466.2", "1-466.3", "1-466.4", "1-466.5", "1-466.6", "1-466.7", "1-466.x", "1-466.y",
				"1-469", "1-493", "1-493.0", "1-493.1", "1-493.2", "1-493.3", "1-493.4", "1-493.5", "1-493.6",
				"1-493.7", "1-493.8", "1-493.9", "1-493.a", "1-493.b", "1-493.c", "1-493.x", "1-493.y", "5-097.4",
				"5-683.15", "5-683.25", "5-823.25", "5-869.1", "8-102", "8-102.0", "8-102.1", "8-102.2", "8-102.3",
				"8-102.4", "8-102.5", "8-102.6", "8-102.7", "8-102.8", "8-102.9", "8-102.a", "8-102.x", "8-102.y",
				"8-158.t", "8-800", "8-800.0", "8-800.1", "8-800.2", "8-800.3", "8-800.4", "8-800.5", "8-800.x",
				"8-800.y", "8-942", "9-309");

		if (filterDoubleSpace.contains(code))
			label = label.replaceAll("  ", " ");

		return super.createOpsEntry(code, label);
	}
}

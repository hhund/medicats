package de.gecko.medicats.ops.ver_2010;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2010Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2010";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "opssyst2010.txt");
	}
}

package de.gecko.medicats.ops.ver_2011;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2011Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2011";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "opssyst2011.txt");
	}
}

package de.gecko.medicats.ops.ver_2012;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2012Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2012";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "opssyst2012.txt");
	}
}

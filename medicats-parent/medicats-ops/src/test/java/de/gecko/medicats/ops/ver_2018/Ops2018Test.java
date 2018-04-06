package de.gecko.medicats.ops.ver_2018;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2018Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2018";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "ops2018syst.txt");
	}
}

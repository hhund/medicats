package de.gecko.medicats.ops.ver_2017;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2017Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2017";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "ops2017syst.txt");
	}
}

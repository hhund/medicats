package de.gecko.medicats.ops.ver_2016;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2016Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2016";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "ops2016syst.txt");
	}
}

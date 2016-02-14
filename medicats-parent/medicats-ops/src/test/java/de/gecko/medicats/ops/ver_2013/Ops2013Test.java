package de.gecko.medicats.ops.ver_2013;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2013Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2013";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "ops2013syst_20121113.txt");
	}
}

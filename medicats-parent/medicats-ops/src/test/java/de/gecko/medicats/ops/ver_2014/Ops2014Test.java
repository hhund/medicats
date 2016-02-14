package de.gecko.medicats.ops.ver_2014;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2014Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2014";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "ops2014syst_20131104.txt");
	}
}

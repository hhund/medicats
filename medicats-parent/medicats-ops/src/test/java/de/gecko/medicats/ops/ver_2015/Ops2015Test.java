package de.gecko.medicats.ops.ver_2015;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2015Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2015";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "ops2015syst.txt");
	}
}

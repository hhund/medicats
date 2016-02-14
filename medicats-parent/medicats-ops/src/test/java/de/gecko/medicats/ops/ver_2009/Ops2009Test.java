package de.gecko.medicats.ops.ver_2009;

import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2009Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2009";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "opsamtl2009.txt");
	}
	
	@Override
	protected Charset getCharset()
	{
		return Charset.forName("Cp1252");
	}
}

package de.gecko.medicats.ops.ver_2009;

import java.nio.charset.Charset;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2009Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2009";
	}

	@Override
	protected Charset getCharset()
	{
		return Charset.forName("Cp1252");
	}
}

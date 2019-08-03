package de.gecko.medicats.ops.ver_2004;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2004Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2004";
	}

	@Override
	protected Charset getCharset()
	{
		return Charset.forName("Cp1252");
	}

	@Override
	protected OpsEntry createOpsEntry(String code, String label)
	{
		List<String> filterDoubleSpace = Arrays.asList("8-544", "8-544.0", "8-544.1", "9-403.2");

		if (filterDoubleSpace.contains(code))
			label = label.replaceAll("  ", " ");

		return super.createOpsEntry(code, label);
	}
}

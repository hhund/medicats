package de.gecko.medicats.ops.ver_2008;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2008Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2008";
	}

	@Override
	protected Charset getCharset()
	{
		return Charset.forName("Cp1252");
	}

	@Override
	protected OpsEntry createOpsEntry(String code, String label)
	{
		List<String> filterDoubleSpace = Arrays.asList("5-039.34", "5-495.1", "5-495.10", "5-495.11", "5-495.1x",
				"5-495.2", "5-495.20", "5-495.21", "5-495.2x", "5-495.3", "5-495.30", "5-495.31", "5-495.3x");

		if (filterDoubleSpace.contains(code))
			label = label.replaceAll("  ", " ");

		return super.createOpsEntry(code, label);
	}
}

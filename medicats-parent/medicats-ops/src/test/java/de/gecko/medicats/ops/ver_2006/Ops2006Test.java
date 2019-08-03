package de.gecko.medicats.ops.ver_2006;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2006Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2006";
	}

	@Override
	protected Charset getCharset()
	{
		return Charset.forName("Cp1252");
	}

	@Override
	protected OpsEntry createOpsEntry(String code, String label)
	{
		List<String> filterDoubleSpace = Arrays.asList("5-028.00", "5-039.3", "5-039.32", "5-039.33", "5-039.34",
				"5-039.35", "5-039.36", "5-039.37", "5-339.03", "8-011.1", "8-544", "8-544.0", "8-544.1", "8-559.30",
				"8-559.40", "8-559.50", "8-559.60", "8-559.70", "8-559.80", "8-91a.0", "9-403.2");

		if (filterDoubleSpace.contains(code))
			label = label.replaceAll("  ", " ");

		return super.createOpsEntry(code, label);
	}
}

package de.gecko.medicats.ops.ver_2007;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2007Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2007";
	}

	@Override
	protected Charset getCharset()
	{
		return Charset.forName("Cp1252");
	}

	@Override
	protected OpsEntry createOpsEntry(String code, String label)
	{
		List<String> filterDoubleSpace = Arrays.asList("5-039.3", "5-039.32", "5-039.33", "5-039.34", "5-039.35",
				"5-339.03", "5-371", "5-371.3", "5-371.30", "5-371.31", "5-371.32", "5-371.33", "5-371.34", "5-371.35",
				"5-371.36", "5-371.3x", "5-371.4", "5-371.40", "5-371.41", "5-371.42", "5-371.43", "5-371.44",
				"5-371.45", "5-371.46", "5-371.4x", "5-371.5", "5-371.50", "5-371.51", "5-371.52", "5-371.53",
				"5-371.54", "5-371.55", "5-371.56", "5-371.5x", "5-371.x", "5-371.y", "8-011.1", "8-544", "8-544.0",
				"8-544.1", "8-559.30", "8-559.40", "8-559.50", "8-559.60", "8-559.70", "8-559.80", "8-83b.61",
				"8-853.75", "8-853.85", "8-854.65", "8-854.75", "8-855.75", "8-855.85", "8-91a.0", "9-403.2");

		if (filterDoubleSpace.contains(code))
			label = label.replaceAll("  ", " ");

		return super.createOpsEntry(code, label);
	}
}

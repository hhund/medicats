package de.gecko.medicats.ops.ver_2005;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops2005Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops2005";
	}

	@Override
	protected Charset getCharset()
	{
		return Charset.forName("Cp1252");
	}

	@Override
	protected OpsEntry createOpsEntry(String code, String label)
	{
		if (code.equals("8-800.7f"))
			return null;

		List<String> filterDoubleSpace = Arrays.asList("5-039.3", "5-039.32", "5-039.33", "5-039.34", "5-039.35",
				"5-339.03", "8-544", "8-544.0", "8-544.1", "9-403.2");

		if (filterDoubleSpace.contains(code))
			label = label.replaceAll("  ", " ");

		return super.createOpsEntry(code, label);
	}
}

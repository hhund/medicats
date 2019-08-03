package de.gecko.medicats.ops.ver_21;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops21Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops21";
	}

	@Override
	protected Charset getCharset()
	{
		return Charset.forName("Cp1252");
	}

	@Override
	protected void parseRow(List<OpsEntry> codes, CSVRecord record)
	{
		String code = record.get(9);
		String label = record.get(10);

		if (code != null && !code.equals("UNDEF") && !code.equals("None") && !code.equals("KOMBI") && label != null
				&& !label.equals("Undefined") && !label.equals("Nicht definiert")
				&& !label.equals("Kombinationsschlüsselnummer erforderlich"))
		{
			OpsEntry o = createOpsEntry(code, label);
			if (o != null)
				codes.add(o);
		}
	}
}

package de.gecko.medicats.icd10.ver_gm2005;

import java.io.InputStream;
import java.util.List;

import de.gecko.medicats.icd10.AbstractIcd10GMTest;

public class Icd10GM2005Test extends AbstractIcd10GMTest
{
	@Override
	protected String getVersion()
	{
		return "icd10gm2005";
	}

	@Override
	protected List<String> readCodes(InputStream systFileInputstream)
	{
		List<String> codes = super.readCodes(systFileInputstream);

		int i = codes.indexOf("B34.9");

		codes.add(i + 1, "U04");
		codes.add(i + 2, "U04.0");
		codes.add(i + 3, "U04.1");
		codes.add(i + 4, "U04.2");
		codes.add(i + 5, "U04.3");
		codes.add(i + 6, "U04.4");
		codes.add(i + 7, "U04.5");
		codes.add(i + 8, "U04.6");
		codes.add(i + 9, "U04.7");
		codes.add(i + 10, "U04.8");
		codes.add(i + 11, "U04.9");

		return codes;
	}
}

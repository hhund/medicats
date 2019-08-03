package de.gecko.medicats.icd10.ver_sgbv20;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import de.gecko.medicats.icd10.AbstractIcd10GMTest;

public class Icd10SGBV20Test extends AbstractIcd10GMTest
{
	@Override
	protected String getVersion()
	{
		return "icd10sgbv20";
	}

	@Override
	protected List<String> readCodes(InputStream systFileInputstream)
	{
		List<String> codes = super.readCodes(systFileInputstream);

		return codes.stream().map(this::fixCode).collect(Collectors.toList());
	}

	private String fixCode(String code)
	{
		if (code.endsWith(".-!") || code.endsWith(".-*"))
			return code.substring(0, code.length() - 3);
		if (code.endsWith(".-"))
			return code.substring(0, code.length() - 2);
		else if (code.endsWith("-") || code.endsWith("!") || code.endsWith("*"))
			return code.substring(0, code.length() - 1);
		else
			return code;
	}
}

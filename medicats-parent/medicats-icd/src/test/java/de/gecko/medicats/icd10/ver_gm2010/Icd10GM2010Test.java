package de.gecko.medicats.icd10.ver_gm2010;

import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.List;

import de.gecko.medicats.icd10.AbstractIcd10GMTest;

public class Icd10GM2010Test extends AbstractIcd10GMTest
{
	@Override
	protected String getVersion()
	{
		return "icd10gm2010";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "icd10gmsyst2010.txt");
	}

	@Override
	protected List<String> readCodes(InputStream systFileInputstream)
	{
		List<String> codes = super.readCodes(systFileInputstream);

		int i = codes.indexOf("O43.1");

		codes.add(i + 1, "O43.2");

		return codes;
	}
}

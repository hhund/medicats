package de.gecko.medicats.icd10.ver_gm2014;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.icd10.AbstractIcd10GMTest;

public class Icd10GM2014Test extends AbstractIcd10GMTest
{
	@Override
	protected String getVersion()
	{
		return "icd10gm2014";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "icd10gm2014syst.txt");
	}
}

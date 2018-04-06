package de.gecko.medicats.icd10.ver_gm2018;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.icd10.AbstractIcd10GMTest;

public class Icd10GM2018Test extends AbstractIcd10GMTest
{
	@Override
	protected String getVersion()
	{
		return "icd10gm2018";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "icd10gm2018syst.txt");
	}
}

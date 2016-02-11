package de.gecko.medicats.icd10.ver_gm2007;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.icd10.AbstractIcd10GMTest;

public class Icd10GM2007Test extends AbstractIcd10GMTest
{
	@Override
	protected String getVersion()
	{
		return "icd10gm2007";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "ICD10V2007.txt");
	}
}

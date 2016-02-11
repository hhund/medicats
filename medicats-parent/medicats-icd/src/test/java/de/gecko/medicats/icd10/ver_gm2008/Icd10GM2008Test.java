package de.gecko.medicats.icd10.ver_gm2008;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.icd10.AbstractIcd10GMTest;

public class Icd10GM2008Test extends AbstractIcd10GMTest
{
	@Override
	protected String getVersion()
	{
		return "icd10gm2008";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", "icd10v2008.txt");
	}
}

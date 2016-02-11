package de.gecko.medicats.icd10.ver_gm2006;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.icd10.AbstractIcd10GMTest;

public class Icd10GM2006Test extends AbstractIcd10GMTest
{
	@Override
	protected String getVersion()
	{
		return "icd10gm2006";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("ICD10V2006.txt");
	}
}

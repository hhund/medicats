package de.gecko.medicats.icd10.ver_sgbv13;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.gecko.medicats.icd10.AbstractIcd10GMTest;
import de.gecko.medicats.icd10.AbstractIcdNodeFactory;

public class Icd10SGBV13Test extends AbstractIcd10GMTest
{
	@Override
	protected String getVersion()
	{
		return "icd10sgbv13";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("icd10v13.txt");
	}

	@Override
	protected FileSystem getTransitionZip(AbstractIcdNodeFactory f)
	{
		Path basePath = Paths.get(System.getProperty(AbstractIcdNodeFactory.DIMDI_FILES_BASE_PATH_PARAM,
				AbstractIcdNodeFactory.DIMDI_FILES_BASE_PATH_DEFAULT_VALUE));

		Path zip = basePath.resolve("x1ueb13_20_v11.zip");

		try
		{
			return FileSystems.newFileSystem(zip, null);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	protected List<String> readCodes(InputStream systFileInputstream)
	{
		List<String> codes = super.readCodes(systFileInputstream);

		codes = codes.stream().map(this::fixCode).collect(Collectors.toList());

		List<String> toRemove = Arrays.asList("A00.0", "A00.1", "A00.9", "A20.0", "A20.1", "A20.2", "A20.3", "A20.7",
				"A20.8", "A20.9", "A30.0", "A30.1", "A30.2", "A30.3", "A30.4", "A30.5", "A30.8", "A30.9", "A44.0",
				"A44.1", "A44.8", "A44.9", "A66.0", "A66.1", "A66.2", "A66.3", "A66.4", "A66.5", "A66.6", "A66.7",
				"A66.8", "A66.9", "A67.0", "A67.1", "A67.2", "A67.3", "A67.9", "A68.0", "A68.1", "A68.9", "A71.0",
				"A71.1", "A71.9", "A77.0", "A77.1", "A77.2", "A77.3", "A77.8", "A77.9", "A83.0", "A83.1", "A83.2",
				"A83.3", "A83.4", "A83.5", "A83.6", "A83.8", "A83.9", "A92.0", "A92.1", "A92.2", "A92.3", "A92.4",
				"A92.8", "A92.9", "A93.0", "A93.1", "A93.2", "A93.8", "A95.0", "A95.1", "A95.9", "A96.0", "A96.1",
				"A96.2", "A96.8", "A96.9", "A98.0", "A98.1", "A98.2", "A98.3", "A98.4", "A98.5", "A98.8", "B38.0",
				"B38.1", "B38.2", "B38.3", "B38.4", "B38.7", "B38.8", "B38.9", "B39.0", "B39.1", "B39.2", "B39.3",
				"B39.4", "B39.5", "B39.9", "B41.0", "B41.7", "B41.8", "B41.9", "B43.0", "B43.1", "B43.2", "B43.8",
				"B43.9", "B47.0", "B47.1", "B47.9", "B55.0", "B55.1", "B55.2", "B55.9", "B56.0", "B56.1", "B56.9",
				"B57.0", "B57.1", "B57.2", "B57.3", "B57.4", "B57.5", "B65.0", "B65.1", "B65.2", "B65.3", "B65.8",
				"B65.9", "B74.0", "B74.1", "B74.2", "B74.3", "B74.4", "B74.8", "B74.9", "B76.0", "B76.1", "B76.8",
				"B76.9", "B87.0", "B87.1", "B87.2", "B87.3", "B87.4", "B87.8", "B87.9", "E12.0", "E12.1", "E12.2",
				"E12.3", "E12.4", "E12.5", "E12.6", "E12.7", "E12.8", "E12.9", "T08.x0", "T08.x1", "T10.x0", "T10.x1",
				"T12.x0", "T12.x1");

		codes.removeAll(toRemove);

		int i = codes.indexOf("Z73");

		codes.add(i + 1, "Z76");
		
		i = codes.indexOf("Z90.8");

		codes.add(i + 1, "Z92");

		return codes;
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

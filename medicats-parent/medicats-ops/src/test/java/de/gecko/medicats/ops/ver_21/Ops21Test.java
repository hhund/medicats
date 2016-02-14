package de.gecko.medicats.ops.ver_21;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import de.gecko.medicats.ops.AbstractOpsNodeFactory;
import de.gecko.medicats.ops.AbstractOpsTest;

public class Ops21Test extends AbstractOpsTest
{
	@Override
	protected String getVersion()
	{
		return "ops21";
	}

	@Override
	protected Path getSystFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("codes.txt");
	}

	@Override
	protected Charset getCharset()
	{
		return Charset.forName("Cp1252");
	}

	@Override
	protected FileSystem getTransitionZip(AbstractOpsNodeFactory f)
	{
		Path basePath = Paths.get(System.getProperty(AbstractOpsNodeFactory.DIMDI_FILES_BASE_PATH_PARAM,
				AbstractOpsNodeFactory.DIMDI_FILES_BASE_PATH_DEFAULT_VALUE));

		Path zip = basePath.resolve("p1ema2_1.zip");

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

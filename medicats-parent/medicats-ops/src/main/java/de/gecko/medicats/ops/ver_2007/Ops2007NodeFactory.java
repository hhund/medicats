package de.gecko.medicats.ops.ver_2007;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;

public class Ops2007NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private static final String SGML_RESOURCE_FILENAME = "OP301.SGM";
	private static final String UMSTEIGER_RESOURCE_FILENAME = "UmsteigerErweitert.txt";
	private static final String PREVIOUS_VERSION = "ops2006";
	private static final String VERSION = "ops2007";

	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public int getSortIndex()
	{
		return 2007;
	}

	@Override
	protected String getSgmlFileName()
	{
		return SGML_RESOURCE_FILENAME;
	}

	@Override
	public String getPreviousVersion()
	{
		return PREVIOUS_VERSION;
	}

	@Override
	protected String getPreviousCodesFileName()
	{
		return UMSTEIGER_RESOURCE_FILENAME;
	}

	@Override
	protected int getPreviousCodesCurrentColumn()
	{
		return 3;
	}

	@Override
	public OpsNodeWalker createNodeWalker()
	{
		return new Ops2007NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("p1ees2007.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 1350522911L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("p1ueberw2006_2007.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 485680026L;
	}

	@Override
	protected Path getSgmlFileNamePath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath("Klassifikationsdateien", getSgmlFileName());
	}

	@Override
	protected Path getTransitionFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", getPreviousCodesFileName());
	}
}

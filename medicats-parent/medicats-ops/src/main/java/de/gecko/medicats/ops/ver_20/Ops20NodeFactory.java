package de.gecko.medicats.ops.ver_20;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;

public class Ops20NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private static final String SGML_RESOURCE_FILENAME = "OP301.sgm";
	private static final String PREVIOUS_VERSION = "ops11";
	private static final String VERSION = "ops20";

	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public int getSortIndex()
	{
		return 20;
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
		return null;
	}

	@Override
	public OpsNodeWalker createNodeWalker()
	{
		return new Ops20NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("p1ses2_0.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 543381695L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return null; // No previous version
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return Long.MIN_VALUE; // No previous version
	}

	@Override
	protected Path getSgmlFileNamePath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath(getSgmlFileName());
	}

	@Override
	protected Path getTransitionFilePath(FileSystem transitionZip)
	{
		return null; // No previous version
	}
}

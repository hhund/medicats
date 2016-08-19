package de.gecko.medicats.ops.ver_2005;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;

public class Ops2005NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private static final String SGML_RESOURCE_FILENAME = "OP301.SGM";
	private static final String UMSTEIGER_RESOURCE_FILENAME = "UmsteigerErweitert.txt";
	private static final String PREVIOUS_VERSION = "ops2004";
	private static final String VERSION = "ops2005";

	@Override
	public String getName()
	{
		return "OPS 2005";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.303";
	}

	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public int getSortIndex()
	{
		return 2005;
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
	protected int getCurrentCodesBackwardsCompatibleColumn()
	{
		return Integer.MIN_VALUE;
	}

	@Override
	public OpsNodeWalker createNodeWalker()
	{
		return new Ops2005NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("p1ees2005.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 2635442911L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("p1ueberw2004_2005_v10.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 2495506828L;
	}

	@Override
	protected Path getSgmlFileNamePath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath(getSgmlFileName());
	}

	@Override
	protected Path getTransitionFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath(getPreviousCodesFileName());
	}
}

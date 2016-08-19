package de.gecko.medicats.ops.ver_2009;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;

public class Ops2009NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private static final String SGML_RESOURCE_FILENAME = "OP301.SGM";
	private static final String UMSTEIGER_RESOURCE_FILENAME = "umsteigeramtl20082009.txt";
	private static final String PREVIOUS_VERSION = "ops2008";
	private static final String VERSION = "ops2009";

	@Override
	public String getName()
	{
		return "OPS 2009";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.357";
	}

	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public int getSortIndex()
	{
		return 2009;
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
	protected int getCurrentCodesColumn()
	{
		return 1;
	}

	@Override
	protected int getCurrentCodesBackwardsCompatibleColumn()
	{
		return 5;
	}

	@Override
	protected int getPreviousCodesForwardsCompatibleColumn()
	{
		return 4;
	}

	@Override
	public OpsNodeWalker createNodeWalker()
	{
		return new Ops2009NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("p1ses2009.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 1141457111L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("p1ueb2008_2009.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 4122055693L;
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

package de.gecko.medicats.ops.ver_2008;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;

public class Ops2008NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private static final String SGML_RESOURCE_FILENAME = "OP301.SGM";
	private static final String UMSTEIGER_RESOURCE_FILENAME = "umsteigererw20072008.txt";
	private static final String PREVIOUS_VERSION = "ops2007";
	private static final String VERSION = "ops2008";

	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public int getSortIndex()
	{
		return 2008;
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
		return 3;
	}

	@Override
	protected int getPreviousCodesForwardsCompatibleColumn()
	{
		return 6;
	}

	@Override
	protected int getCurrentCodesBackwardsCompatibleColumn()
	{
		return Integer.MIN_VALUE;
	}

	@Override
	public OpsNodeWalker createNodeWalker()
	{
		return new Ops2008NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("p1ees2008.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 3679931325L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("p1ueberw2007_2008.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 1217639680L;
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

package de.gecko.medicats.ops.ver_21;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;

public class Ops21NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private static final String SGML_RESOURCE_FILENAME = "OP301.SGM";
	private static final String UMSTEIGER_RESOURCE_FILENAME = "Umsteiger.txt";
	private static final String PREVIOUS_VERSION = "ops20";
	private static final String VERSION = "ops21";

	@Override
	public String getName()
	{
		return "OPS-301 Version 2.1";
	}

	@Override
	public String getOid()
	{
		return "";
	}

	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public int getSortIndex()
	{
		return 21;
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
	protected int getPreviousCodesForwardsCompatibleColumn()
	{
		return 2;
	}

	@Override
	protected int getCurrentCodesBackwardsCompatibleColumn()
	{
		return 3;
	}

	@Override
	public OpsNodeWalker createNodeWalker()
	{
		return new Ops21NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("p1ees2_1.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 2016043895L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("p1ueb20_21_v10.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 559765520L;
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

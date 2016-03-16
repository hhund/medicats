package de.gecko.medicats.ops.ver_2006;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;

public class Ops2006NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private static final String SGML_RESOURCE_FILENAME = "OP301.SGM";
	private static final String UMSTEIGER_RESOURCE_FILENAME = "UmsteigerErweitert.txt";
	private static final String PREVIOUS_VERSION = "ops2005";
	private static final String VERSION = "ops2006";

	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public int getSortIndex()
	{
		return 2006;
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
		return new Ops2006NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("p1ees2006.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 1258873145L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("p1ueberw2005_2006.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 1278310663L;
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

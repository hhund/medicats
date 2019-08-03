package de.gecko.medicats.ops.ver_2006;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;

public class Ops2006NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "ops2006.zip", 2033774077L);

	private final FileSource sgml = new FileSource(zip, "ops2006erw", "p1ees2006", "OP301.SGM");
	private FileSource transitionFile = new FileSource(zip, "ops2006erw", "p1ueberw2005_2006",
			"UmsteigerErweitert.txt");
	private FileSource systFile = new FileSource(zip, "ops2006erw", "p1ueberw2005_2006", "OPS2006.txt");

	@Override
	public String getName()
	{
		return "OPS 2006";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.310";
	}

	@Override
	public String getPreviousVersion()
	{
		return "ops2005";
	}

	@Override
	public String getVersion()
	{
		return "ops2006";
	}

	@Override
	public int getSortIndex()
	{
		return 2006;
	}

	@Override
	protected FileSource getSgml()
	{
		return sgml;
	}

	@Override
	protected FileSource getTransitionFile()
	{
		return transitionFile;
	}

	@Override
	protected FileSource getSystFile()
	{
		return systFile;
	}

	@Override
	public OpsNodeWalker createNodeWalker()
	{
		return new Ops2006NodeWalker(getRootNode());
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
}

package de.gecko.medicats.ops.ver_21;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;

public class Ops21NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "ops21.zip", 2988462901L);

	private final FileSource sgml = new FileSource(zip, "ops21erw", "p1ees2_1", "OP301.SGM");
	private FileSource transitionFile = new FileSource(zip, "ops21erw", "p1ueb20_21_v10", "Umsteiger.txt");
	private FileSource systFile = new FileSource(zip, "ops21erw", "p1ema2_1", "codes.txt");

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
	public String getPreviousVersion()
	{
		return "ops20";
	}

	@Override
	public String getVersion()
	{
		return "ops21";
	}

	@Override
	public int getSortIndex()
	{
		return 21;
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
		return new Ops21NodeWalker(getRootNode());
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
}

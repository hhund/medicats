package de.gecko.medicats.ops.ver_2005;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;

public class Ops2005NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "ops2005.zip", 3343551789L);

	private final FileSource sgml = new FileSource(zip, "ops2005erw", "p1ees2005", "OP301.SGM");
	private FileSource transitionFile = new FileSource(zip, "ops2005erw", "p1ueberw2004_2005_v10",
			"UmsteigerErweitert.txt");
	private FileSource systFile = new FileSource(zip, "ops2005erw", "p1ueberw2004_2005_v10", "OPS2005.txt");

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
	public String getPreviousVersion()
	{
		return "ops2004";
	}

	@Override
	public String getVersion()
	{
		return "ops2005";
	}

	@Override
	public int getSortIndex()
	{
		return 2005;
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
		return new Ops2005NodeWalker(getRootNode());
	}

	@Override
	protected int getCurrentCodesBackwardsCompatibleColumn()
	{
		return Integer.MIN_VALUE;
	}
}

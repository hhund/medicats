package de.gecko.medicats.ops.ver_20;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;

public class Ops20NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "ops20.zip", 4097034323L);

	private final FileSource sgml = new FileSource(zip, "p1ses2_0", "OP301.sgm");
	private FileSource systFile = new FileSource(zip, "p1ueb11_20_v11", "Opsv20.txt");

	@Override
	public String getName()
	{
		return "OPS-301 Version 2.0";
	}

	@Override
	public String getOid()
	{
		return "";
	}

	@Override
	public String getPreviousVersion()
	{
		return "ops11";
	}

	@Override
	public String getVersion()
	{
		return "ops20";
	}

	@Override
	public int getSortIndex()
	{
		return 20;
	}

	@Override
	protected FileSource getSgml()
	{
		return sgml;
	}

	@Override
	protected FileSource getTransitionFile()
	{
		return null;
	}

	@Override
	protected FileSource getSystFile()
	{
		return systFile;
	}

	@Override
	public OpsNodeWalker createNodeWalker()
	{
		return new Ops20NodeWalker(getRootNode());
	}
}

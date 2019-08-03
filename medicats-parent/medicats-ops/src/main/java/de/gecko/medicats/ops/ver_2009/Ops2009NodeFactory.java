package de.gecko.medicats.ops.ver_2009;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;

public class Ops2009NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "ops2009.zip", 922375794L);

	private final FileSource sgml = new FileSource(zip, "p1ses2009", "Klassifikationsdateien", "OP301.SGM");
	private FileSource transitionFile = new FileSource(zip, "p1ueb2008_2009", "Klassifikationsdateien",
			"umsteigeramtl20082009.txt");
	private FileSource systFile = new FileSource(zip, "p1ueb2008_2009", "Klassifikationsdateien", "opsamtl2009.txt");

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
	public String getPreviousVersion()
	{
		return "ops2008";
	}

	@Override
	public String getVersion()
	{
		return "ops2009";
	}

	@Override
	public int getSortIndex()
	{
		return 2009;
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
		return new Ops2009NodeWalker(getRootNode());
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
}

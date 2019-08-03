package de.gecko.medicats.ops.ver_2008;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;

public class Ops2008NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "ops2008.zip", 1640621382L);

	private final FileSource sgml = new FileSource(zip, "ops2008erw", "p1ees2008", "Klassifikationsdateien",
			"OP301.SGM");
	private FileSource transitionFile = new FileSource(zip, "ops2008erw", "p1ueberw2007_2008", "Klassifikationsdateien",
			"umsteigererw20072008.txt");
	private FileSource systFile = new FileSource(zip, "ops2008erw", "p1ueberw2007_2008", "Klassifikationsdateien",
			"opserw2008.txt");

	@Override
	public String getName()
	{
		return "OPS 2008";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.331";
	}

	@Override
	public String getPreviousVersion()
	{
		return "ops2007";
	}

	@Override
	public String getVersion()
	{
		return "ops2008";
	}

	@Override
	public int getSortIndex()
	{
		return 2008;
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
		return new Ops2008NodeWalker(getRootNode());
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

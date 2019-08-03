package de.gecko.medicats.ops.ver_2004;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;
import de.gecko.medicats.ops.sgml.SgmlOpsNode;

public class Ops2004NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "ops2004.zip", 309357916L);

	private final FileSource sgml = new FileSource(zip, "ops2004erw", "p1ees2004", "OP301.SGM");
	private FileSource transitionFile = new FileSource(zip, "ops2004amtl", "p1ueb21_2004_v10", "Umsteiger.txt");
	private FileSource systFile = new FileSource(zip, "ops2004amtl", "p1ueb21_2004_v10", "opsv2004.txt");

	@Override
	public String getName()
	{
		return "OPS 2004";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.301";
	}

	@Override
	public String getPreviousVersion()
	{
		return "ops21";
	}

	@Override
	public String getVersion()
	{
		return "ops2004";
	}

	@Override
	public int getSortIndex()
	{
		return 2004;
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
		return new Ops2004NodeWalker(getRootNode());
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
	protected String fixFstLabel(String label, String code, SgmlOpsNode parent)
	{
		if ("8-810".equals(parent.getCode()) && "8-810.c".equals(code))
			return "Transfusion von Plasma und Plasmabestandteilen und gentechnisch hergestellten Plasmaproteinen: Prothrombinkomplex mit Faktor-VIII-Inhibitor-Bypass-Aktivität [Feiba]";
		else
			return super.fixFstLabel(label, code, parent);
	}

	@Override
	protected String fixSstLabel(String label, String code, SgmlOpsNode parent)
	{
		if ("8-810.c".equals(parent.getCode()))
		{
			if ("8-810.c0".equals(code))
				return "Transfusion von Plasma und Plasmabestandteilen und gentechnisch hergestellten Plasmaproteinen: Prothrombinkomplex mit Faktor-VIII-Inhibitor-Bypass-Aktivität [Feiba]: Bis 2000 Einheiten";
			else if ("8-810.c1".equals(code))
				return "Transfusion von Plasma und Plasmabestandteilen und gentechnisch hergestellten Plasmaproteinen: Prothrombinkomplex mit Faktor-VIII-Inhibitor-Bypass-Aktivität [Feiba]: 2001 bis 5000 Einheiten";
			else if ("8-810.c2".equals(code))
				return "Transfusion von Plasma und Plasmabestandteilen und gentechnisch hergestellten Plasmaproteinen: Prothrombinkomplex mit Faktor-VIII-Inhibitor-Bypass-Aktivität [Feiba]: Mehr als 5000 Einheiten";
		}

		return super.fixSstLabel(label, code, parent);
	}
}

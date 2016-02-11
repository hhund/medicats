package de.gecko.medicats.ops.ver_2004;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.sgml.AbstractSgmlOpsNodeFactory;
import de.gecko.medicats.ops.sgml.SgmlOpsNode;

public class Ops2004NodeFactory extends AbstractSgmlOpsNodeFactory implements OpsNodeFactory
{
	private static final String SGML_RESOURCE_FILENAME = "OP301.SGM";
	private static final String UMSTEIGER_RESOURCE_FILENAME = "umsteiger.txt";
	private static final String PREVIOUS_VERSION = "ops21";
	private static final String VERSION = "ops2004";

	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public int getSortIndex()
	{
		return 2004;
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
	protected int getPreviousCodesCurrentColumn()
	{
		return 1;
	}

	@Override
	public OpsNodeWalker createNodeWalker()
	{
		return new Ops2004NodeWalker(getRootNode());
	}

	@Override
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("p1ees2004.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 973286594L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("p1ueb2004_2005_v10.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 3283081517L;
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

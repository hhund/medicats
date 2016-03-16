package de.gecko.medicats.ops.sgml;

import de.gecko.medicats.PreviousCodeMappings;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.OpsService;

public class SgmlOpsNodeRoot extends SgmlOpsNode
{
	private final String version;
	private final PreviousCodeMappings mappings;
	private final String previousVersion;

	private OpsNodeWalker previousNodeWalker;
	private boolean previouseCodeSupported = true;

	public SgmlOpsNodeRoot(String version, PreviousCodeMappings mappings, String previousVersion)
	{
		super(null, null, "ROOT", "ROOT", OpsNodeType.ROOT, null, null);

		this.version = version;
		this.mappings = mappings;
		this.previousVersion = previousVersion;
	}

	@Override
	public int getHierarchyDepth()
	{
		return 0;
	}

	@Override
	protected PreviousCodeMappings getPreviousCodes()
	{
		return mappings;
	}

	@Override
	public String getPreviousVersion()
	{
		return previousVersion;
	}

	@Override
	protected OpsNodeWalker getPreviousNodeWalker()
	{
		if (previousNodeWalker == null && previouseCodeSupported)
		{
			OpsService icdService = OpsService.getService();
			if (icdService.supportsVersion(getPreviousVersion()))
			{
				OpsNodeFactory previousNodeFactory = icdService.getNodeFactory(getPreviousVersion());
				previousNodeWalker = previousNodeFactory.createNodeWalker();
			}
			else
				previouseCodeSupported = false;
		}

		return previousNodeWalker;
	}

	@Override
	public String getVersion()
	{
		return version;
	}

	@Override
	public String toString()
	{
		return getLabel() + " " + getVersion();
	}

	@Override
	public String getPath()
	{
		return getVersion();
	}

	@Override
	public boolean isAmtl()
	{
		return false;
	}
}

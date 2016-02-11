package de.gecko.medicats.icd10.sgml;

import java.util.Map;

import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.IcdService;

public class SgmlIcdNodeRoot extends SgmlIcdNode
{
	private final String version;
	private final int sortIndex;
	private final Map<String, String> previousCodes;
	private final String previousVersion;

	private IcdNodeWalker previousNodeWalker;
	private boolean previouseCodeSupported = true;

	public SgmlIcdNodeRoot(String version, int sortIndex, Map<String, String> previousCodes, String previousVersion)
	{
		super(null, null, "ROOT", "ROOT", IcdNodeType.ROOT, IcdNodeUsage.UNDEFINED, null, null);

		this.version = version;
		this.sortIndex = sortIndex;
		this.previousCodes = previousCodes;
		this.previousVersion = previousVersion;
	}

	@Override
	public int getHierarchyDepth()
	{
		return 0;
	}

	@Override
	protected Map<String, String> getPreviousCodes()
	{
		return previousCodes;
	}

	@Override
	public String getPreviousVersion()
	{
		return previousVersion;
	}

	@Override
	protected IcdNodeWalker getPreviousNodeWalker()
	{
		if (previousNodeWalker == null && previouseCodeSupported)
		{
			IcdService icdService = IcdService.getService();
			if (icdService.supportsVersion(getPreviousVersion()))
			{
				IcdNodeFactory previousNodeFactory = icdService.getNodeFactory(getPreviousVersion());
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
	public int getSortIndex()
	{
		return sortIndex;
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
}

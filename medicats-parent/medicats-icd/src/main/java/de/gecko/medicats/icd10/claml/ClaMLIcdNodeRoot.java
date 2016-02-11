package de.gecko.medicats.icd10.claml;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.gecko.medicats.claml.ClaML;
import de.gecko.medicats.claml.ClaMLClassKind;
import de.gecko.medicats.claml.RubricKind;
import de.gecko.medicats.claml.UsageKind;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.IcdService;

final class ClaMLIcdNodeRoot extends ClaMLIcdNode
{
	private final ClaML claML;
	private final String version;
	private final int sortIndex;
	private final Map<String, String> previousCodes;
	private final String previousVersion;

	private Map<String, UsageKind> usageKinds;
	private Map<String, RubricKind> rubricKinds;
	private Map<String, ClaMLClassKind> icdClassKinds;
	private IcdNodeWalker previousNodeWalker;
	private boolean previouseCodeSupported = true;

	ClaMLIcdNodeRoot(ClaML claML, String version, int sortIndex, Map<String, String> previousCodes,
			String previousVersion)
	{
		super(null, null, null, null);

		this.claML = claML;
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
	protected Map<String, ClaMLClassKind> getClamlClassKinds()
	{
		if (icdClassKinds == null)
			icdClassKinds = claML.getClaMLClassKinds().getClaMLClassKinds().stream()
					.collect(Collectors.toMap(k -> k.getName(), Function.identity()));

		return icdClassKinds;
	}

	@Override
	protected Map<String, RubricKind> getRubricKinds()
	{
		if (rubricKinds == null)
			rubricKinds = claML.getRubricKinds().getRubricKinds().stream()
					.collect(Collectors.toMap(k -> k.getName(), Function.identity()));

		return rubricKinds;
	}

	@Override
	protected Map<String, UsageKind> getUsageKinds()
	{
		if (usageKinds == null)
			usageKinds = claML.getUsageKinds().getUsageKinds().stream()
					.collect(Collectors.toMap(k -> k.getName(), Function.identity()));

		return usageKinds;
	}

	@Override
	protected Map<String, String> getPreviousCodes()
	{
		return previousCodes;
	}

	@Override
	public String getLabel()
	{
		return "ROOT";
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
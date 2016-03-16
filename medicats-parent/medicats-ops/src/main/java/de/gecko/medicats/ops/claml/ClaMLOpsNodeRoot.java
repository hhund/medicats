package de.gecko.medicats.ops.claml;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.gecko.medicats.PreviousCodeMappings;
import de.gecko.medicats.claml.ClaML;
import de.gecko.medicats.claml.ClaMLClassKind;
import de.gecko.medicats.claml.RubricKind;
import de.gecko.medicats.claml.UsageKind;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.OpsService;

public class ClaMLOpsNodeRoot extends ClaMLOpsNode
{
	private final ClaML claML;
	private final String version;
	private final PreviousCodeMappings mappings;
	private final String previousVersion;

	private Map<String, UsageKind> usageKinds;
	private Map<String, RubricKind> rubricKinds;
	private Map<String, ClaMLClassKind> icdClassKinds;
	private OpsNodeWalker previousNodeWalker;
	private boolean previouseCodeSupported = true;

	ClaMLOpsNodeRoot(ClaML claML, String version, PreviousCodeMappings mappings, String previousVersion)
	{
		super(null, null, null, null);

		this.claML = claML;
		this.version = version;
		this.mappings = mappings;
		this.previousVersion = previousVersion;
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
	protected PreviousCodeMappings getPreviousCodes()
	{
		return mappings;
	}

	@Override
	protected OpsNodeWalker getPreviousNodeWalker()
	{
		if (previousNodeWalker == null && previouseCodeSupported)
		{
			OpsService opsService = OpsService.getService();
			if (opsService.supportsVersion(getPreviousVersion()))
			{
				OpsNodeFactory previousNodeFactory = opsService.getNodeFactory(getPreviousVersion());
				previousNodeWalker = previousNodeFactory.createNodeWalker();
			}
			else
				previouseCodeSupported = false;
		}

		return previousNodeWalker;
	}
}

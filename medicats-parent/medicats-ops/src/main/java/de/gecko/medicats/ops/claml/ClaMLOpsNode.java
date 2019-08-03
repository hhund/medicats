package de.gecko.medicats.ops.claml;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.gecko.medicats.claml.ClamlClass;
import de.gecko.medicats.claml.ClaMLClassKind;
import de.gecko.medicats.claml.ModifierClass;
import de.gecko.medicats.claml.Reference;
import de.gecko.medicats.claml.Rubric;
import de.gecko.medicats.claml.RubricKind;
import de.gecko.medicats.claml.Term;
import de.gecko.medicats.claml.UsageKind;
import de.gecko.medicats.ops.OpsNode;

public class ClaMLOpsNode extends OpsNode
{
	/**
	 * @param parent
	 *            not <code>null</code>
	 * @param clamlClass
	 *            not <code>null</code>
	 * @return
	 */
	public static ClaMLOpsNode createNode(ClaMLOpsNode parent, ClamlClass clamlClass)
	{
		return createNodeAndAddToParent(Objects.requireNonNull(parent), Objects.requireNonNull(clamlClass), null, null);
	}

	/**
	 * @param parent
	 *            not <code>null</code>
	 * @param clamlClass
	 *            not <code>null</code>
	 * @param primaryModifier
	 *            not <code>null</code>
	 * @return
	 */
	public static ClaMLOpsNode createNode(ClaMLOpsNode parent, ClamlClass clamlClass, ModifierClass primaryModifier)
	{
		return createNodeAndAddToParent(Objects.requireNonNull(parent), Objects.requireNonNull(clamlClass),
				Objects.requireNonNull(primaryModifier), null);
	}

	/**
	 * @param parent
	 *            not <code>null</code>
	 * @param clamlClass
	 *            not <code>null</code>
	 * @param primaryModifier
	 *            not <code>null</code>
	 * @param secondaryModifier
	 *            not <code>null</code>
	 * @return
	 */
	public static ClaMLOpsNode createNode(ClaMLOpsNode parent, ClamlClass clamlClass, ModifierClass primaryModifier,
			ModifierClass secondaryModifier)
	{
		return createNodeAndAddToParent(Objects.requireNonNull(parent), Objects.requireNonNull(clamlClass),
				Objects.requireNonNull(primaryModifier), Objects.requireNonNull(secondaryModifier));
	}

	private static ClaMLOpsNode createNodeAndAddToParent(ClaMLOpsNode parent, ClamlClass clamlClass,
			ModifierClass primaryModifier, ModifierClass secondaryModifier)
	{
		ClaMLOpsNode node = new ClaMLOpsNode(parent, clamlClass, primaryModifier, secondaryModifier);
		node.addToParent();
		return node;
	}

	private final ClamlClass clamlClass;
	private final ModifierClass primaryModifier;
	private final ModifierClass secondaryModifier;

	private String code;

	public ClaMLOpsNode(ClaMLOpsNode parent, ClamlClass clamlClass, ModifierClass primaryModifier,
			ModifierClass secondaryModifier)
	{
		super(parent);

		this.clamlClass = clamlClass;
		this.primaryModifier = primaryModifier;
		this.secondaryModifier = secondaryModifier;
	}

	@Override
	public ClaMLOpsNode getParent()
	{
		return (ClaMLOpsNode) super.getParent();
	}

	public ModifierClass getPrimaryModifier()
	{
		return primaryModifier;
	}

	public ModifierClass getSecondaryModifier()
	{
		return secondaryModifier;
	}

	public boolean hasSecondaryModifier()
	{
		return secondaryModifier != null;
	}

	public boolean hasPrimaryModifier()
	{
		return primaryModifier != null;
	}

	public ClamlClass getClamlClass()
	{
		return clamlClass;
	}

	protected Map<String, ClaMLClassKind> getClamlClassKinds()
	{
		return getParent().getClamlClassKinds();
	}

	protected Map<String, RubricKind> getRubricKinds()
	{
		return getParent().getRubricKinds();
	}

	protected Map<String, UsageKind> getUsageKinds()
	{
		return getParent().getUsageKinds();
	}

	@Override
	public String getCode()
	{
		if (code == null)
		{
			if (hasSecondaryModifier())
				code = getClamlClass().getCode() + secondaryModifier.getCode();
			else if (hasPrimaryModifier())
			{
				if (!primaryModifier.getCode().startsWith(".") && !getClamlClass().getCode().contains("."))
					code = getClamlClass().getCode() + "." + primaryModifier.getCode();
				else
					code = getClamlClass().getCode() + primaryModifier.getCode();
			}
			else if (getClamlClass() != null)
				code = getClamlClass().getCode();
			else
				code = "ROOT";
		}

		return code;
	}

	@Override
	public String getLabel()
	{
		RubricKind preferredKind = getRubricKinds().get("preferred");
		RubricKind preferredLongKind = getRubricKinds().get("preferredLong");

		String parentLabel;

		final List<Rubric> preferredRubrics;
		final List<Rubric> preferredLongRubrics;
		if (hasSecondaryModifier())
		{
			parentLabel = getParent().getLabel();

			preferredRubrics = getSecondaryModifier().getRubricsByKind(preferredKind);
			preferredLongRubrics = getSecondaryModifier().getRubricsByKind(preferredLongKind);
		}
		else if (hasPrimaryModifier())
		{
			parentLabel = getParent().getLabel();

			preferredRubrics = getPrimaryModifier().getRubricsByKind(preferredKind);
			preferredLongRubrics = getPrimaryModifier().getRubricsByKind(preferredLongKind);
		}
		else if (getCodeLength() > 5)
		{
			parentLabel = getParent().getLabel();

			preferredRubrics = getClamlClass().getRubricsByKind(preferredKind);
			preferredLongRubrics = getClamlClass().getRubricsByKind(preferredLongKind);
		}
		else
		{
			parentLabel = null;

			preferredRubrics = getClamlClass().getRubricsByKind(preferredKind);
			preferredLongRubrics = getClamlClass().getRubricsByKind(preferredLongKind);
		}

		if (!preferredLongRubrics.isEmpty())
			return (parentLabel != null ? (parentLabel + ": ") : "") + getLabelExpectingOneRubric(preferredLongRubrics);
		else if (!preferredRubrics.isEmpty())
			return (parentLabel != null ? (parentLabel + ": ") : "") + getLabelExpectingOneRubric(preferredRubrics);
		else
			return parentLabel;
	}

	private String getLabelExpectingOneRubric(List<Rubric> rubrics)
	{
		return rubrics.stream().flatMap(r -> r.getLabels().stream()).flatMap(l -> l.getValues().stream()).filter(o ->
		{
			if (o instanceof String || o instanceof Term)
				return true;
			else if (o instanceof Reference)
				return false;
			else
				throw new IllegalStateException("Unexpected type in label elements (" + o.getClass().getName() + ")");
		}).map(o ->
		{
			if (o instanceof String)
				return ((String) o);
			else if (o instanceof Term)
				return ((Term) o).getValue();
			else
				return Objects.toString(o);
		}).collect(Collectors.joining()).trim();
	}

	@Override
	public OpsNodeType getNodeType()
	{
		if (getParent() == null)
			return OpsNodeType.ROOT;
		else if (getClamlClass().getKind().equals(getClamlClassKinds().get(OpsNodeType.CATEGORY.getClassKind())))
			return OpsNodeType.CATEGORY;
		else if (getClamlClass().getKind().equals(getClamlClassKinds().get(OpsNodeType.BLOCK.getClassKind())))
			return OpsNodeType.BLOCK;
		else if (getClamlClass().getKind().equals(getClamlClassKinds().get(OpsNodeType.CHAPTER.getClassKind())))
			return OpsNodeType.CHAPTER;
		else
			throw new IllegalStateException("Unknown node type");
	}

	@Override
	public Stream<OpsNode> getExclusionsImpl(Function<String, List<OpsNode>> byCode)
	{
		if (isRoot())
			return Stream.empty();

		List<Rubric> exclusionRubrics = getClamlClass().getRubricsByKind(getRubricKinds().get("exclusion"));

		return exclusionRubrics.stream().flatMap(r -> r.getLabels().stream())
				.flatMap(l -> l.getReferencesRecursive().stream()).flatMap(r -> byCode.apply(r.getValue()).stream())
				.distinct();
	}

	@Override
	public Stream<OpsNode> getInclusionsImpl(Function<String, List<OpsNode>> byCode)
	{
		if (isRoot())
			return Stream.empty();

		List<Rubric> exclusionRubrics = getClamlClass().getRubricsByKind(getRubricKinds().get("inclusion"));

		return exclusionRubrics.stream().flatMap(r -> r.getLabels().stream())
				.flatMap(l -> l.getReferencesRecursive().stream()).flatMap(r -> byCode.apply(r.getValue()).stream())
				.distinct();
	}
}

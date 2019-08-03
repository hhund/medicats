package de.gecko.medicats.icd10.claml;

import java.util.ArrayList;
import java.util.Collections;
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
import de.gecko.medicats.icd10.IcdNode;

public class ClamlIcdNode extends IcdNode
{
	/**
	 * @param parent
	 *            not <code>null</code>
	 * @param clamlClass
	 *            not <code>null</code>
	 * @return
	 */
	public static ClamlIcdNode createNode(ClamlIcdNode parent, ClamlClass clamlClass)
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
	public static ClamlIcdNode createNode(ClamlIcdNode parent, ClamlClass clamlClass, ModifierClass primaryModifier)
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
	public static ClamlIcdNode createNode(ClamlIcdNode parent, ClamlClass clamlClass, ModifierClass primaryModifier,
			ModifierClass secondaryModifier)
	{
		return createNodeAndAddToParent(Objects.requireNonNull(parent), Objects.requireNonNull(clamlClass),
				Objects.requireNonNull(primaryModifier), Objects.requireNonNull(secondaryModifier));
	}

	private static ClamlIcdNode createNodeAndAddToParent(ClamlIcdNode parent, ClamlClass clamlClass,
			ModifierClass primaryModifier, ModifierClass secondaryModifier)
	{
		ClamlIcdNode node = new ClamlIcdNode(parent, clamlClass, primaryModifier, secondaryModifier);
		node.addToParent();
		return node;
	}

	private final ClamlClass clamlClass;
	private final ModifierClass primaryModifier;
	private final ModifierClass secondaryModifier;

	private String code;

	protected ClamlIcdNode(ClamlIcdNode parent, ClamlClass clamlClass, ModifierClass primaryModifier,
			ModifierClass secondaryModifier)
	{
		super(parent);
		this.clamlClass = clamlClass;
		this.primaryModifier = primaryModifier;
		this.secondaryModifier = secondaryModifier;
	}

	@Override
	public ClamlIcdNode getParent()
	{
		return (ClamlIcdNode) super.getParent();
	}

	@Override
	public IcdNodeType getNodeType()
	{
		if (getParent() == null)
			return IcdNodeType.ROOT;
		else if (getClamlClass().getKind().equals(getClamlClassKinds().get(IcdNodeType.CATEGORY.getClassKind())))
			return IcdNodeType.CATEGORY;
		else if (getClamlClass().getKind().equals(getClamlClassKinds().get(IcdNodeType.BLOCK.getClassKind())))
			return IcdNodeType.BLOCK;
		else if (getClamlClass().getKind().equals(getClamlClassKinds().get(IcdNodeType.CHAPTER.getClassKind())))
			return IcdNodeType.CHAPTER;
		else
			throw new IllegalStateException("Unknown node type");
	}

	@Override
	public IcdNodeUsage getNodeUsage()
	{
		if (getClamlClass() == null)
			return IcdNodeUsage.UNDEFINED;
		else
			return toNodeUsage(getClamlClass().getUsage());
	}

	private IcdNodeUsage toNodeUsage(Object o)
	{
		if (o == null)
			return IcdNodeUsage.UNDEFINED;
		else if (o.equals(getUsageKinds().get(IcdNodeUsage.ASTER.getUsageKind())))
			return IcdNodeUsage.ASTER;
		else if (o.equals(getUsageKinds().get(IcdNodeUsage.DAGGER.getUsageKind())))
			return IcdNodeUsage.DAGGER;
		else if (o.equals(getUsageKinds().get(IcdNodeUsage.OPTIONAL.getUsageKind())))
			return IcdNodeUsage.OPTIONAL;
		else
			throw new IllegalStateException("Unknown usage kind");
	}

	public ClamlClass getClamlClass()
	{
		return clamlClass;
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
				code = getClamlClass().getCode() + primaryModifier.getCode() + secondaryModifier.getCode();
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

	public String getExclusionsString(Function<String, List<IcdNode>> byCode)
	{
		return getExclusions(byCode).map(n -> n.getCode()).collect(Collectors.joining(", ", "[", "]"));
	}

	public List<Reference> getExclusionRefereces()
	{
		if (isRoot())
			return Collections.emptyList();

		List<Rubric> exclusionRubrics = getClamlClass().getRubricsByKind(getRubricKinds().get("exclusion"));

		return exclusionRubrics.stream().flatMap(r -> r.getLabels().stream())
				.flatMap(l -> l.getReferencesRecursive().stream()).collect(Collectors.toList());
	}

	@Override
	public Stream<IcdNode> getExclusionsImpl(Function<String, List<IcdNode>> byCode)
	{
		if (isRoot())
			return Stream.empty();

		List<Rubric> exclusionRubrics = getClamlClass().getRubricsByKind(getRubricKinds().get("exclusion"));

		Stream<IcdNode> nodes = exclusionRubrics.stream().flatMap(r -> r.getLabels().stream())
				.flatMap(l -> l.getReferencesRecursive().stream())
				.flatMap(r -> wrappIfUsageDifferent(byCode.apply(r.getValue()), toNodeUsage(r.getUsage())).stream())
				.distinct();

		return nodes;
	}

	public String getInclusionsString(Function<String, List<IcdNode>> byCode)
	{
		return getInclusions(byCode).map(n -> n.getCode()).collect(Collectors.joining(", ", "[", "]"));
	}

	public List<Reference> getInclusionRefereces()
	{
		if (isRoot())
			return Collections.emptyList();

		List<Rubric> inclusionRubrics = getClamlClass().getRubricsByKind(getRubricKinds().get("inclusion"));

		List<Reference> references = new ArrayList<>();

		references.addAll(inclusionRubrics.stream().flatMap(r -> r.getLabels().stream())
				.flatMap(l -> l.getReferencesRecursive().stream()).collect(Collectors.toList()));

		references.addAll(getPreferredRefereces());

		return references;
	}

	@Override
	public Stream<IcdNode> getInclusionsImpl(Function<String, List<IcdNode>> byCode)
	{
		if (isRoot())
			return Stream.empty();

		List<Rubric> inclusionRubrics = getClamlClass().getRubricsByKind(getRubricKinds().get("inclusion"));

		Stream<IcdNode> inclusions = inclusionRubrics.stream().flatMap(r -> r.getLabels().stream())
				.flatMap(l -> l.getReferencesRecursive().stream())
				.flatMap(r -> wrappIfUsageDifferent(byCode.apply(r.getValue()), toNodeUsage(r.getUsage())).stream())
				.distinct();

		Stream<IcdNode> preferreds = getPreferreds(byCode);

		return Stream.concat(inclusions, preferreds);
	}

	private List<Reference> getPreferredRefereces()
	{
		if (isRoot())
			return Collections.emptyList();

		List<Rubric> preferredRubrics = new ArrayList<>(
				getClamlClass().getRubricsByKind(getRubricKinds().get("preferred")));
		preferredRubrics.addAll(getClamlClass().getRubricsByKind(getRubricKinds().get("preferredLong")));

		return preferredRubrics.stream().flatMap(r -> r.getLabels().stream())
				.flatMap(l -> l.getReferencesRecursive().stream()).collect(Collectors.toList());
	}

	private Stream<IcdNode> getPreferreds(Function<String, List<IcdNode>> byCode)
	{
		if (isRoot())
			return Stream.empty();

		List<Rubric> preferredRubrics = new ArrayList<>(
				getClamlClass().getRubricsByKind(getRubricKinds().get("preferred")));
		preferredRubrics.addAll(getClamlClass().getRubricsByKind(getRubricKinds().get("preferredLong")));

		Stream<IcdNode> nodes = preferredRubrics.stream().flatMap(r -> r.getLabels().stream())
				.flatMap(l -> l.getReferencesRecursive().stream())
				.flatMap(r -> wrappIfUsageDifferent(byCode.apply(r.getValue()), toNodeUsage(r.getUsage())).stream())
				.distinct();

		return nodes;
	}
}

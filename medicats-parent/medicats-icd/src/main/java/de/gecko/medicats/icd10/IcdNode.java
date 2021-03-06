package de.gecko.medicats.icd10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.gecko.medicats.HasExclusions;
import de.gecko.medicats.HasInclusions;
import de.gecko.medicats.PreviousCodeMapping;
import de.gecko.medicats.PreviousCodeMappings;
import de.gecko.medicats.VersionedNode;

public abstract class IcdNode implements VersionedNode<IcdNode>, HasExclusions<IcdNode>, HasInclusions<IcdNode>
{
	public enum IcdNodeType
	{
		ROOT(""), CHAPTER("chapter"), BLOCK("block"), CATEGORY("category");

		private final String classKind;

		private IcdNodeType(String classKind)
		{
			this.classKind = classKind;
		}

		public String getClassKind()
		{
			return classKind;
		}
	}

	public enum IcdNodeUsage
	{
		UNDEFINED("", ""), ASTER("aster", "*"), DAGGER("dagger", "†"), OPTIONAL("optional", "!");

		private final String usageKind;
		private final String icon;

		private IcdNodeUsage(String usageKind, String icon)
		{
			this.usageKind = usageKind;
			this.icon = icon;
		}

		public String getUsageKind()
		{
			return usageKind;
		}

		public String getIcon()
		{
			return icon;
		}

		public static IcdNodeUsage fromString(String s)
		{
			if (s == null)
				return IcdNodeUsage.UNDEFINED;

			switch (s)
			{
				case "*":
					return IcdNodeUsage.ASTER;
				case "+":
				case "†":
					return IcdNodeUsage.DAGGER;
				case "!":
					return IcdNodeUsage.OPTIONAL;
				default:
					return IcdNodeUsage.UNDEFINED;
			}
		}
	}

	private final List<IcdNode> children = new ArrayList<>();
	private final IcdNode parent;

	public IcdNode(IcdNode parent)
	{
		this.parent = parent;
	}

	/*
	 * Do not call from constructor -> IcdNodeWrapper...
	 */
	protected void addToParent()
	{
		if (getParent() != null)
			getParent().children.add(this);
	}

	@Override
	public IcdNode getParent()
	{
		return parent;
	}

	@Override
	public Stream<IcdNode> children()
	{
		return children.stream();
	}

	public List<IcdNode> getChildren()
	{
		return Collections.unmodifiableList(children);
	}

	public int getHierarchyDepth()
	{
		return getParent().getHierarchyDepth() + 1;
	}

	@Override
	public abstract String getLabel();

	@Override
	public abstract String getCode();

	public abstract IcdNodeType getNodeType();

	public abstract IcdNodeUsage getNodeUsage();

	public boolean isRoot()
	{
		return getParent() == null;
	}

	public int getParentCount()
	{
		if (isRoot())
			return 0;
		else
			return getParent().getParentCount() + 1;
	}

	public int getCodeLength()
	{
		return getCode().length();
	}

	@Override
	public String getPreviousVersion()
	{
		return getParent().getPreviousVersion();
	}

	@Override
	public String getVersion()
	{
		return getParent().getVersion();
	}

	public int getSortIndex()
	{
		return getParent().getSortIndex();
	}

	protected PreviousCodeMappings getPreviousCodes()
	{
		return getParent().getPreviousCodes();
	}

	@Override
	public Optional<IcdNode> getPrevious()
	{
		if (getPreviousNodeWalker() == null)
			return Optional.empty();

		Optional<IcdNode> previous = getPreviousNodeWalker().getNodeByCode(getPreviousMapping());

		// if not found by previous code, try this code and check label
		// unchanged
		if (!previous.isPresent())
		{
			IcdNode sameCodePrevious = getPreviousNodeWalker().getNodeByCode(getCode());
			if (sameCodePrevious != null && sameCodePrevious.getLabel().equals(getLabel()))
				previous = Optional.of(sameCodePrevious);
		}

		return previous;
	}

	protected IcdNodeWalker getPreviousNodeWalker()
	{
		return getParent().getPreviousNodeWalker();
	}

	@Override
	public Optional<PreviousCodeMapping> getPreviousMapping()
	{
		PreviousCodeMapping mapping = getPreviousCodes().get(getCode()).orElse(tryPreviousSameCode());

		return Optional.ofNullable(mapping);
	}

	private PreviousCodeMapping tryPreviousSameCode()
	{
		if (getPreviousNodeWalker() == null)
			return null;

		IcdNode sameCodePrevious = getPreviousNodeWalker().getNodeByCode(getCode());
		if (sameCodePrevious != null && sameCodePrevious.getLabel().equals(getLabel()))
			return PreviousCodeMapping.unchanged(getCode());
		else
			return null;
	}

	@Override
	public final Stream<IcdNode> getInclusions(Function<String, List<IcdNode>> byCode)
	{
		return getInclusionsImpl(byCode).filter(distinctByKey(IcdNode::getPath));
	}

	@Override
	public final List<IcdNode> getInclusionsList(Function<String, List<IcdNode>> byCode)
	{
		return getInclusions(byCode).collect(Collectors.toList());
	}

	@Override
	public final Stream<IcdNode> getExclusions(Function<String, List<IcdNode>> byCode)
	{
		return getExclusionsImpl(byCode).filter(distinctByKey(IcdNode::getPath));
	}

	@Override
	public final List<IcdNode> getExclusionList(Function<String, List<IcdNode>> byCode)
	{
		return getExclusions(byCode).collect(Collectors.toList());
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
	{
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public abstract Stream<IcdNode> getInclusionsImpl(Function<String, List<IcdNode>> byCode);

	public abstract Stream<IcdNode> getExclusionsImpl(Function<String, List<IcdNode>> byCode);

	@Override
	public String toString()
	{
		return getCode() + getNodeUsage().getIcon() + " " + getLabel();
	}

	protected List<IcdNode> wrappIfUsageDifferent(List<? extends IcdNode> toWrapp, IcdNodeUsage usage)
	{
		List<IcdNode> wrappedNodes = new ArrayList<>();
		for (IcdNode n : toWrapp)
			wrappedNodes.add(wrappIfUsageDifferent(n, usage));

		return wrappedNodes;
	}

	protected IcdNode wrappIfUsageDifferent(IcdNode toWrapp, IcdNodeUsage usage)
	{
		if (toWrapp.getNodeUsage().equals(usage))
			return toWrapp;
		else
			return new IcdNodeWrapper(toWrapp, usage);
	}

	@Override
	public String getPath()
	{
		return getParent().getPath() + "/" + getCode();
	}
}

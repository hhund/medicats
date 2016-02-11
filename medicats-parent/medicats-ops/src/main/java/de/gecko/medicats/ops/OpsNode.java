package de.gecko.medicats.ops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.gecko.medicats.Node;

public abstract class OpsNode implements Node<OpsNode>
{
	public enum OpsNodeType
	{
		ROOT(""), CHAPTER("chapter"), BLOCK("block"), CATEGORY("category");

		private final String classKind;

		private OpsNodeType(String classKind)
		{
			this.classKind = classKind;
		}

		public String getClassKind()
		{
			return classKind;
		}
	}

	private final List<OpsNode> children = new ArrayList<>();
	private final OpsNode parent;

	public OpsNode(OpsNode parent)
	{
		this.parent = parent;
	}

	protected void addToParent()
	{
		if (getParent() != null)
			getParent().children.add(this);
	}

	public OpsNode getParent()
	{
		return parent;
	}

	public List<OpsNode> getChildren()
	{
		return Collections.unmodifiableList(children);
	}

	@Override
	public Stream<OpsNode> children()
	{
		return children.stream();
	}

	public int getHierarchyDepth()
	{
		return getParent().getHierarchyDepth() + 1;
	}

	@Override
	public abstract String getLabel();

	@Override
	public abstract String getCode();

	public abstract OpsNodeType getNodeType();

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

	public String getPreviousVersion()
	{
		return getParent().getPreviousVersion();
	}

	/**
	 * @return may be <code>null</code>
	 */
	public String getPreviousCode()
	{
		return getPreviousCodes().get(getCode());
	}

	protected Map<String, String> getPreviousCodes()
	{
		return getParent().getPreviousCodes();
	}

	protected OpsNodeWalker getPreviousNodeWalker()
	{
		return getParent().getPreviousNodeWalker();
	}

	/**
	 * @return might be <code>null</code>
	 */
	public OpsNode getPrevious()
	{
		if (getPreviousNodeWalker() == null)
			return null;

		OpsNode previous = null;
		if (getPreviousCode() != null)
			previous = getPreviousNodeWalker().getNodeByCode(getPreviousCode());

		// if not found by previous code, try this code and check label
		// unchanged
		if (previous == null)
		{
			OpsNode sameCodePrevious = getPreviousNodeWalker().getNodeByCode(getCode());
			if (sameCodePrevious != null && sameCodePrevious.getLabel().equals(getLabel()))
				previous = sameCodePrevious;
		}

		return previous;
	}

	public String getVersion()
	{
		return getParent().getVersion();
	}

	public String getPath()
	{
		return getParent().getPath() + "/" + getCode();
	}

	@Override
	public String toString()
	{
		return getCode() + " " + getLabel();
	}

	public final Stream<OpsNode> getInclusions(Function<String, List<OpsNode>> byCode)
	{
		return getInclusionsImpl(byCode).filter(distinctByKey(OpsNode::getPath));
	}

	public final List<OpsNode> getInclusionsList(Function<String, List<OpsNode>> byCode)
	{
		return getInclusions(byCode).collect(Collectors.toList());
	}

	public final Stream<OpsNode> getExclusions(Function<String, List<OpsNode>> byCode)
	{
		return getExclusionsImpl(byCode).filter(distinctByKey(OpsNode::getPath));
	}

	public final List<OpsNode> getExclusionList(Function<String, List<OpsNode>> byCode)
	{
		return getExclusions(byCode).collect(Collectors.toList());
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
	{
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public abstract Stream<OpsNode> getInclusionsImpl(Function<String, List<OpsNode>> byCode);

	public abstract Stream<OpsNode> getExclusionsImpl(Function<String, List<OpsNode>> byCode);
}

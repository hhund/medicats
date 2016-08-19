package de.gecko.medicats.icd10.sgml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.w3c.dom.Element;

import de.gecko.medicats.icd10.IcdNode;

public class SgmlIcdNode extends IcdNode
{
	public static SgmlIcdNode createNode(SgmlIcdNode parent, Element element, String label, String code,
			IcdNodeType nodeType, IcdNodeUsage nodeUsage, List<String> inclusionCodes, List<String> exclusionCodes)
	{
		SgmlIcdNode node = new SgmlIcdNode(Objects.requireNonNull(parent, "parent"),
				Objects.requireNonNull(element, "element"), Objects.requireNonNull(label, "label"),
				Objects.requireNonNull(code, "code"), Objects.requireNonNull(nodeType, "nodeType"),
				Objects.requireNonNull(nodeUsage, "nodeUsage"),
				Objects.requireNonNull(inclusionCodes, "inclusionCodes"),
				Objects.requireNonNull(exclusionCodes, "exclusionCodes"));

		if (code.isEmpty())
			throw new IllegalArgumentException("code is empty");

		node.addToParent();
		return node;
	}

	private final Element element;
	private final String label;
	private final String code;
	private final IcdNodeType nodeType;
	private final IcdNodeUsage nodeUsage;
	private final List<String> inclusionCodes;
	private final List<String> exclusionCodes;

	protected SgmlIcdNode(IcdNode parent, Element element, String label, String code, IcdNodeType nodeType,
			IcdNodeUsage nodeUsage, List<String> inclusionCodes, List<String> exclusionCodes)
	{
		super(parent);

		this.element = element;
		this.label = label;
		this.code = code;
		this.nodeType = nodeType;
		this.nodeUsage = nodeUsage;
		this.inclusionCodes = inclusionCodes;
		this.exclusionCodes = exclusionCodes;
	}

	public Element getSgmlElement()
	{
		return element;
	}

	@Override
	public String getLabel()
	{
		return label;
	}

	@Override
	public String getCode()
	{
		return code;
	}

	@Override
	public IcdNodeType getNodeType()
	{
		return nodeType;
	}

	@Override
	public IcdNodeUsage getNodeUsage()
	{
		return nodeUsage;
	}

	private static class IcdNodesWithExtraUsage
	{
		final List<IcdNode> nodes = new ArrayList<>();
		final IcdNodeUsage usage;

		IcdNodesWithExtraUsage(List<? extends IcdNode> nodes, IcdNodeUsage usage)
		{
			this.nodes.addAll(nodes);
			this.usage = usage;
		}
	}

	@Override
	public Stream<IcdNode> getInclusionsImpl(Function<String, List<IcdNode>> byCode)
	{
		if (inclusionCodes == null || inclusionCodes.isEmpty())
			return Stream.empty();

		return inclusionCodes.stream().map(c -> new IcdNodesWithExtraUsage(byCode.apply(removeUsage(c)), fromCode(c)))
				.map(e -> wrappIfUsageDifferent(e.nodes, e.usage)).flatMap(l -> l.stream()).distinct();
	}

	@Override
	public Stream<IcdNode> getExclusionsImpl(Function<String, List<IcdNode>> byCode)
	{
		if (exclusionCodes == null || exclusionCodes.isEmpty())
			return Stream.empty();

		return exclusionCodes.stream().map(c -> new IcdNodesWithExtraUsage(byCode.apply(removeUsage(c)), fromCode(c)))
				.map(e -> wrappIfUsageDifferent(e.nodes, e.usage)).flatMap(l -> l.stream()).distinct();
	}

	private String removeUsage(String code)
	{
		if (code.endsWith("*") || code.endsWith("+") || code.endsWith("!"))
			return code.substring(0, code.length() - 1);
		else
			return code;
	}

	private IcdNodeUsage fromCode(String code)
	{
		if (code.endsWith("*"))
			return IcdNodeUsage.ASTER;
		else if (code.endsWith("+"))
			return IcdNodeUsage.DAGGER;
		else if (code.endsWith("!"))
			return IcdNodeUsage.OPTIONAL;
		else
			return IcdNodeUsage.UNDEFINED;
	}
}

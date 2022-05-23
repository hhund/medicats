package de.gecko.medicats.icd10.sgml;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.w3c.dom.Element;

import de.gecko.medicats.icd10.IcdNode;

public class SgmlIcdNode extends IcdNode
{
	public static SgmlIcdNode createNode(SgmlIcdNode parent, Element element, String label, String code,
										 IcdNodeType nodeType, IcdNodeUsage nodeUsage, List<String> inclusionCodes,
										 List<String> exclusionCodes, List<String> inclusionStrings,
										 List<String> exclusionStrings)
	{
		SgmlIcdNode node = new SgmlIcdNode(Objects.requireNonNull(parent, "parent"),
				Objects.requireNonNull(element, "element"),
				Objects.requireNonNull(label, "label"),
				Objects.requireNonNull(code, "code"),
				Objects.requireNonNull(nodeType, "nodeType"),
				Objects.requireNonNull(nodeUsage, "nodeUsage"),
				Objects.requireNonNull(inclusionCodes, "inclusionCodes"),
				Objects.requireNonNull(exclusionCodes, "exclusionCodes"),
				Objects.requireNonNull(inclusionStrings, "inclusionStrings"),
				Objects.requireNonNull(exclusionStrings, "exclusionStrings"));

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

	private final List<String> inclusionStrings;
	private final List<String> exclusionStrings;

	protected SgmlIcdNode(IcdNode parent, Element element, String label, String code, IcdNodeType nodeType,
						  IcdNodeUsage nodeUsage, List<String> inclusionCodes, List<String> exclusionCodes, List<String> inclusionStrings, List<String> exclusionStrings)
	{
		super(parent);

		this.element = element;
		this.label = label;
		this.code = code;
		this.nodeType = nodeType;
		this.nodeUsage = nodeUsage;
		this.inclusionCodes = inclusionCodes;
		this.exclusionCodes = exclusionCodes;
		this.inclusionStrings = inclusionStrings;
		this.exclusionStrings = exclusionStrings;
	}

	public static SgmlIcdNode createNode(SgmlIcdNode parent,
										 Element chapter,
										 String label,
										 String code,
										 IcdNodeType nodeType,
										 IcdNodeUsage icdNodeUsage,
										 Map<String, String> inclusions,
										 Map<String, String> exclusions) {
		List<String> inclusionCodes = new ArrayList<>(inclusions.values());
		List<String> exclusionCodes = new ArrayList<>(exclusions.values());
		List<String> inclusionStrings = new ArrayList<>(inclusions.keySet());
		List<String> exclusionStrings = new ArrayList<>(exclusions.keySet());
		return createNode(parent, chapter, label, code, nodeType, icdNodeUsage, inclusionCodes, exclusionCodes, inclusionStrings, exclusionStrings);
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

	public List<String> getInclusionCodes() {
		return inclusionCodes;
	}

	public List<String> getInclusionStrings() {
		return inclusionStrings;
	}

	public List<String> getExclusionCodes() {
		return exclusionCodes;
	}

	public List<String> getExclusionStrings() {
		return exclusionStrings;
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
				.map(e -> wrappIfUsageDifferent(e.nodes, e.usage)).flatMap(Collection::stream).distinct();
	}

	@Override
	public Stream<IcdNode> getExclusionsImpl(Function<String, List<IcdNode>> byCode)
	{
		if (exclusionCodes == null || exclusionCodes.isEmpty())
			return Stream.empty();

		return exclusionCodes.stream().map(c -> new IcdNodesWithExtraUsage(byCode.apply(removeUsage(c)), fromCode(c)))
				.map(e -> wrappIfUsageDifferent(e.nodes, e.usage)).flatMap(Collection::stream).distinct();
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

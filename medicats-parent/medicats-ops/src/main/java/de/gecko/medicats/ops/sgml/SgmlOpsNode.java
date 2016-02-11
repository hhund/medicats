package de.gecko.medicats.ops.sgml;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.w3c.dom.Element;

import de.gecko.medicats.ops.OpsNode;

public class SgmlOpsNode extends OpsNode
{
	public static SgmlOpsNode createNode(SgmlOpsNode parent, Element element, String label, String code, OpsNodeType nodeType,
			List<String> inclusionCodes, List<String> exclusionCodes)
	{
		SgmlOpsNode node = new SgmlOpsNode(Objects.requireNonNull(parent, "parent"),
				Objects.requireNonNull(element, "element"), Objects.requireNonNull(label, "label"),
				Objects.requireNonNull(code, "code"), Objects.requireNonNull(nodeType, "nodeType"),
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
	private final OpsNodeType nodeType;
	private final List<String> inclusionCodes;
	private final List<String> exclusionCodes;

	protected SgmlOpsNode(OpsNode parent, Element element, String label, String code, OpsNodeType nodeType,
			List<String> inclusionCodes, List<String> exclusionCodes)
	{
		super(parent);

		this.element = element;
		this.label = label;
		this.code = code;
		this.nodeType = nodeType;
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
	public OpsNodeType getNodeType()
	{
		return nodeType;
	}

	@Override
	public Stream<OpsNode> getInclusionsImpl(Function<String, List<OpsNode>> byCode)
	{
		if (inclusionCodes == null || inclusionCodes.isEmpty())
			return Stream.empty();

		return inclusionCodes.stream().map(c -> byCode.apply(c)).flatMap(l -> l.stream()).distinct();
	}

	@Override
	public Stream<OpsNode> getExclusionsImpl(Function<String, List<OpsNode>> byCode)
	{
		if (exclusionCodes == null || exclusionCodes.isEmpty())
			return Stream.empty();

		return exclusionCodes.stream().map(c -> byCode.apply(c)).flatMap(l -> l.stream()).distinct();
	}

	public boolean isAmtl()
	{
		return "J".equals(getSgmlElement().getAttribute("AMTL"));
	}
}

package de.gecko.medicats.icd10;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class IcdNodeWrapper extends IcdNode
{
	private final IcdNodeUsage usage;
	private final IcdNode sourceNode;

	public IcdNodeWrapper(IcdNode sourceNode, IcdNodeUsage usage)
	{
		super(Objects.requireNonNull(sourceNode, "sourceNode").getParent());
		// , sourceNode.getClamlIcdClass(),
		// sourceNode.getPrimaryModifier(), sourceNode.getSecondaryModifier());

		this.usage = Objects.requireNonNull(usage, "usage");
		this.sourceNode = sourceNode;
	}

	public IcdNodeUsage getNodeUsage()
	{
		return usage;
	}

	public IcdNode getSourceNode()
	{
		return sourceNode;
	}

	@Override
	public String getLabel()
	{
		return getSourceNode().getLabel();
	}

	@Override
	public String getCode()
	{
		return getSourceNode().getCode();
	}

	@Override
	public IcdNodeType getNodeType()
	{
		return getSourceNode().getNodeType();
	}

	@Override
	public Stream<IcdNode> getInclusionsImpl(Function<String, List<? extends IcdNode>> byCode)
	{
		return getSourceNode().getInclusionsImpl(byCode);
	}

	@Override
	public Stream<IcdNode> getExclusionsImpl(Function<String, List<? extends IcdNode>> byCode)
	{
		return getSourceNode().getExclusionsImpl(byCode);
	}
}

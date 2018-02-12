package de.gecko.medicats.ops;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractOpsNodeWalker implements OpsNodeWalker
{
	private static final String[] INDENTENTIONS = { "", "\t", "\t\t", "\t\t\t", "\t\t\t\t", "\t\t\t\t\t",
			"\t\t\t\t\t\t", "\t\t\t\t\t\t\t", "\t\t\t\t\t\t\t\t", "\t\t\t\t\t\t\t\t\t" };

	private final OpsNode root;
	private Map<String, OpsNode> nodesByCode;

	public AbstractOpsNodeWalker(OpsNode root)
	{
		this.root = root;
	}

	@Override
	public OpsNode getRootNode()
	{
		return root;
	}

	@Override
	public Map<String, OpsNode> getNodesByCode()
	{
		if (nodesByCode == null)
			nodesByCode = preOrderStream().collect(Collectors.toMap(OpsNode::getCode, Function.identity()));

		return Collections.unmodifiableMap(nodesByCode);
	}

	@Override
	public List<OpsNode> getNodesBySudoCode(String code)
	{
		if (code == null)
			return Collections.emptyList();
		else
		{
			OpsNode nodeByCode = getNodeByCode(code);
			if (nodeByCode != null)
				return Collections.singletonList(nodeByCode);
			else
				return Collections.emptyList();
		}
	}

	@Override
	public void walkNodesPreOrder(OpsNodeConsumer consumer)
	{
		walkNodesPreOrder(Objects.requireNonNull(consumer, "consumer"), root);
	}

	private void walkNodesPreOrder(OpsNodeConsumer consumer, OpsNode node)
	{
		Stream<OpsNode> exclusions = node.getExclusions(this::getNodesBySudoCode);
		Stream<OpsNode> inclusions = node.getInclusions(this::getNodesBySudoCode);

		consumer.accept(node, exclusions, inclusions);

		List<OpsNode> children = node.getChildren();

		if (!children.isEmpty())
			for (OpsNode chiledNode : children)
				walkNodesPreOrder(consumer, chiledNode);
	}

	@Override
	public void walkNodesPostOrder(OpsNodeConsumer consumer)
	{
		walkNodesPostOrder(Objects.requireNonNull(consumer, "consumer"), root);
	}

	private void walkNodesPostOrder(OpsNodeConsumer consumer, OpsNode node)
	{
		List<OpsNode> children = node.getChildren();

		if (!children.isEmpty())
			for (OpsNode chiledNode : children)
				walkNodesPostOrder(consumer, chiledNode);

		Stream<OpsNode> exclusions = node.getExclusions(this::getNodesBySudoCode);
		Stream<OpsNode> inclusions = node.getInclusions(this::getNodesBySudoCode);

		consumer.accept(node, exclusions, inclusions);
	}

	@Override
	public Consumer<OpsNode> printer(PrintStream out)
	{
		return n ->
		{
			final String identention = INDENTENTIONS[n.getParentCount()];

			out.println(identention + n.toString());

			List<OpsNode> exclusionsList = n.getExclusionList(this::getNodesBySudoCode);
			List<OpsNode> inclusionsList = n.getInclusionList(this::getNodesBySudoCode);

			if (!exclusionsList.isEmpty())
				out.println(identention + " -" + exclusionsList);
			if (!inclusionsList.isEmpty())
				out.println(identention + " +" + inclusionsList);
		};
	}
}

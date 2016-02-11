package de.gecko.medicats.ops;

import java.util.List;
import java.util.stream.Stream;

import de.gecko.medicats.NodeWalker;

public interface OpsNodeWalker extends NodeWalker<OpsNode>
{
	@FunctionalInterface
	public interface OpsNodeConsumer
	{
		void accept(OpsNode node, Stream<OpsNode> exclusions, Stream<OpsNode> inclusions);
	}

	List<OpsNode> getNodesBySudoCode(String code);

	void walkNodesPreOrder(OpsNodeConsumer consumer);

	void walkNodesPostOrder(OpsNodeConsumer consumer);
}
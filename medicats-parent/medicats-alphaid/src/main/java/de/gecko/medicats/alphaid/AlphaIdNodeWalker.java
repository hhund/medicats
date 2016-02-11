package de.gecko.medicats.alphaid;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import de.gecko.medicats.NodeWalker;

public interface AlphaIdNodeWalker extends NodeWalker<AlphaIdNode>
{
	void walkNodes(Consumer<AlphaIdNode> consumer);

	Stream<AlphaIdNode> stream();

	List<AlphaIdNode> getNodes();
}
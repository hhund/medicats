package de.gecko.medicats.alphaid;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AlphaIdNodeWalkerImpl implements AlphaIdNodeWalker
{
	private final AlphaIdNodeRoot root;
	private Map<String, AlphaIdNode> byCode;

	public AlphaIdNodeWalkerImpl(AlphaIdNodeRoot root)
	{
		this.root = root;
	}

	@Override
	public AlphaIdNode getRootNode()
	{
		return root;
	}

	@Override
	public AlphaIdNode getNodeByCode(String code)
	{
		return getNodesByCode().get(code);
	}

	@Override
	public Map<String, AlphaIdNode> getNodesByCode()
	{
		if (byCode == null)
			byCode = root.getChildren().stream()
					.collect(Collectors.toMap(AlphaIdNode::getAlphaId, Function.identity()));

		return Collections.unmodifiableMap(byCode);
	}

	@Override
	public void walkNodes(Consumer<AlphaIdNode> consumer)
	{
		consumer.accept(root);

		root.getChildren().forEach(consumer);
	}

	@Override
	public Stream<AlphaIdNode> stream()
	{
		return Stream.concat(Stream.of(root), root.getChildren().stream());
	}
	
	@Override
	public List<AlphaIdNode> getNodes()
	{
		List<AlphaIdNode> nodes = new ArrayList<>(getRootNode().getChildren().size() + 1);

		nodes.add(getRootNode());
		nodes.addAll(getRootNode().getChildren());

		return nodes;
	}

	@Override
	public Consumer<AlphaIdNode> printer(PrintStream out)
	{
		return n ->
		{
			if (getRootNode().equals(n))
				out.println(n);
			else
				out.println("\t" + n);
		};
	}
}

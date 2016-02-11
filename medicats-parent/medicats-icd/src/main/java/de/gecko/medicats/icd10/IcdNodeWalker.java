package de.gecko.medicats.icd10;

import java.util.List;
import java.util.stream.Stream;

import de.gecko.medicats.NodeWalker;

public interface IcdNodeWalker extends NodeWalker<IcdNode>
{
	@FunctionalInterface
	public interface IcdNodeConsumer
	{
		void accept(IcdNode node, Stream<IcdNode> exclusions, Stream<IcdNode> inclusions);
	}

	List<IcdNode> getNodesBySudoCode(String code);

	List<IcdNode> getNodes(String codeFrom, String codeTo);

	List<IcdNode> getNodesFiltered(String codeFrom, String codeTo);

	void walkNodesPreOrder(IcdNodeConsumer consumer);

	void walkNodesPostOrder(IcdNodeConsumer consumer);
}
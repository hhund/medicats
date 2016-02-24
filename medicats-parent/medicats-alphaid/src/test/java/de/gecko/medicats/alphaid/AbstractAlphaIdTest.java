package de.gecko.medicats.alphaid;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public abstract class AbstractAlphaIdTest
{
	protected abstract int expectedNodesCount();

	protected abstract String getVersion();

	@Test
	public void codeCountTest() throws Exception
	{
		AlphaIdService service = AlphaIdService.getService();
		assertNotNull(service);

		AlphaIdNodeFactory nodeFactory = service.getNodeFactory(getVersion());
		assertNotNull(nodeFactory);

		AlphaIdNodeWalker nodeWalker = nodeFactory.createNodeWalker();
		assertNotNull(nodeWalker);

		List<AlphaIdNode> nodes = nodeWalker.getNodes();
		assertNotNull(nodes);

		assertEquals(expectedNodesCount(), nodes.size());
	}
}

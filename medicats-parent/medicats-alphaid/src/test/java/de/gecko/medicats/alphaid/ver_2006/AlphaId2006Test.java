package de.gecko.medicats.alphaid.ver_2006;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2006Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 73765 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2006";
	}
}

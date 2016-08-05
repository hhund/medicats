package de.gecko.medicats.alphaid.ver_2016;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2016Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 79643 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2016";
	}
}

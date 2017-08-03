package de.gecko.medicats.alphaid.ver_2017;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2017Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 80455 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2017";
	}
}

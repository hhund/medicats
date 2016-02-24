package de.gecko.medicats.alphaid.ver_2014;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2014Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 77671 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2014";
	}
}

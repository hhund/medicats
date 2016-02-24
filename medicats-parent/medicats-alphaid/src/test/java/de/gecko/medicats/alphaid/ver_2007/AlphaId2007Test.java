package de.gecko.medicats.alphaid.ver_2007;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2007Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 74280 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2007";
	}
}

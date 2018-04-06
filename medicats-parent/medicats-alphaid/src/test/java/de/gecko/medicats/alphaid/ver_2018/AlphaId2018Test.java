package de.gecko.medicats.alphaid.ver_2018;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2018Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 80681 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2018";
	}
}

package de.gecko.medicats.alphaid.ver_2019;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2019Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 81777 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2019";
	}
}

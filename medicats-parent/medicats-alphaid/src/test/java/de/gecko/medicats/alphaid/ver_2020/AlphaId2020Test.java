package de.gecko.medicats.alphaid.ver_2020;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2020Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 83804 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2020";
	}
}

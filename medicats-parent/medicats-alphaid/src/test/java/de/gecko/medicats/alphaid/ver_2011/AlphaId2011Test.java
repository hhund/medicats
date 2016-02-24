package de.gecko.medicats.alphaid.ver_2011;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2011Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 76954 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2011";
	}
}

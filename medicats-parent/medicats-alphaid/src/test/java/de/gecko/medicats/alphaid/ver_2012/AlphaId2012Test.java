package de.gecko.medicats.alphaid.ver_2012;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2012Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 77120 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2012";
	}
}

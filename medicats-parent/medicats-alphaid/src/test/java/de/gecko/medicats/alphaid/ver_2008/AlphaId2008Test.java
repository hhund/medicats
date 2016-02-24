package de.gecko.medicats.alphaid.ver_2008;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2008Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 76134 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2008";
	}
}

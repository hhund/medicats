package de.gecko.medicats.alphaid.ver_2009;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2009Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 76350 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2009";
	}
}

package de.gecko.medicats.alphaid.ver_2005;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2005Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 66824 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2005";
	}
}

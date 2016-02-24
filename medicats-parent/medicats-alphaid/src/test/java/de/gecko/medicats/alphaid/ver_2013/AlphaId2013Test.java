package de.gecko.medicats.alphaid.ver_2013;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2013Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 77467 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2013";
	}
}

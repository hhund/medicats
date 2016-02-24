package de.gecko.medicats.alphaid.ver_2015;

import de.gecko.medicats.alphaid.AbstractAlphaIdTest;

public class AlphaId2015Test extends AbstractAlphaIdTest
{
	@Override
	protected int expectedNodesCount()
	{
		return 78214 + 1; // + Root Node
	}

	@Override
	protected String getVersion()
	{
		return "alphaid2015";
	}
}

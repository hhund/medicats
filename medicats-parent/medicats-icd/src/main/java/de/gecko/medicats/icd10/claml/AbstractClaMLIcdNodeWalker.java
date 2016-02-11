package de.gecko.medicats.icd10.claml;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.gecko.medicats.icd10.AbstractIcdNodeWalker;
import de.gecko.medicats.icd10.IcdNode;

public class AbstractClaMLIcdNodeWalker extends AbstractIcdNodeWalker
{
	public AbstractClaMLIcdNodeWalker(ClaMLIcdNode root)
	{
		super(root);
	}

	@Override
	protected List<IcdNode> fromSpecialCasesOrEmptyList(String code)
	{
		if (code.equals("E10-E14, vierte Stelle .0"))
			return Arrays.asList(getNodeByCode("E10.0"), getNodeByCode("E11.0"), getNodeByCode("E12.0"),
					getNodeByCode("E13.0"), getNodeByCode("E14.0"));
		if (code.equals("E10-E14, vierte Stelle .1"))
			return Arrays.asList(getNodeByCode("E10.1"), getNodeByCode("E11.1"), getNodeByCode("E12.1"),
					getNodeByCode("E13.1"), getNodeByCode("E14.1"));
		if (code.equals("E10-E14, vierte Stelle .2"))
			return Arrays.asList(getNodeByCode("E10.2"), getNodeByCode("E11.2"), getNodeByCode("E12.2"),
					getNodeByCode("E13.2"), getNodeByCode("E14.2"));
		if (code.equals("E10-E14, vierte Stelle .3"))
			return Arrays.asList(getNodeByCode("E10.3"), getNodeByCode("E11.3"), getNodeByCode("E12.3"),
					getNodeByCode("E13.3"), getNodeByCode("E14.3"));
		if (code.equals("E10-E14, vierte Stelle .4"))
			return Arrays.asList(getNodeByCode("E10.4"), getNodeByCode("E11.4"), getNodeByCode("E12.4"),
					getNodeByCode("E13.4"), getNodeByCode("E14.4"));
		if (code.equals("E10-E14, vierte Stelle .5"))
			return Arrays.asList(getNodeByCode("E10.5"), getNodeByCode("E11.5"), getNodeByCode("E12.5"),
					getNodeByCode("E13.5"), getNodeByCode("E14.5"));
		if (code.equals("E10-E14, vierte Stelle .6"))
			return Arrays.asList(getNodeByCode("E10.6"), getNodeByCode("E11.6"), getNodeByCode("E12.6"),
					getNodeByCode("E13.6"), getNodeByCode("E14.6"));

		if (code.equals("F10-F19 mit vierter Stelle .2"))
			return Arrays.asList(getNodeByCode("F10.2"), getNodeByCode("F11.2"), getNodeByCode("F12.2"),
					getNodeByCode("F13.2"), getNodeByCode("F14.2"), getNodeByCode("F15.2"), getNodeByCode("F16.2"),
					getNodeByCode("F17.2"), getNodeByCode("F18.2"), getNodeByCode("F19.2"));
		if (code.equals("F10-F19, vierte Stelle .0"))
			return Arrays.asList(getNodeByCode("F10.0"), getNodeByCode("F11.0"), getNodeByCode("F12.0"),
					getNodeByCode("F13.0"), getNodeByCode("F14.0"), getNodeByCode("F15.0"), getNodeByCode("F16.0"),
					getNodeByCode("F17.0"), getNodeByCode("F18.0"), getNodeByCode("F19.0"));
		if (code.equals("F10-F19, vierte Stelle .6"))
			return Arrays.asList(getNodeByCode("F10.6"), getNodeByCode("F11.6"), getNodeByCode("F12.6"),
					getNodeByCode("F13.6"), getNodeByCode("F14.6"), getNodeByCode("F15.6"), getNodeByCode("F16.6"),
					getNodeByCode("F17.6"), getNodeByCode("F18.6"), getNodeByCode("F19.6"));

		if (code.equals("F11-F19, vierte Stelle .5"))
			return Arrays.asList(getNodeByCode("F11.5"), getNodeByCode("F12.5"), getNodeByCode("F13.5"),
					getNodeByCode("F14.5"), getNodeByCode("F15.5"), getNodeByCode("F16.5"), getNodeByCode("F17.5"),
					getNodeByCode("F18.5"), getNodeByCode("F19.5"));
		if (code.equals("F11-F19, vierte Stelle .6"))
			return Arrays.asList(getNodeByCode("F11.6"), getNodeByCode("F12.6"), getNodeByCode("F13.6"),
					getNodeByCode("F14.6"), getNodeByCode("F15.6"), getNodeByCode("F16.6"), getNodeByCode("F17.6"),
					getNodeByCode("F18.6"), getNodeByCode("F19.6"));

		if (code.equals("F19 mit vierter Stelle .2"))
			return Collections.singletonList(getNodeByCode("F19.2"));

		if (code.equals("N39.3-N39.4-"))
			return Arrays.asList(getNodeByCode("N39.3"), getNodeByCode("N39.4"));

		if (code.equals("Z75.6-, Z75.7-"))
			return Arrays.asList(getNodeByCode("Z75.6"), getNodeByCode("Z75.7"));

		return Collections.emptyList();
	}
}

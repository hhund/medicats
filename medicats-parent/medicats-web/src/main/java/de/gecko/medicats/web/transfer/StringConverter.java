package de.gecko.medicats.web.transfer;

import java.util.Locale;

import de.gecko.medicats.icd10.IcdNode.IcdNodeType;
import de.gecko.medicats.icd10.IcdNode.IcdNodeUsage;
import de.gecko.medicats.ops.OpsNode.OpsNodeType;

public final class StringConverter
{
	private StringConverter()
	{
	}

	public static String toString(IcdNodeUsage nodeUsage)
	{
		switch (nodeUsage)
		{
			case ASTER:
				return "*";
			case DAGGER:
				return "+";
			case OPTIONAL:
				return "!";
			case UNDEFINED:
			default:
				return null;
		}
	}

	public static String toString(IcdNodeType nodeType)
	{
		return nodeType.toString().toLowerCase(Locale.ENGLISH);
	}

	public static String toString(OpsNodeType nodeType)
	{
		return nodeType.toString().toLowerCase(Locale.ENGLISH);
	}
}

package de.gecko.medicats.claml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "usageKinds" })
@XmlRootElement(name = "UsageKinds")
public class UsageKinds
{
	@XmlElement(name = "UsageKind", required = true)
	private List<UsageKind> usageKinds;

	/**
	 * @return unmodifiable list
	 */
	public List<UsageKind> getUsageKinds()
	{
		if (usageKinds == null)
			usageKinds = new ArrayList<>();

		return Collections.unmodifiableList(usageKinds);
	}
}

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
@XmlType(name = "", propOrder = { "variant" })
@XmlRootElement(name = "Variants")
public class Variants
{
	@XmlElement(name = "Variant", required = true)
	private List<Variant> variant;

	/**
	 * @return unmodifiable list
	 */
	public List<Variant> getVariant()
	{
		if (variant == null)
			variant = new ArrayList<>();

		return Collections.unmodifiableList(variant);
	}
}

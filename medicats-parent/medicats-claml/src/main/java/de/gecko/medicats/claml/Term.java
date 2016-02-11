package de.gecko.medicats.claml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "value" })
@XmlRootElement(name = "Term")
public class Term
{
	@XmlAttribute(name = "class")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String termClass;

	@XmlValue
	private String value;

	public String getClazz()
	{
		return termClass;
	}

	public String getValue()
	{
		return value;
	}
}

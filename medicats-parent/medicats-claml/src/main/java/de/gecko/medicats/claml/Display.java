package de.gecko.medicats.claml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "value" })
@XmlRootElement(name = "Display")
public class Display
{
	@XmlAttribute(name = "xml:lang", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String xmlLang;

	@XmlAttribute(name = "variants")
	@XmlIDREF
	private Object variants;

	@XmlValue
	private String value;

	public String getXmlLang()
	{
		return xmlLang;
	}

	public Object getVariants()
	{
		return variants;
	}

	public String getvalue()
	{
		return value;
	}
}

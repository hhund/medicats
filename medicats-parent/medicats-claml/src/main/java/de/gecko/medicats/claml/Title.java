package de.gecko.medicats.claml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "value" })
@XmlRootElement(name = "Title")
public class Title
{
	@XmlAttribute(name = "name", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String name;

	@XmlAttribute(name = "version")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String version;

	@XmlAttribute(name = "date")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String date;

	@XmlValue
	private String value;

	public String getName()
	{
		return name;
	}

	public String getVersion()
	{
		return version;
	}

	public String getDate()
	{
		return date;
	}

	public String getvalue()
	{
		return value;
	}
}

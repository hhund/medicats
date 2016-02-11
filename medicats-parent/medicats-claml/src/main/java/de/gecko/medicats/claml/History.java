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
@XmlRootElement(name = "History")
public class History
{
	@XmlAttribute(name = "author", required = true)
	@XmlIDREF
	private Object author;

	@XmlAttribute(name = "date", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String date;

	@XmlValue
	private String value;

	public Object getAuthor()
	{
		return author;
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

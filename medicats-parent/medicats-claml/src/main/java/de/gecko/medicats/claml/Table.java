package de.gecko.medicats.claml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "caption", "head", "body", "foot" })
@XmlRootElement(name = "Table")
public class Table
{
	@XmlAttribute(name = "class")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String tableClass;

	@XmlElement(name = "Caption")
	private Caption caption;

	@XmlElement(name = "THead")
	private THead head;

	@XmlElement(name = "TBody")
	private TBody body;

	@XmlElement(name = "TFoot")
	private TFoot foot;

	public String getTableClass()
	{
		return tableClass;
	}

	public Caption getCaption()
	{
		return caption;
	}

	public THead getHead()
	{
		return head;
	}

	public TBody getBody()
	{
		return body;
	}

	public TFoot getFoot()
	{
		return foot;
	}
}

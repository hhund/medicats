package de.gecko.medicats.claml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "IncludeDescendants")
public class IncludeDescendants
{
	@XmlAttribute(name = "code", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String code;

	@XmlAttribute(name = "kind", required = true)
	@XmlIDREF
	private Object kind;

	public String getCode()
	{
		return code;
	}

	public Object getKind()
	{
		return kind;
	}
}

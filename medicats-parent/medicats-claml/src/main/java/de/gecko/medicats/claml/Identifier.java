package de.gecko.medicats.claml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "Identifier")
public class Identifier
{
	@XmlAttribute(name = "authority")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String authority;

	@XmlAttribute(name = "uid", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String uid;

	public String getAuthority()
	{
		return authority;
	}

	public String getUid()
	{
		return uid;
	}
}

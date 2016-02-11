package de.gecko.medicats.claml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "Include")
public class Include
{
	@XmlAttribute(name = "class")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String includeClass;

	@XmlAttribute(name = "rubric", required = true)
	@XmlIDREF
	protected Object rubric;

	public String getIncludeClass()
	{
		return includeClass;
	}

	public Object getRubric()
	{
		return rubric;
	}
}

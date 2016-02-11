package de.gecko.medicats.claml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "value" })
@XmlRootElement(name = "Reference")
public class Reference
{
	@XmlAttribute(name = "class")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String referenceClass;

	@XmlAttribute(name = "authority")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String authority;

	@XmlAttribute(name = "uid")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String uid;

	@XmlAttribute(name = "code")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String code;

	@XmlAttribute(name = "usage")
	@XmlIDREF
	private Object usage;

	@XmlAttribute(name = "variants")
	@XmlIDREF
	private List<Object> variants;

	@XmlValue
	private String value;

	public String getReferenceClass()
	{
		return referenceClass;
	}

	public String getAuthority()
	{
		return authority;
	}

	public String getUid()
	{
		return uid;
	}

	public String getCode()
	{
		return code;
	}

	public Object getUsage()
	{
		return usage;
	}

	/**
	 * @return unmodifiable list
	 */
	public List<Object> getVariants()
	{
		if (variants == null)
			variants = new ArrayList<>();

		return Collections.unmodifiableList(variants);
	}

	public String getValue()
	{
		return value;
	}
}

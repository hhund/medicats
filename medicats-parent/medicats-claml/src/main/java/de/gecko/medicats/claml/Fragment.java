package de.gecko.medicats.claml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "values" })
@XmlRootElement(name = "Fragment")
public class Fragment implements WithMixedValues
{
	@XmlAttribute(name = "class")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String fragmentClass;

	@XmlAttribute(name = "usage")
	@XmlIDREF
	private Object usage;

	@XmlAttribute(name = "type")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String type;

	@XmlElementRefs({ @XmlElementRef(name = "Reference", type = Reference.class),
			@XmlElementRef(name = "Term", type = Term.class) })
	@XmlMixed
	private List<Object> values;

	public String getFragmentClass()
	{
		return fragmentClass;
	}

	public Object getUsage()
	{
		return usage;
	}

	public String getType()
	{
		return type == null ? "item" : type;
	}

	@Override
	public List<Object> getValues()
	{
		if (values == null)
			values = new ArrayList<>();

		return Collections.unmodifiableList(values);
	}
}

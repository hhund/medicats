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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "values" })
@XmlRootElement(name = "Label")
public class Label implements WithMixedValues
{
	@XmlAttribute(name = "xml:lang", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String xmlLang;

	@XmlAttribute(name = "xml:space")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String xmlSpace;

	@XmlAttribute(name = "variants")
	@XmlIDREF
	private List<Object> variants;

	@XmlElementRefs({ @XmlElementRef(name = "Reference", type = Reference.class),
			@XmlElementRef(name = "Term", type = Term.class), @XmlElementRef(name = "Para", type = Para.class),
			@XmlElementRef(name = "IncludeDescendants", type = IncludeDescendants.class),
			@XmlElementRef(name = "Fragment", type = Fragment.class),
			@XmlElementRef(name = "List", type = LabelList.class), @XmlElementRef(name = "Table", type = Table.class) })
	@XmlMixed
	private List<Object> values;

	public String getXmlLang()
	{
		return xmlLang;
	}

	public String getXmlSpace()
	{
		return xmlSpace == null ? "default" : xmlSpace;
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

	@Override
	public List<Object> getValues()
	{
		if (values == null)
			values = new ArrayList<>();

		return Collections.unmodifiableList(values);
	}
}

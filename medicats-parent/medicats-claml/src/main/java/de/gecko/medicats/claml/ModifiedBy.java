package de.gecko.medicats.claml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "metaElements", "validModifierClasses" })
@XmlRootElement(name = "ModifiedBy")
public class ModifiedBy
{
	@XmlAttribute(name = "code", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String code;

	@XmlAttribute(name = "all")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String all;

	@XmlAttribute(name = "position")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String position;

	@XmlAttribute(name = "variants")
	@XmlIDREF
	private List<Object> variants;

	@XmlElement(name = "Meta")
	private List<Meta> metaElements;

	@XmlElement(name = "ValidModifierClass")
	private List<ValidModifierClass> validModifierClasses;

	public String getCode()
	{
		return code;
	}

	public String getAll()
	{
		return all == null ? "true" : all;
	}

	public String getPosition()
	{
		return position;
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

	/**
	 * @return unmodifiable list
	 */
	public List<Meta> getMetaElements()
	{
		if (metaElements == null)
			metaElements = new ArrayList<>();

		return Collections.unmodifiableList(metaElements);
	}

	/**
	 * @return unmodifiable list
	 */
	public List<ValidModifierClass> getValidModifierClasses()
	{
		if (validModifierClasses == null)
			validModifierClasses = new ArrayList<>();

		return Collections.unmodifiableList(validModifierClasses);
	}
}

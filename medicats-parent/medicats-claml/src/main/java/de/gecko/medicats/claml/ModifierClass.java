package de.gecko.medicats.claml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "metaElements", "superClass", "subClasses", "rubrics", "historyElements" })
@XmlRootElement(name = "ModifierClass")
public class ModifierClass
{
	@XmlAttribute(name = "modifier", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String modifier;

	@XmlAttribute(name = "code", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String code;

	@XmlAttribute(name = "usage")
	@XmlIDREF
	private Object usage;

	@XmlAttribute(name = "variants")
	@XmlIDREF
	private List<Object> variants;

	@XmlElement(name = "Meta")
	private List<Meta> metaElements;

	@XmlElement(name = "SuperClass", required = true)
	private SuperClass superClass;

	@XmlElement(name = "SubClass")
	private List<SubClass> subClasses;

	@XmlElement(name = "Rubric")
	private List<Rubric> rubrics;

	@XmlElement(name = "History")
	private List<History> historyElements;

	public String getModifier()
	{
		return modifier;
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

	/**
	 * @return unmodifiable list
	 */
	public List<Meta> getMetaElements()
	{
		if (metaElements == null)
			metaElements = new ArrayList<>();

		return Collections.unmodifiableList(metaElements);
	}

	public SuperClass getSuperClass()
	{
		return superClass;
	}

	/**
	 * @return unmodifiable list
	 */
	public List<SubClass> getSubClasses()
	{
		if (subClasses == null)
			subClasses = new ArrayList<>();

		return Collections.unmodifiableList(subClasses);
	}

	/**
	 * @return unmodifiable list
	 */
	public List<Rubric> getRubrics()
	{
		if (rubrics == null)
			rubrics = new ArrayList<>();

		return Collections.unmodifiableList(rubrics);
	}

	/**
	 * @param kind
	 * @return unmodifiable list, might be empty
	 */
	public List<Rubric> getRubricsByKind(RubricKind kind)
	{
		if (kind == null)
			return Collections.emptyList();

		List<Rubric> filteredList = getRubrics().stream().filter(r -> kind.equals(r.getKind()))
				.collect(Collectors.toList());

		return Collections.unmodifiableList(filteredList);
	}

	/**
	 * @return unmodifiable list
	 */
	public List<History> getHistoryElements()
	{
		if (historyElements == null)
			historyElements = new ArrayList<>();

		return Collections.unmodifiableList(historyElements);
	}
}

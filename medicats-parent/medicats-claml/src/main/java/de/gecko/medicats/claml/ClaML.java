package de.gecko.medicats.claml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "metaElements", "identifier", "title", "authors", "variants", "clamlClassKinds",
		"usageKinds", "rubricKinds", "modifier", "modifierClasses", "clamlClasses" })
@XmlRootElement(name = "ClaML")
public class ClaML
{
	@XmlAttribute(name = "version", required = true)
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String version;

	@XmlElement(name = "Meta")
	private List<Meta> metaElements;

	@XmlElement(name = "Identifier")
	private List<Identifier> identifier;

	@XmlElement(name = "Title", required = true)
	private Title title;

	@XmlElement(name = "Authors")
	private Authors authors;

	@XmlElement(name = "Variants")
	private Variants variants;

	@XmlElement(name = "ClassKinds", required = true)
	private ClaMLClassKinds clamlClassKinds;

	@XmlElement(name = "UsageKinds")
	private UsageKinds usageKinds;

	@XmlElement(name = "RubricKinds", required = true)
	private RubricKinds rubricKinds;

	@XmlElement(name = "Modifier")
	private List<Modifier> modifier;

	@XmlElement(name = "ModifierClass")
	private List<ModifierClass> modifierClasses;

	@XmlElement(name = "Class")
	private List<ClaMLClass> clamlClasses;

	public String getVersion()
	{
		return version;
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
	public List<Identifier> getIdentifier()
	{
		if (identifier == null)
			identifier = new ArrayList<>();

		return Collections.unmodifiableList(identifier);
	}

	public Title getTitle()
	{
		return title;
	}

	public Authors getAuthors()
	{
		return authors;
	}

	public Variants getVariants()
	{
		return variants;
	}

	public ClaMLClassKinds getClaMLClassKinds()
	{
		return clamlClassKinds;
	}

	public UsageKinds getUsageKinds()
	{
		return usageKinds;
	}

	public RubricKinds getRubricKinds()
	{
		return rubricKinds;
	}

	/**
	 * @return unmodifiable list
	 */
	public List<Modifier> getModifier()
	{
		if (modifier == null)
			modifier = new ArrayList<>();

		return Collections.unmodifiableList(modifier);
	}

	/**
	 * @return unmodifiable list
	 */
	public List<ModifierClass> getModifierClasses()
	{
		if (modifierClasses == null)
			modifierClasses = new ArrayList<>();

		return Collections.unmodifiableList(modifierClasses);
	}

	/**
	 * @return unmodifiable list
	 */
	public List<ClaMLClass> getClaMLClasses()
	{
		if (clamlClasses == null)
			clamlClasses = new ArrayList<>();

		return Collections.unmodifiableList(clamlClasses);
	}
}

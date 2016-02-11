package de.gecko.medicats.claml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "labels", "historyElements" })
@XmlRootElement(name = "Rubric")
public class Rubric
{
	@XmlAttribute(name = "id")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlID
	private String id;

	@XmlAttribute(name = "kind", required = true)
	@XmlIDREF
	private Object kind;

	@XmlAttribute(name = "usage")
	@XmlIDREF
	private Object usage;

	@XmlElement(name = "Label", required = true)
	private List<Label> labels;

	@XmlElement(name = "History")
	private List<History> historyElements;

	public String getId()
	{
		return id;
	}

	public Object getKind()
	{
		return kind;
	}

	public Object getUsage()
	{
		return usage;
	}

	/**
	 * @return unmodifiable list
	 */
	public List<Label> getLabels()
	{
		if (labels == null)
			labels = new ArrayList<>();

		return Collections.unmodifiableList(labels);
	}

	/**
	 * @return unmodifiable list
	 */
	public List<History> getHistory()
	{
		if (historyElements == null)
			historyElements = new ArrayList<>();

		return Collections.unmodifiableList(historyElements);
	}
}

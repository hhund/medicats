package de.gecko.medicats.claml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "displayElements" })
@XmlRootElement(name = "ClassKind")
public class ClaMLClassKind
{
	@XmlAttribute(name = "name", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlID
	private String name;

	@XmlElement(name = "Display")
	private List<Display> displayElements;

	public String getName()
	{
		return name;
	}

	/**
	 * @return unmodifiable list
	 */
	public List<Display> getDisplay()
	{
		if (displayElements == null)
			displayElements = new ArrayList<>();

		return Collections.unmodifiableList(displayElements);
	}
}

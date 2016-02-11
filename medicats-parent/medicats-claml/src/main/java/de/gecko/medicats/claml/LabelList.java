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
@XmlType(name = "", propOrder = { "listItems" })
@XmlRootElement(name = "List")
public class LabelList
{
	@XmlAttribute(name = "class")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String labelListClass;

	@XmlElement(name = "ListItem", required = true)
	private List<LabelListItem> listItems;

	public String getLabelListClass()
	{
		return labelListClass;
	}

	/**
	 * @return unmodifiable list
	 */
	public List<LabelListItem> getListItems()
	{
		if (listItems == null)
			listItems = new ArrayList<>();

		return Collections.unmodifiableList(listItems);
	}
}

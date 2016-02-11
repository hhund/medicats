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
@XmlType(name = "", propOrder = { "cells" })
@XmlRootElement(name = "Row")
public class Row
{
	@XmlAttribute(name = "class")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String rowClass;

	@XmlElement(name = "Cell")
	private List<Cell> cells;

	public String getRowClass()
	{
		return rowClass;
	}

	/**
	 * @return unmodifiable list
	 */
	public List<Cell> getCells()
	{
		if (cells == null)
			cells = new ArrayList<>();

		return Collections.unmodifiableList(cells);
	}
}

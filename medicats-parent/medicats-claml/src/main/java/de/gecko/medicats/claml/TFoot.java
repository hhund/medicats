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
@XmlType(name = "", propOrder = { "rows" })
@XmlRootElement(name = "TFoot")
public class TFoot
{
	@XmlAttribute(name = "class")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String footClass;

	@XmlElement(name = "Row", required = true)
	private List<Row> rows;

	public String getfootClass()
	{
		return footClass;
	}

	/**
	 * @return unmodifiable list
	 */
	public List<Row> getRows()
	{
		if (rows == null)
			rows = new ArrayList<>();

		return Collections.unmodifiableList(rows);
	}
}

package de.gecko.medicats.claml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "values" })
@XmlRootElement(name = "Cell")
public class Cell implements WithMixedValues
{
	@XmlAttribute(name = "class")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String cellClass;

	@XmlAttribute(name = "rowspan")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String rowspan;

	@XmlAttribute(name = "colspan")
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	private String colspan;

	@XmlElementRefs({ @XmlElementRef(name = "Reference", type = Reference.class),
			@XmlElementRef(name = "Term", type = Term.class), @XmlElementRef(name = "Para", type = Para.class),
			@XmlElementRef(name = "Include", type = Include.class),
			@XmlElementRef(name = "List", type = LabelList.class), @XmlElementRef(name = "Table", type = Table.class) })
	@XmlMixed
	private List<Object> values;

	public String getCellClass()
	{
		return cellClass;
	}

	public String getRowspan()
	{
		return rowspan;
	}

	public String getColspan()
	{
		return colspan;
	}

	@Override
	public List<Object> getValues()
	{
		if (values == null)
			values = new ArrayList<>();

		return values;
	}
}

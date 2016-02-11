package de.gecko.medicats.claml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "clamlClassKinds" })
@XmlRootElement(name = "ClassKinds")
public class ClaMLClassKinds
{
	@XmlElement(name = "ClassKind", required = true)
	private List<ClaMLClassKind> clamlClassKinds;

	/**
	 * @return unmodifiable list
	 */
	public List<ClaMLClassKind> getClaMLClassKinds()
	{
		if (clamlClassKinds == null)
			clamlClassKinds = new ArrayList<>();

		return Collections.unmodifiableList(clamlClassKinds);
	}
}

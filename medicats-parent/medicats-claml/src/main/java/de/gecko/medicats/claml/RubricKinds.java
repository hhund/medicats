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
@XmlType(name = "", propOrder = { "rubricKinds" })
@XmlRootElement(name = "RubricKinds")
public class RubricKinds
{
	@XmlElement(name = "RubricKind", required = true)
	private List<RubricKind> rubricKinds;

	/**
	 * @return unmodifiable list
	 */
	public List<RubricKind> getRubricKinds()
	{
		if (rubricKinds == null)
			rubricKinds = new ArrayList<>();

		return Collections.unmodifiableList(rubricKinds);
	}
}

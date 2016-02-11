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
@XmlType(name = "", propOrder = { "authors" })
@XmlRootElement(name = "Authors")
public class Authors
{
	@XmlElement(name = "Author")
	private List<Author> authors;

	/**
	 * @return unmodifiable list
	 */
	public List<Author> getAuthors()
	{
		if (authors == null)
			authors = new ArrayList<>();

		return Collections.unmodifiableList(authors);
	}
}

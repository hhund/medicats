package de.gecko.medicats.web.transfer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "dictionaries")
@XmlType(propOrder = { "dictionaries", "links" })
public class Dictionaries extends WithLinks
{
	@XmlElement(name = "dictionary")
	private final List<Dictionary> dictionaries = new ArrayList<>();

	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	Dictionaries()
	{
	}

	public Dictionaries(Collection<? extends Link> links, Collection<? extends Dictionary> dictionaries)
	{
		super(links);

		if (dictionaries != null)
			this.dictionaries.addAll(dictionaries);
	}

	public List<Dictionary> getDictionaries()
	{
		return Collections.unmodifiableList(dictionaries);
	}
}

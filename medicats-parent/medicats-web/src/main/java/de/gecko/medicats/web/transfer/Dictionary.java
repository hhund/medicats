package de.gecko.medicats.web.transfer;

import java.util.Collection;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "dictionary")
@XmlType(propOrder = { "name", "links" })
public class Dictionary extends WithLinks
{
	@XmlElement(name = "name")
	private final String name;

	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	Dictionary()
	{
		name = null;
	}

	public Dictionary(Collection<? extends Link> links, String name)
	{
		super(links);

		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}

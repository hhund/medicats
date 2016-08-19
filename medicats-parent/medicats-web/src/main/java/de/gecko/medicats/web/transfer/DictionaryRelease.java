package de.gecko.medicats.web.transfer;

import java.util.Collection;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "dictionary")
@XmlType(propOrder = { "name", "oid", "links" })
public class DictionaryRelease extends WithLinks
{
	@XmlElement(name = "name")
	private final String name;

	@XmlElement(name = "oid")
	private final String oid;

	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	DictionaryRelease()
	{
		name = null;
		oid = null;
	}

	public DictionaryRelease(Collection<? extends Link> links, String name, String oid)
	{
		super(links);

		this.name = name;
		this.oid = oid;
	}

	public String getName()
	{
		return name;
	}

	public String getOid()
	{
		return oid;
	}
}

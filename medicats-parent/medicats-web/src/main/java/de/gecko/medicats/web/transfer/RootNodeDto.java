package de.gecko.medicats.web.transfer;

import java.util.Collection;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "dictionary-release")
@XmlType(propOrder = { "name", "oid", "links" })
public class RootNodeDto extends WithLinks
{
	@XmlElement(name = "name")
	private final String name;

	@XmlElement(name = "oid")
	private final String oid;

	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	RootNodeDto()
	{
		name = null;
		oid = null;
	}

	public RootNodeDto(Collection<? extends Link> links, String name, String oid)
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

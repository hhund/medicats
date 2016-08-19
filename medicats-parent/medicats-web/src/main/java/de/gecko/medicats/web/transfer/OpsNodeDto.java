package de.gecko.medicats.web.transfer;

import java.util.Collection;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ops")
@XmlType(propOrder = { "code", "label", "type", "links" })
public class OpsNodeDto extends NodeDto
{
	@XmlElement(name = "type")
	private final String type;

	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	OpsNodeDto()
	{
		super();

		type = null;
	}

	public OpsNodeDto(Collection<? extends Link> links, String code, String name, String type)
	{
		super(links, code, name);

		this.type = type;
	}

	public String getType()
	{
		return type;
	}
}

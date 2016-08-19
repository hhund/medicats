package de.gecko.medicats.web.transfer;

import java.util.Collection;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "icd")
@XmlType(propOrder = { "code", "label", "type", "usage", "links" })
public class IcdNodeDto extends NodeDto
{
	@XmlElement(name = "type")
	private final String type;

	@XmlElement(name = "usage")
	private final String usage;

	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	IcdNodeDto()
	{
		super();

		type = null;
		usage = null;
	}

	public IcdNodeDto(Collection<? extends Link> links, String code, String name, String type, String usage)
	{
		super(links, code, name);

		this.type = type;
		this.usage = usage;
	}

	public String getType()
	{
		return type;
	}

	public String getUsage()
	{
		return usage;
	}
}

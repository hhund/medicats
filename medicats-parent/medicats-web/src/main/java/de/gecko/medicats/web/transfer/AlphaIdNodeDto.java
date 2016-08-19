package de.gecko.medicats.web.transfer;

import java.util.Collection;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "alpha-id")
@XmlType(propOrder = { "code", "label", "links" })
public class AlphaIdNodeDto extends NodeDto
{
	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	AlphaIdNodeDto()
	{
		super();
	}

	public AlphaIdNodeDto(Collection<? extends Link> links, String code, String name)
	{
		super(links, code, name);
	}
}

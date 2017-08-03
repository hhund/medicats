package de.gecko.medicats.web.transfer;

import java.util.Collection;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "alpha-id")
@XmlType(propOrder = { "oid", "dictionary", "code", "label", "links" })
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

	public AlphaIdNodeDto(Collection<? extends Link> links, String oid, String dictionary, String code, String name)
	{
		super(links, oid, dictionary, code, name);
	}
}

package de.gecko.medicats.web.transfer;

import java.util.Collection;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class NodeDto extends WithLinks
{
	@XmlElement(name = "code")
	private final String code;

	@XmlElement(name = "label")
	private final String label;

	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	NodeDto()
	{
		code = null;
		label = null;
	}

	protected NodeDto(Collection<? extends Link> links, String code, String label)
	{
		super(links);

		this.code = code;
		this.label = label;
	}

	public String getCode()
	{
		return code;
	}

	public String getLabel()
	{
		return label;
	}
}

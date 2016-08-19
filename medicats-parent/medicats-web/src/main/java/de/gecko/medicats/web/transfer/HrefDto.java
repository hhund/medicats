package de.gecko.medicats.web.transfer;

import javax.xml.bind.annotation.XmlAttribute;

public class HrefDto
{
	@XmlAttribute(name = "href")
	private final String href;

	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	HrefDto()
	{
		href = null;
	}

	public HrefDto(String href)
	{
		this.href = href;
	}

	public String getHref()
	{
		return href;
	}

	@Override
	public String toString()
	{
		return href;
	}
}

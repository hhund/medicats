package de.gecko.medicats.web.transfer;

import java.util.Collection;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class NodeDto extends WithLinks
{
	@XmlElement(name = "oid")
	private final String oid;

	@XmlElement(name = "dictionary")
	private final String dictionary;

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
		oid = null;
		dictionary = null;
		code = null;
		label = null;
	}

	protected NodeDto(Collection<? extends Link> links, String oid, String dictionary, String code, String label)
	{
		super(links);

		this.oid = oid;
		this.dictionary = dictionary;
		this.code = code;
		this.label = label;
	}

	public String getOid()
	{
		return oid;
	}

	public String getDictionary()
	{
		return dictionary;
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

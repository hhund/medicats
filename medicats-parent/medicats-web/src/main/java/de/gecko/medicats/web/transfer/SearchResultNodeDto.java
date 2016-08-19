package de.gecko.medicats.web.transfer;

import java.util.Collection;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = { "oid", "dictionary", "version", "label", "code", "links" })
public class SearchResultNodeDto extends WithLinks
{
	@XmlElement(name = "oid")
	private final String oid;

	@XmlElement(name = "dictionary")
	private final String dictionary;

	@XmlElement(name = "version")
	private final String version;

	@XmlElement(name = "label")
	private final String label;

	@XmlElement(name = "code")
	private final String code;

	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	SearchResultNodeDto()
	{
		oid = null;
		dictionary = null;
		version = null;
		label = null;
		code = null;
	}

	public SearchResultNodeDto(Collection<? extends Link> links, String oid, String dictionary, String version,
			String label, String code)
	{
		super(links);

		this.oid = oid;
		this.dictionary = dictionary;
		this.version = version;
		this.label = label;
		this.code = code;
	}

	public String getOid()
	{
		return oid;
	}

	public String getDictionary()
	{
		return dictionary;
	}

	public String getVersion()
	{
		return version;
	}

	public String getLabel()
	{
		return label;
	}

	public String getCode()
	{
		return code;
	}
}

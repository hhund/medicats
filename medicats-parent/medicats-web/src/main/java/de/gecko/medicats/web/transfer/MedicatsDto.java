package de.gecko.medicats.web.transfer;

import java.util.Collection;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "medicats")
@XmlType(propOrder = { "copyrightEn", "copyrightDe", "links" })
public class MedicatsDto extends WithLinks
{
	@XmlElement(name = "copyright-en")
	private final String copyrightEn;

	@XmlElement(name = "copyright-de")
	private final String copyrightDe;

	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	MedicatsDto()
	{
		copyrightEn = null;
		copyrightDe = null;
	}

	public MedicatsDto(String copyrightEn, String copyrightDe, Collection<? extends Link> links)
	{
		super(links);

		this.copyrightEn = copyrightEn;
		this.copyrightDe = copyrightDe;
	}
}

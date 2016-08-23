package de.gecko.medicats.web.transfer;

import java.util.Collection;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "medicats")
public class MedicatsDto extends WithLinks
{
	/**
	 *@deprecated only for JAX-B 
	 */
	@Deprecated
	MedicatsDto()
	{
	}
	
	public MedicatsDto(Collection<? extends Link> links)
	{
		super(links);
	}
}

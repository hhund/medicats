package de.gecko.medicats.web.transfer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlTransient
public class WithLinks
{
	@XmlElementWrapper(name = "links")
	@XmlElement(name = "link")
	@XmlJavaTypeAdapter(Link.JaxbAdapter.class)
	@JsonProperty("link")
	private final List<Link> links = new ArrayList<>();

	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	WithLinks()
	{
	}

	public WithLinks(Collection<? extends Link> links)
	{
		if (links != null)
			links.stream().filter(n -> n != null).forEach(n -> this.links.add(n));

	}

	public List<Link> getLinks()
	{
		return Collections.unmodifiableList(links);
	}
}

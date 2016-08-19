package de.gecko.medicats.web.transfer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "dictionary-releases")
@XmlType(propOrder = { "dictionaryRelease", "links" })
public class DictionaryReleases extends WithLinks
{
	@XmlElement(name = "dictionary-release")
	private final List<DictionaryRelease> dictionaryRelease = new ArrayList<>();

	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	DictionaryReleases()
	{
	}
	
	

	public DictionaryReleases(Collection<? extends Link> links, Collection<? extends DictionaryRelease> dictionaries)
	{
		super(links);
		
		if (dictionaries != null)
			this.dictionaryRelease.addAll(dictionaries);
	}

	public List<DictionaryRelease> getDictionaryReleases()
	{
		return Collections.unmodifiableList(dictionaryRelease);
	}
}

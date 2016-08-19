package de.gecko.medicats.web.transfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "search-result")
public class SearchResultNodeListDto
{
	@XmlElement(name = "result")
	private final List<SearchResultNodeDto> results = new ArrayList<>();

	/**
	 * @deprecated only for JAX-B
	 */
	@Deprecated
	SearchResultNodeListDto()
	{
	}

	public SearchResultNodeListDto(List<SearchResultNodeDto> results)
	{
		if (results != null)
			this.results.addAll(results);
	}

	public List<SearchResultNodeDto> getResults()
	{
		return Collections.unmodifiableList(results);
	}
}

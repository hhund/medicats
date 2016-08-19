package de.gecko.medicats.web.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.gecko.medicats.web.transfer.Dictionaries;
import de.gecko.medicats.web.transfer.Dictionary;

@Path("dictionary")
public class DictionaryWebservice
{
	public static final String PATH = "dictionary";

	private final String baseUrl;
	private final List<Dictionary> dictionaries = new ArrayList<>();

	public DictionaryWebservice(String baseUrl)
	{
		this.baseUrl = baseUrl;
		dictionaries.add(new Dictionary(Collections.singleton(Link.fromUri(baseUrl + "/" + PATH + "/alpha-id").build()),
				"Alpha-ID"));
		dictionaries.add(new Dictionary(
				Collections.singleton(Link.fromUri(baseUrl + "/" + PATH + "/icd-10-gm").build()), "ICD-10-GM"));
		dictionaries
				.add(new Dictionary(Collections.singleton(Link.fromUri(baseUrl + "/" + PATH + "/ops").build()), "OPS"));
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getVocabularies()
	{
		return Response
				.ok(new Dictionaries(Collections.singleton(Link.fromUri(baseUrl + "/" + PATH).rel("self").build()), dictionaries))
				.build();
	}
}

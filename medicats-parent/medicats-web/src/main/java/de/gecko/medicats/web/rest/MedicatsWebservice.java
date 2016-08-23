package de.gecko.medicats.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;

import de.gecko.medicats.web.transfer.MedicatsDto;

@Path(MedicatsWebservice.PATH)
public class MedicatsWebservice
{
	public static final String PATH = "/";

	private final List<Link> links = new ArrayList<>();

	public MedicatsWebservice(String baseUrl)
	{
		links.add(Link.fromUri(baseUrl + "/" + OidDictionaryWebservice.PATH).rel("resource").type("oid")
				.title("Dictionaries by OID").build());
		links.add(Link.fromUri(baseUrl + "/" + DictionaryWebservice.PATH).rel("resource").type("dictionary")
				.title("Dictionaries by Name").build());
		links.add(Link.fromUri(baseUrl + "/" + SearchWebservice.PATH + "?q=").rel("resource")
				.title("Nodes by full text query").build());
	}

	@GET
	public Response root()
	{
		return Response.ok(new MedicatsDto(links)).build();
	}
}

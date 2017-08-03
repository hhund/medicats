package de.gecko.medicats.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.gecko.medicats.web.transfer.MedicatsDto;

@Path(MedicatsWebservice.PATH)
public class MedicatsWebservice
{
	public static final String PATH = "/";

	private final List<Link> links = new ArrayList<>();
	private final MedicatsDto indexDto;

	private final XsltTransformer transformer;

	public MedicatsWebservice(String baseUrl, XsltTransformer transformer)
	{
		links.add(Link.fromUri(baseUrl + "/" + OidDictionaryWebservice.PATH).rel("resource").type("oid")
				.title("Dictionaries by OID").build());
		links.add(Link.fromUri(baseUrl + "/" + DictionaryWebservice.PATH).rel("resource").type("dictionary")
				.title("Dictionaries by Name").build());
		links.add(Link.fromUri(baseUrl + "/" + SearchWebservice.PATH + "?q=").rel("resource")
				.title("Search dictionary entries by full text query").build());

		indexDto = new MedicatsDto(
				"Source for ICD-10-GM, OPS and Alpha-ID catalogs: Deutschen Instituts für Medizinische Dokumentation und Information (DIMDI) http://www.dimdi.de.",
				"Die Erstellung erfolgt unter Verwendung der maschinenlesbaren Fassung des Deutschen Instituts für Medizinische Dokumentation und Information (DIMDI). Quelle für ICD-10-GM, OPS und Alpha-ID Kataloge: http://www.dimdi.de.",
				links);

		this.transformer = transformer;
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response root()
	{
		return Response.ok(indexDto).build();
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response rootHtml()
	{
		return Response.ok(transformer.transform(indexDto, "MedicatsDto.xslt")).build();
	}
}

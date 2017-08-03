package de.gecko.medicats.web.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Link.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import de.gecko.medicats.CodeService;
import de.gecko.medicats.NodeWalker;
import de.gecko.medicats.VersionedNodeFactory;
import de.gecko.medicats.web.transfer.DictionaryRelease;
import de.gecko.medicats.web.transfer.DictionaryReleases;
import de.gecko.medicats.web.transfer.NodeDto;
import de.gecko.medicats.web.transfer.RootNodeDto;

public abstract class AbstractDictionaryWebservice<N extends de.gecko.medicats.VersionedNode<N>, W extends NodeWalker<N>, F extends VersionedNodeFactory<N, W>, S extends CodeService<N, W, F>>
		implements InitializingBean
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractDictionaryWebservice.class);

	private final String baseUrl;
	private final String vocabulary;

	private final Map<String, F> factoriesByUrl;

	private final XsltTransformer transformer;

	public AbstractDictionaryWebservice(S service, String baseUrl, String vocabulary, XsltTransformer transformer)
	{
		this.baseUrl = baseUrl;
		this.vocabulary = vocabulary;

		factoriesByUrl = toFactoriesByUrl(service);

		this.transformer = transformer;
	}

	private Map<String, F> toFactoriesByUrl(S service)
	{
		return service.getNodeFactories().stream().sorted(Comparator.comparing(VersionedNodeFactory::getSortIndex))
				.collect(Collectors.toMap(f -> String.valueOf(f.getSortIndex()), Function.identity()));
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		Objects.requireNonNull(baseUrl, "baseUrl");
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getVocabularyReleases()
	{
		logger.trace("GET '/" + Objects.toString(vocabulary) + "'");

		DictionaryReleases indexDto = getIndexDto();

		return Response.ok(indexDto).build();
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response getVocabularyReleasesHtml()
	{
		logger.trace("GET '/" + Objects.toString(vocabulary) + "'");

		DictionaryReleases indexDto = getIndexDto();

		return Response.ok(transformer.transform(indexDto, "DictionaryReleases.xslt")).build();
	}

	private DictionaryReleases getIndexDto()
	{
		List<DictionaryRelease> vocabularyReleases = factoriesByUrl.entrySet().stream()
				.map(e -> e
						.getValue())
				.sorted(Comparator
						.comparing(
								VersionedNodeFactory::getSortIndex))
				.map(f -> new DictionaryRelease(
						Collections.singleton(toLink("dictionary-release", "dictionary", f.getName(),
								new String[] { baseUrl, vocabulary, String.valueOf(f.getSortIndex()) })),
						f.getName(), f.getOid()))
				.collect(Collectors.toList());

		DictionaryReleases indexDto = new DictionaryReleases(
				Collections.singleton(Link.fromUri(baseUrl + "/" + vocabulary).rel("self").build()),
				vocabularyReleases);
		return indexDto;
	}

	@GET
	@Path("{vocabulary-release}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getVocabularyRelease(@PathParam("vocabulary-release") String vocabularyRelease)
	{
		logger.trace("GET '/" + Objects.toString(vocabulary) + "/" + Objects.toString(vocabularyRelease) + "'");
		if (!factoriesByUrl.containsKey(vocabularyRelease))
			return Response.status(Status.NOT_FOUND).build();

		RootNodeDto rootNode = getRootNode(vocabularyRelease);

		return Response.ok(rootNode).build();
	}

	@GET
	@Path("{vocabulary-release}")
	@Produces(MediaType.TEXT_HTML)
	public Response getVocabularyReleaseHtml(@PathParam("vocabulary-release") String vocabularyRelease)
	{
		logger.trace("GET '/" + Objects.toString(vocabulary) + "/" + Objects.toString(vocabularyRelease) + "'");
		if (!factoriesByUrl.containsKey(vocabularyRelease))
			return Response.status(Status.NOT_FOUND).build();

		RootNodeDto rootNode = getRootNode(vocabularyRelease);

		return Response.ok(transformer.transform(rootNode, "RootNodeDto.xslt")).build();
	}

	private RootNodeDto getRootNode(String vocabularyRelease)
	{
		F nodeFactory = factoriesByUrl.get(vocabularyRelease);

		N root = nodeFactory.getRootNode();
		List<Link> childrenHrefs = root.children()
				.map(n -> Link.fromUri(baseUrl + "/" + vocabulary + "/" + vocabularyRelease + n.getUri()).rel("child")
						.title(n.getCode() + " (" + n.getLabel() + ")").type("dictionary").build())
				.collect(Collectors.toList());

		List<Link> links = new ArrayList<>();
		links.add(Link.fromUri(baseUrl + "/" + vocabulary + "/" + vocabularyRelease).rel("self")
				.title(nodeFactory.getName()).build());
		links.addAll(childrenHrefs);

		RootNodeDto rootNode = new RootNodeDto(links, nodeFactory.getName(), nodeFactory.getOid());
		return rootNode;
	}

	@GET
	@Path("{vocabulary-release}/{p1}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getNode(@PathParam("vocabulary-release") String vocabularyRelease, @PathParam("p1") String p1)
	{
		logger.trace(
				"GET '/" + vocabulary + "/" + Objects.toString(vocabularyRelease) + "/" + Objects.toString(p1) + "'");

		return Response.ok(getNodeDto(vocabularyRelease, p1)).build();
	}

	@GET
	@Path("{vocabulary-release}/{p1}")
	@Produces(MediaType.TEXT_HTML)
	public Response getNodeHtml(@PathParam("vocabulary-release") String vocabularyRelease, @PathParam("p1") String p1)
	{
		logger.trace(
				"GET '/" + vocabulary + "/" + Objects.toString(vocabularyRelease) + "/" + Objects.toString(p1) + "'");

		return Response.ok(transformer.transform(getNodeDto(vocabularyRelease, p1), "NodeDto.xslt")).build();
	}

	@GET
	@Path("{vocabulary-release}/{p1}/{p2}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getNode(@PathParam("vocabulary-release") String vocabularyRelease, @PathParam("p1") String p1,
			@PathParam("p2") String p2)
	{
		logger.trace("GET '/" + vocabulary + "/" + Objects.toString(vocabularyRelease) + "/" + Objects.toString(p1)
				+ "/" + Objects.toString(p2) + "'");

		return Response.ok(getNodeDto(vocabularyRelease, p1, p2)).build();
	}

	@GET
	@Path("{vocabulary-release}/{p1}/{p2}")
	@Produces(MediaType.TEXT_HTML)
	public Response getNodeHtml(@PathParam("vocabulary-release") String vocabularyRelease, @PathParam("p1") String p1,
			@PathParam("p2") String p2)
	{
		logger.trace("GET '/" + vocabulary + "/" + Objects.toString(vocabularyRelease) + "/" + Objects.toString(p1)
				+ "/" + Objects.toString(p2) + "'");

		return Response.ok(transformer.transform(getNodeDto(vocabularyRelease, p1, p2), "NodeDto.xslt")).build();
	}

	@GET
	@Path("{vocabulary-release}/{p1}/{p2}/{p3}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getNodes(@PathParam("vocabulary-release") String vocabularyRelease, @PathParam("p1") String p1,
			@PathParam("p2") String p2, @PathParam("p3") String p3)
	{
		logger.trace("GET '/" + vocabulary + "/" + Objects.toString(vocabularyRelease) + "/" + Objects.toString(p1)
				+ "/" + Objects.toString(p2) + "/" + Objects.toString(p3) + "'");

		return Response.ok(getNodeDto(vocabularyRelease, p1, p2, p3)).build();
	}

	@GET
	@Path("{vocabulary-release}/{p1}/{p2}/{p3}")
	@Produces(MediaType.TEXT_HTML)
	public Response getNodesHtml(@PathParam("vocabulary-release") String vocabularyRelease, @PathParam("p1") String p1,
			@PathParam("p2") String p2, @PathParam("p3") String p3)
	{
		logger.trace("GET '/" + vocabulary + "/" + Objects.toString(vocabularyRelease) + "/" + Objects.toString(p1)
				+ "/" + Objects.toString(p2) + "/" + Objects.toString(p3) + "'");

		return Response.ok(transformer.transform(getNodeDto(vocabularyRelease, p1, p2, p3), "NodeDto.xslt")).build();
	}

	@GET
	@Path("{vocabulary-release}/{p1}/{p2}/{p3}/{p4}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getNodes(@PathParam("vocabulary-release") String vocabularyRelease, @PathParam("p1") String p1,
			@PathParam("p2") String p2, @PathParam("p3") String p3, @PathParam("p4") String p4)
	{
		logger.trace("GET '/" + vocabulary + "/" + Objects.toString(vocabularyRelease) + "/" + Objects.toString(p1)
				+ "/" + Objects.toString(p2) + "/" + Objects.toString(p3) + "/" + Objects.toString(p4) + "'");

		return Response.ok(getNodeDto(vocabularyRelease, p1, p2, p3, p4)).build();
	}

	@GET
	@Path("{vocabulary-release}/{p1}/{p2}/{p3}/{p4}")
	@Produces(MediaType.TEXT_HTML)
	public Response getNodesHtml(@PathParam("vocabulary-release") String vocabularyRelease, @PathParam("p1") String p1,
			@PathParam("p2") String p2, @PathParam("p3") String p3, @PathParam("p4") String p4)
	{
		logger.trace("GET '/" + vocabulary + "/" + Objects.toString(vocabularyRelease) + "/" + Objects.toString(p1)
				+ "/" + Objects.toString(p2) + "/" + Objects.toString(p3) + "/" + Objects.toString(p4) + "'");

		return Response.ok(transformer.transform(getNodeDto(vocabularyRelease, p1, p2, p3, p4), "NodeDto.xslt"))
				.build();
	}

	@GET
	@Path("{vocabulary-release}/{p1}/{p2}/{p3}/{p4}/{p5}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getNode(@PathParam("vocabulary-release") String vocabularyRelease, @PathParam("p1") String p1,
			@PathParam("p2") String p2, @PathParam("p3") String p3, @PathParam("p4") String p4,
			@PathParam("p5") String p5)
	{
		logger.trace("GET '/" + vocabulary + "/" + Objects.toString(vocabularyRelease) + "/" + Objects.toString(p1)
				+ "/" + Objects.toString(p2) + "/" + Objects.toString(p3) + "/" + Objects.toString(p4) + "/"
				+ Objects.toString(p5) + "'");

		return Response.ok(getNodeDto(vocabularyRelease, p1, p2, p3, p4, p5)).build();
	}

	@GET
	@Path("{vocabulary-release}/{p1}/{p2}/{p3}/{p4}/{p5}")
	@Produces(MediaType.TEXT_HTML)
	public Response getNodeHtml(@PathParam("vocabulary-release") String vocabularyRelease, @PathParam("p1") String p1,
			@PathParam("p2") String p2, @PathParam("p3") String p3, @PathParam("p4") String p4,
			@PathParam("p5") String p5)
	{
		logger.trace("GET '/" + vocabulary + "/" + Objects.toString(vocabularyRelease) + "/" + Objects.toString(p1)
				+ "/" + Objects.toString(p2) + "/" + Objects.toString(p3) + "/" + Objects.toString(p4) + "/"
				+ Objects.toString(p5) + "'");

		return Response.ok(transformer.transform(getNodeDto(vocabularyRelease, p1, p2, p3, p4, p5), "NodeDto.xslt"))
				.build();
	}

	@GET
	@Path("{vocabulary-release}/{p1}/{p2}/{p3}/{p4}/{p5}/{p6}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getNode(@PathParam("vocabulary-release") String vocabularyRelease, @PathParam("p1") String p1,
			@PathParam("p2") String p2, @PathParam("p3") String p3, @PathParam("p4") String p4,
			@PathParam("p5") String p5, @PathParam("p6") String p6)
	{
		logger.trace("GET '/" + vocabulary + "/" + Objects.toString(vocabularyRelease) + "/" + Objects.toString(p1)
				+ "/" + Objects.toString(p2) + "/" + Objects.toString(p3) + "/" + Objects.toString(p4) + "/"
				+ Objects.toString(p5) + "/" + Objects.toString(p6) + "'");

		return Response.ok(getNodeDto(vocabularyRelease, p1, p2, p3, p4, p5, p6)).build();
	}

	@GET
	@Path("{vocabulary-release}/{p1}/{p2}/{p3}/{p4}/{p5}/{p6}")
	@Produces(MediaType.TEXT_HTML)
	public Response getNodeHtml(@PathParam("vocabulary-release") String vocabularyRelease, @PathParam("p1") String p1,
			@PathParam("p2") String p2, @PathParam("p3") String p3, @PathParam("p4") String p4,
			@PathParam("p5") String p5, @PathParam("p6") String p6)
	{
		logger.trace("GET '/" + vocabulary + "/" + Objects.toString(vocabularyRelease) + "/" + Objects.toString(p1)
				+ "/" + Objects.toString(p2) + "/" + Objects.toString(p3) + "/" + Objects.toString(p4) + "/"
				+ Objects.toString(p5) + "/" + Objects.toString(p6) + "'");

		return Response.ok(transformer.transform(getNodeDto(vocabularyRelease, p1, p2, p3, p4, p5, p6), "NodeDto.xslt"))
				.build();
	}

	NodeDto getNodeDto(String vocabularyRelease, String... p)
	{
		if (!factoriesByUrl.containsKey(vocabularyRelease))
			throw new WebApplicationException(Status.NOT_FOUND);

		F nodeFactory = factoriesByUrl.get(vocabularyRelease);

		N startNode = nodeFactory.createNodeWalker().getNodeByCode(p[p.length - 1]);
		if (startNode == null)
			throw new WebApplicationException(Status.NOT_FOUND);

		if (!startNode.getPath()
				.substring(nodeFactory.getRootNode().getPath().length() + 1, startNode.getPath().length())
				.equals(toPath(p)))
			throw new WebApplicationException(Status.NOT_FOUND);

		List<Link> childrenHrefs = startNode.children()
				.map(n -> Link.fromUri(baseUrl + "/" + vocabulary + "/" + vocabularyRelease + n.getUri()).rel("child")
						.title(n.getCode() + " (" + n.getLabel() + ")").type("dictionary").build())
				.collect(Collectors.toList());

		return toDto(nodeFactory, startNode, baseUrl, vocabulary, vocabularyRelease, p, childrenHrefs);
	}

	protected abstract NodeDto toDto(F nodeFactory, N node, String baseUrl, String vocabulary, String vocabularyRelease,
			String[] p, List<Link> childrenHrefs);

	protected final List<Link> toLinks(String rel, String type, String vocabularyRelease, N root, Stream<N> nodes)
	{
		int rootNodePathLength = root.getPath().length();
		return nodes
				.map(n -> Link
						.fromUri(baseUrl + "/" + vocabulary + "/" + vocabularyRelease
								+ n.getPath().substring(rootNodePathLength, n.getPath().length()))
						.rel(rel).title(n.getCode() + " (" + n.getLabel() + ")").type(type).build())
				.collect(Collectors.toList());
	}

	protected final Link toLink(String rel, String type, String vocabularyRelease, N root, N node)
	{
		int rootNodePathLength = root.getPath().length();
		return Link
				.fromUri(baseUrl + "/" + vocabulary + "/" + vocabularyRelease
						+ node.getPath().substring(rootNodePathLength, node.getPath().length()))
				.rel(rel).title(node.getCode() + " (" + node.getLabel() + ")").type(type).build();
	}

	private Link toLink(String rel, String type, String label, String[] values)
	{
		return toLink(rel, type, label, Arrays.asList(values).stream());
	}

	protected final Link toLink(String rel, String type, String label, Stream<String> values)
	{
		Builder l = Link.fromUri(values.collect(Collectors.joining("/"))).rel(rel);

		if (label != null)
			l = l.title(label);

		return l.type(type).build();
	}

	private String toPath(String... values)
	{
		return Arrays.stream(values).collect(Collectors.joining("/"));
	}
}

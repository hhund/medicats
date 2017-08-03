package de.gecko.medicats.web.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gecko.medicats.Node;
import de.gecko.medicats.NodeFactory;
import de.gecko.medicats.VersionedNode;
import de.gecko.medicats.VersionedNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNode;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdService;
import de.gecko.medicats.icd10.IcdNode;
import de.gecko.medicats.icd10.IcdNode.IcdNodeType;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.IcdService;
import de.gecko.medicats.ops.OpsNode;
import de.gecko.medicats.ops.OpsNode.OpsNodeType;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.OpsService;
import de.gecko.medicats.web.transfer.AlphaIdNodeDto;
import de.gecko.medicats.web.transfer.DictionaryRelease;
import de.gecko.medicats.web.transfer.DictionaryReleases;
import de.gecko.medicats.web.transfer.IcdNodeDto;
import de.gecko.medicats.web.transfer.NodeDto;
import de.gecko.medicats.web.transfer.OpsNodeDto;
import de.gecko.medicats.web.transfer.RootNodeDto;
import de.gecko.medicats.web.transfer.StringConverter;

@Path(OidDictionaryWebservice.PATH)
public class OidDictionaryWebservice
{
	public static final String PATH = "oid";

	private static final Logger logger = LoggerFactory.getLogger(OidDictionaryWebservice.class);

	private final String baseUrl;
	private final Map<String, VersionedNodeFactory<?, ?>> factories = new HashMap<>();

	private final List<DictionaryRelease> dictionaries;
	private final DictionaryReleases indexDto;

	private final IcdService icdService;
	private final OpsService opsService;
	private final AlphaIdService alphaIdService;

	private final XsltTransformer transformer;

	public OidDictionaryWebservice(String baseUrl, IcdService icdService, OpsService opsService,
			AlphaIdService alphaIdService, XsltTransformer transformer)
	{
		this.baseUrl = baseUrl;
		this.icdService = icdService;
		this.opsService = opsService;
		this.alphaIdService = alphaIdService;
		this.transformer = transformer;

		icdService.getNodeFactories().stream().filter(f -> f.getOid() != null && !f.getOid().isEmpty())
				.collect(Collectors.toMap(NodeFactory::getOid, Function.identity(), (a, b) -> a, () -> factories));
		opsService.getNodeFactories().stream().filter(f -> f.getOid() != null && !f.getOid().isEmpty())
				.collect(Collectors.toMap(NodeFactory::getOid, Function.identity(), (a, b) -> a, () -> factories));
		alphaIdService.getNodeFactories().stream().filter(f -> f.getOid() != null && !f.getOid().isEmpty())
				.collect(Collectors.toMap(NodeFactory::getOid, Function.identity(), (a, b) -> a, () -> factories));

		dictionaries = factories.values().stream()
				.sorted(Comparator.comparing(VersionedNodeFactory::getOid)).map(
						f -> new DictionaryRelease(
								Collections.singleton(Link.fromUri(baseUrl + "/" + PATH + "/" + f.getOid())
										.rel("resource").title(f.getName()).type("oid").build()),
								f.getName(), f.getOid()))
				.collect(Collectors.toList());

		indexDto = new DictionaryReleases(Collections.singleton(Link.fromUri(baseUrl + "/" + PATH).build()),
				dictionaries);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAll()
	{
		logger.trace("GET '/" + PATH + "'");

		return Response.ok(indexDto).build();
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response getAllHtml()
	{
		logger.trace("GET '/" + PATH + "'");

		return Response.ok(transformer.transform(indexDto, "DictionaryReleases.xslt")).build();
	}

	@GET
	@Path("{oid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getOid(@PathParam("oid") String oid)
	{
		logger.trace("GET '/" + PATH + "/" + Objects.toString(oid) + "'");

		if (!factories.containsKey(oid))
			return Response.status(Status.NOT_FOUND).build();

		RootNodeDto dto = getMyOid(oid);

		return Response.ok(dto).build();
	}

	@GET
	@Path("{oid}")
	@Produces(MediaType.TEXT_HTML)
	public Response getOidHtml(@PathParam("oid") String oid)
	{
		logger.trace("GET '/" + PATH + "/" + Objects.toString(oid) + "'");

		if (!factories.containsKey(oid))
			return Response.status(Status.NOT_FOUND).build();

		RootNodeDto dto = getMyOid(oid);

		return Response.ok(transformer.transform(dto, "RootNodeDto.xslt")).build();
	}

	private RootNodeDto getMyOid(String oid)
	{
		VersionedNodeFactory<?, ?> f = factories.get(oid);

		List<Link> children = f.createNodeWalker().preOrderStream().filter(this::selectNode)
				.map(n -> Link.fromUri(baseUrl + "/" + PATH + "/" + oid + "/" + n.getCode()).rel("child")
						.title(n.getCode() + " (" + n.getLabel() + ")").type("oid").build())
				.collect(Collectors.toList());

		Link self = Link.fromUri(baseUrl + "/" + PATH + "/" + oid).rel("self").title(f.getName()).type("oid").build();

		List<Link> links = new ArrayList<>();
		links.add(self);
		links.addAll(children);

		RootNodeDto dto = new RootNodeDto(links, f.getName(), f.getOid());
		return dto;
	}

	private boolean selectNode(Object node)
	{
		if (node instanceof IcdNode)
			return IcdNodeType.CATEGORY.equals(((IcdNode) node).getNodeType());
		else if (node instanceof OpsNode)
			return OpsNodeType.CATEGORY.equals(((OpsNode) node).getNodeType());
		else if (node instanceof AlphaIdNode)
			return !((AlphaIdNode) node).isRoot();
		else
			return false;
	}

	@GET
	@Path("{oid}/{code}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getOid(@PathParam("oid") String oid, @PathParam("code") String code)
	{
		logger.trace("GET '/" + PATH + "/" + Objects.toString(oid) + "/" + Objects.toString(code) + "'");

		if (!factories.containsKey(oid))
			return Response.status(Status.NOT_FOUND).build();

		VersionedNodeFactory<?, ?> factory = factories.get(oid);

		VersionedNode<?> node = factory.createNodeWalker().getNodeByCode(code);

		if (node == null || !selectNode(node))
			return Response.status(Status.NOT_FOUND).build();

		NodeDto dto = toDto(oid, factory, node);

		return Response.ok(dto).build();
	}

	@GET
	@Path("{oid}/{code}")
	@Produces(MediaType.TEXT_HTML)
	public Response getOidHtml(@PathParam("oid") String oid, @PathParam("code") String code)
	{
		logger.trace("GET '/" + PATH + "/" + Objects.toString(oid) + "/" + Objects.toString(code) + "'");

		if (!factories.containsKey(oid))
			return Response.status(Status.NOT_FOUND).build();

		VersionedNodeFactory<?, ?> factory = factories.get(oid);

		VersionedNode<?> node = factory.createNodeWalker().getNodeByCode(code);

		if (node == null || !selectNode(node))
			return Response.status(Status.NOT_FOUND).build();

		NodeDto dto = toDto(oid, factory, node);

		return Response.ok(transformer.transform(dto, "NodeDto.xslt")).build();
	}

	private NodeDto toDto(String oid, VersionedNodeFactory<?, ?> factory, VersionedNode<?> node)
	{
		Link self = Link.fromUri(baseUrl + "/" + PATH + "/" + oid + "/" + node.getCode()).rel("self")
				.title(node.getCode() + " (" + node.getLabel() + ")").type("oid").build();

		String altPath;
		if (factory instanceof IcdNodeFactory)
			altPath = baseUrl + "/" + IcdDictionaryWebservice.PATH + "/" + factory.getSortIndex();
		else if (factory instanceof OpsNodeFactory)
			altPath = baseUrl + "/" + OpsDictionaryWebservice.PATH + "/" + factory.getSortIndex();
		else if (factory instanceof AlphaIdNodeFactory)
			altPath = baseUrl + "/" + AlphaIdDictionaryWebservice.PATH + "/" + factory.getSortIndex();
		else
			throw new WebApplicationException(Status.NOT_FOUND);

		Link alt = Link.fromUri(altPath + node.getUri()).rel("alternate")
				.title(node.getCode() + " (" + node.getLabel() + ")").type("dictionary").build();

		Link previous = null;
		if (node.getPrevious().isPresent())
		{
			Node<?> previousNode = node.getPrevious().get();
			NodeFactory<?, ?> previousNodeFactory;

			if (factory instanceof IcdNodeFactory)
				previousNodeFactory = icdService.getNodeFactory(factory.getPreviousVersion());
			else if (factory instanceof OpsNodeFactory)
				previousNodeFactory = opsService.getNodeFactory(factory.getPreviousVersion());
			else if (factory instanceof AlphaIdNodeFactory)
				previousNodeFactory = alphaIdService.getNodeFactory(factory.getPreviousVersion());
			else
				throw new WebApplicationException(Status.NOT_FOUND);

			if (previousNodeFactory != null && previousNodeFactory.getOid() != null
					&& !previousNodeFactory.getOid().isEmpty())
				previous = Link.fromUri(baseUrl + "/oid/" + previousNodeFactory.getOid() + "/" + previousNode.getCode())
						.rel("previous").title(previousNode.getCode() + " (" + previousNode.getLabel() + ")")
						.type("oid").build();
		}

		Link parent;
		if (node.getParent() == null)
			parent = null;
		else if (node instanceof AlphaIdNode)
			parent = Link.fromUri(baseUrl + "/" + PATH + "/" + oid).rel("parent").title(factory.getName()).type("oid")
					.build();
		else if (!selectNode(node.getParent()))
			parent = Link.fromUri(altPath + node.getParent().getUri()).rel("parent")
					.title(node.getParent().getCode() + " (" + node.getParent().getLabel() + ")").type("dictionary")
					.build();
		else
			parent = toHref("parent", oid, node.getParent());

		List<Link> links = new ArrayList<>();
		links.add(self);
		links.add(alt);
		links.add(previous);

		if (node instanceof IcdNode)
		{
			IcdNodeWalker walker = ((IcdNodeFactory) factory).createNodeWalker();

			List<Link> excludes = toLinksFlatNodes("excludes", oid,
					((IcdNode) node).getExclusionList(walker::getNodesBySudoCode));
			List<Link> includes = toLinksFlatNodes("includes", oid,
					((IcdNode) node).getInclusionsList(walker::getNodesBySudoCode));
			List<Link> children = toLinksFlatNodes("child", oid, ((IcdNode) node).getChildren());

			links.addAll(excludes);
			links.addAll(includes);
			links.add(parent);
			links.addAll(children);

			return new IcdNodeDto(links, factory.getOid(), factory.getName(), node.getCode(), node.getLabel(),
					StringConverter.toString(((IcdNode) node).getNodeType()),
					StringConverter.toString(((IcdNode) node).getNodeUsage()));
		}
		else if (node instanceof OpsNode)
		{
			OpsNodeWalker walker = ((OpsNodeFactory) factory).createNodeWalker();

			List<Link> excludes = toLinksFlatNodes("excludes", oid,
					((OpsNode) node).getExclusionList(walker::getNodesBySudoCode));
			List<Link> includes = toLinksFlatNodes("includes", oid,
					((OpsNode) node).getInclusionsList(walker::getNodesBySudoCode));
			List<Link> children = toLinksFlatNodes("child", oid, ((OpsNode) node).getChildren());

			links.addAll(excludes);
			links.addAll(includes);
			links.add(parent);
			links.addAll(children);

			return new OpsNodeDto(links, factory.getOid(), factory.getName(), node.getCode(), node.getLabel(),
					StringConverter.toString(((OpsNode) node).getNodeType()));
		}
		else if (node instanceof AlphaIdNode)
		{
			IcdNode primaryIcdNode = ((AlphaIdNode) node).getPrimaryIcdNode();
			Link primaryIcd = primaryIcdNode == null ? null
					: toHref("primary-icd", icdService.getNodeFactory(primaryIcdNode.getVersion()).getOid(),
							primaryIcdNode);
			IcdNode asterixIcdNode = ((AlphaIdNode) node).getAsterixIcdNode();
			Link asterixIcd = asterixIcdNode == null ? null
					: toHref("asterix-icd", icdService.getNodeFactory(asterixIcdNode.getVersion()).getOid(),
							asterixIcdNode);
			IcdNode additionalIcdNode = ((AlphaIdNode) node).getAdditionalIcdNode();
			Link additionalIcd = additionalIcdNode == null ? null
					: toHref("additional-icd", icdService.getNodeFactory(additionalIcdNode.getVersion()).getOid(),
							additionalIcdNode);

			List<Link> children = toLinksFlatNodes("child", oid, ((AlphaIdNode) node).getChildren());

			links.add(primaryIcd);
			links.add(asterixIcd);
			links.add(additionalIcd);
			links.add(parent);
			links.addAll(children);

			return new AlphaIdNodeDto(links, factory.getOid(), factory.getName(), node.getCode(), node.getLabel());
		}
		else
			return null;
	}

	private List<Link> toLinksFlatNodes(String rel, String oid, Collection<? extends Node<?>> nodes)
	{
		return nodes.stream().flatMap(n -> n.childrenRecursive()).filter(this::selectNode).map(n -> toHref(rel, oid, n))
				.collect(Collectors.toList());
	}

	private Link toHref(String rel, String oid, Node<?> node)
	{
		if (node == null)
			return null;

		return Link.fromUri(baseUrl + "/" + PATH + "/" + oid + "/" + node.getCode()).rel(rel)
				.title(node.getCode() + " (" + node.getLabel() + ")").type("oid").build();
	}
}

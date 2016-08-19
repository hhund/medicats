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

	private IcdService icdService;

	public OidDictionaryWebservice(String baseUrl, IcdService icdService, OpsService opsService,
			AlphaIdService alphaIdService)
	{
		this.baseUrl = baseUrl;
		this.icdService = icdService;

		icdService.getNodeFactories().stream().filter(f -> f.getOid() != null && !f.getOid().isEmpty())
				.collect(Collectors.toMap(NodeFactory::getOid, Function.identity(), (a, b) -> a, () -> factories));
		opsService.getNodeFactories().stream().filter(f -> f.getOid() != null && !f.getOid().isEmpty())
				.collect(Collectors.toMap(NodeFactory::getOid, Function.identity(), (a, b) -> a, () -> factories));
		alphaIdService.getNodeFactories().stream().filter(f -> f.getOid() != null && !f.getOid().isEmpty())
				.collect(Collectors.toMap(NodeFactory::getOid, Function.identity(), (a, b) -> a, () -> factories));
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAll()
	{
		logger.trace("GET '/" + PATH + "'");

		List<DictionaryRelease> dictionaries = factories.values().stream()
				.sorted(Comparator.comparing(VersionedNodeFactory::getOid))
				.map(f -> new DictionaryRelease(
						Collections.singleton(Link.fromUri(baseUrl + "/" + PATH + "/" + f.getOid()).build()),
						f.getName(), f.getOid()))
				.collect(Collectors.toList());

		return Response.ok(
				new DictionaryReleases(Collections.singleton(Link.fromUri(baseUrl + "/" + PATH).build()), dictionaries))
				.build();
	}

	@GET
	@Path("{oid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getOid(@PathParam("oid") String oid)
	{
		logger.trace("GET '/" + PATH + "/" + Objects.toString(oid) + "'");

		if (!factories.containsKey(oid))
			return Response.status(Status.NOT_FOUND).build();

		VersionedNodeFactory<?, ?> f = factories.get(oid);

		List<Link> children = f.createNodeWalker().preOrderStream().filter(this::selectNode)
				.map(n -> Link.fromUri(baseUrl + "/" + PATH + "/" + oid + "/" + n.getCode()).rel("child")
						.title(n.getCode()).type("oid").build())
				.collect(Collectors.toList());

		Link self = Link.fromUri(baseUrl + "/" + PATH + "/" + oid).rel("self").title(f.getName()).type("oid").build();

		List<Link> links = new ArrayList<>();
		links.add(self);
		links.addAll(children);

		return Response.ok(new RootNodeDto(links, f.getName(), f.getOid())).build();
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

		return Response.ok(toDto(oid, factory, node)).build();
	}

	private NodeDto toDto(String oid, VersionedNodeFactory<?, ?> factory, VersionedNode<?> node)
	{
		Link self = Link.fromUri(baseUrl + "/" + PATH + "/" + oid + "/" + node.getCode()).rel("self")
				.title(node.getCode()).type("oid").build();

		String altPath;
		if (factory instanceof IcdNodeFactory)
			altPath = baseUrl + "/" + IcdDictionaryWebservice.PATH + "/" + factory.getSortIndex();
		else if (factory instanceof OpsNodeFactory)
			altPath = baseUrl + "/" + OpsDictionaryWebservice.PATH + "/" + factory.getSortIndex();
		else if (factory instanceof AlphaIdNodeFactory)
			altPath = baseUrl + "/" + AlphaIdDictionaryWebservice.PATH + "/" + factory.getSortIndex();
		else
			throw new WebApplicationException(Status.NOT_FOUND);

		Link alt = Link.fromUri(altPath + node.getUri()).rel("alternate").title(node.getCode()).type("dictionary")
				.build();

		Link parent;
		if (node.getParent() == null)
			parent = null;
		else if (node instanceof AlphaIdNode)
			parent = Link.fromUri(baseUrl + "/" + PATH + "/" + oid).rel("parent").title(factory.getName()).type("oid")
					.build();
		else if (!selectNode(node.getParent()))
			parent = Link.fromUri(altPath + node.getParent().getUri()).rel("parent").title(node.getParent().getCode())
					.type("dictionary").build();
		else
			parent = toHref("parent", oid, node.getParent());

		List<Link> links = new ArrayList<>();
		links.add(self);
		links.add(alt);

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

			return new IcdNodeDto(links, node.getCode(), node.getLabel(),
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

			return new OpsNodeDto(links, node.getCode(), node.getLabel(),
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

			return new AlphaIdNodeDto(links, node.getCode(), node.getLabel());
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

		return Link.fromUri(baseUrl + "/" + PATH + "/" + oid + "/" + node.getCode()).rel(rel).title(node.getCode())
				.type("oid").build();
	}
}

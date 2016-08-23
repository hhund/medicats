package de.gecko.medicats.web.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Link;

import de.gecko.medicats.ops.OpsNode;
import de.gecko.medicats.ops.OpsNode.OpsNodeType;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.OpsService;
import de.gecko.medicats.web.transfer.NodeDto;
import de.gecko.medicats.web.transfer.OpsNodeDto;
import de.gecko.medicats.web.transfer.StringConverter;

@Path(OpsDictionaryWebservice.PATH)
public class OpsDictionaryWebservice
		extends AbstractDictionaryWebservice<OpsNode, OpsNodeWalker, OpsNodeFactory, OpsService>
{
	public static final String PATH = DictionaryWebservice.PATH + "/ops";

	private final OpsService service;

	public OpsDictionaryWebservice(OpsService service, String baseUrl)
	{
		super(service, baseUrl, PATH);

		this.service = service;
	}

	@Override
	protected NodeDto toDto(OpsNodeFactory nodeFactory, OpsNode node, String baseUrl, String vocabulary,
			String vocabularyRelease, String[] p, List<Link> children)
	{
		List<String> parentUri = new ArrayList<>(Arrays.asList(baseUrl, vocabulary, vocabularyRelease));
		for (int i = 0; i < p.length - 1; i++)
			parentUri.add(p[i]);

		OpsNodeWalker walker = nodeFactory.createNodeWalker();

		Link self = toLink("self", "dictionary", vocabularyRelease, nodeFactory.getRootNode(), node);
		Link alt = OpsNodeType.CATEGORY.equals(node.getNodeType()) ? Link.fromUri(
				baseUrl + "/" + OidDictionaryWebservice.PATH + "/" + nodeFactory.getOid() + "/" + node.getCode())
				.rel("alternate").title(node.getCode()).type("oid").build() : null;

		List<Link> excludes = toLinks("excludes", "dictionary", vocabularyRelease, nodeFactory.getRootNode(),
				node.getExclusions(walker::getNodesBySudoCode));
		List<Link> includes = toLinks("includes", "dictionary", vocabularyRelease, nodeFactory.getRootNode(),
				node.getInclusions(walker::getNodesBySudoCode));

		Link previous = null;
		if (node.getPrevious().isPresent())
		{
			OpsNode previousNode = node.getPrevious().get();
			OpsNodeFactory previousNodeFactory = service.getNodeFactory(nodeFactory.getPreviousVersion());

			if (previousNodeFactory != null)
				previous = Link
						.fromUri(baseUrl + "/" + vocabulary + "/" + previousNodeFactory.getSortIndex()
								+ previousNode.getUri())
						.rel("previous").title(previousNode.getCode()).type("dictionary").build();
		}

		String parentTitle;
		if (nodeFactory.getRootNode().equals(node.getParent()))
			parentTitle = nodeFactory.getName();
		else if (node.getParent() != null)
			parentTitle = node.getParent().getCode();
		else
			parentTitle = null;

		Link parent = toLink("parent", "dictionary", parentTitle, parentUri.stream());

		List<Link> links = new ArrayList<>();
		links.add(self);
		links.add(alt);
		links.add(previous);
		links.addAll(excludes);
		links.addAll(includes);
		links.add(parent);
		links.addAll(children);

		return new OpsNodeDto(links, node.getCode(), node.getLabel(), StringConverter.toString(node.getNodeType()));
	}
}

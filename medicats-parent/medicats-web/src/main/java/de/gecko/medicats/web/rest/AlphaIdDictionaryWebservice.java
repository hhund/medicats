package de.gecko.medicats.web.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Link;

import de.gecko.medicats.alphaid.AlphaIdNode;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeWalker;
import de.gecko.medicats.alphaid.AlphaIdService;
import de.gecko.medicats.icd10.IcdNode;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdService;
import de.gecko.medicats.web.transfer.AlphaIdNodeDto;
import de.gecko.medicats.web.transfer.NodeDto;

@Path(AlphaIdDictionaryWebservice.PATH)
public class AlphaIdDictionaryWebservice
		extends AbstractDictionaryWebservice<AlphaIdNode, AlphaIdNodeWalker, AlphaIdNodeFactory, AlphaIdService>
{
	public static final String PATH = DictionaryWebservice.PATH + "/alpha-id";

	private final AlphaIdService service;
	private final IcdService icdService;

	public AlphaIdDictionaryWebservice(AlphaIdService service, IcdService icdService, String baseUrl,
			XsltTransformer transformer)
	{
		super(service, baseUrl, PATH, transformer);

		this.service = service;
		this.icdService = icdService;
	}

	@Override
	protected NodeDto toDto(AlphaIdNodeFactory nodeFactory, AlphaIdNode node, String baseUrl, String vocabulary,
			String vocabularyRelease, String[] p, List<Link> childrenHrefs)
	{
		List<String> parentUri = new ArrayList<>(Arrays.asList(baseUrl, vocabulary, vocabularyRelease));
		for (int i = 0; i < p.length - 1; i++)
			parentUri.add(p[i]);

		Link self = toLink("self", "dictionary", vocabularyRelease, nodeFactory.getRootNode(), node);
		Link alt = Link
				.fromUri(baseUrl + "/" + OidDictionaryWebservice.PATH + "/" + nodeFactory.getOid() + "/"
						+ node.getCode())
				.rel("alternate").title(node.getCode() + " (" + node.getLabel() + ")").type("oid").build();
		Link parent = toLink("parent", "dictionary", nodeFactory.getName(), parentUri.stream());

		Link primaryIcdNode = toNodeLink("primary-icd", "dictionary", baseUrl + "/" + DictionaryWebservice.PATH,
				node.getPrimaryIcdNode());
		Link asterixIcdNode = toNodeLink("asterix-icd", "dictionary", baseUrl + "/" + DictionaryWebservice.PATH,
				node.getAsterixIcdNode());
		Link additionalIcdNode = toNodeLink("additional-icd", "dictionary", baseUrl + "/" + DictionaryWebservice.PATH,
				node.getAdditionalIcdNode());

		Link previous = null;
		if (node.getPrevious().isPresent())
		{
			AlphaIdNode previousNode = node.getPrevious().get();
			AlphaIdNodeFactory previousNodeFactory = service.getNodeFactory(nodeFactory.getPreviousVersion());

			if (previousNodeFactory != null)
				previous = Link
						.fromUri(baseUrl + "/" + vocabulary + "/" + previousNodeFactory.getSortIndex()
								+ previousNode.getUri())
						.rel("previous").title(previousNode.getCode() + " (" + previousNode.getLabel() + ")")
						.type("dictionary").build();
		}

		List<Link> links = new ArrayList<>();
		links.add(self);
		if (nodeFactory.getOid() != null && !nodeFactory.getOid().isEmpty())
			links.add(alt);
		links.add(previous);
		links.add(primaryIcdNode);
		links.add(asterixIcdNode);
		links.add(additionalIcdNode);
		links.add(parent);
		links.addAll(childrenHrefs);

		return new AlphaIdNodeDto(links, nodeFactory.getOid(), nodeFactory.getName(), node.getCode(), node.getLabel());
	}

	private Link toNodeLink(String rel, String type, String baseUrl, IcdNode node)
	{
		if (node == null)
			return null;

		IcdNodeFactory icdNodeNodeFactory = icdService.getNodeFactory(node.getVersion());
		int icdNodeRootNodePathLength = icdNodeNodeFactory.getRootNode().getPath().length();

		return toNodeLink(rel, type, node.getCode() + " (" + node.getLabel() + ")", baseUrl, "icd-10-gm",
				icdNodeNodeFactory.getSortIndex(),
				node.getPath().substring(icdNodeRootNodePathLength, node.getPath().length()));
	}

	private Link toNodeLink(String rel, String type, String label, String baseUrl, String icdVocabulary,
			int icdVocabularyRelease, String icdPath)
	{
		return Link.fromUri(baseUrl + "/" + icdVocabulary + "/" + icdVocabularyRelease + icdPath).rel(rel).title(label)
				.type(type).build();
	}
}

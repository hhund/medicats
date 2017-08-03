package de.gecko.medicats.web.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Link;

import de.gecko.medicats.icd10.IcdNode;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.IcdService;
import de.gecko.medicats.icd10.IcdNode.IcdNodeType;
import de.gecko.medicats.web.transfer.IcdNodeDto;
import de.gecko.medicats.web.transfer.NodeDto;
import de.gecko.medicats.web.transfer.StringConverter;

@Path(IcdDictionaryWebservice.PATH)
public class IcdDictionaryWebservice
		extends AbstractDictionaryWebservice<IcdNode, IcdNodeWalker, IcdNodeFactory, IcdService>
{
	public static final String PATH = DictionaryWebservice.PATH + "/icd-10-gm";

	private final IcdService service;

	public IcdDictionaryWebservice(IcdService service, String baseUrl, XsltTransformer transformer)
	{
		super(service, baseUrl, PATH, transformer);

		this.service = service;
	}

	@Override
	protected NodeDto toDto(IcdNodeFactory nodeFactory, IcdNode node, String baseUrl, String vocabulary,
			String vocabularyRelease, String[] p, List<Link> children)
	{
		List<String> parentUri = new ArrayList<>(Arrays.asList(baseUrl, vocabulary, vocabularyRelease));
		for (int i = 0; i < p.length - 1; i++)
			parentUri.add(p[i]);

		IcdNodeWalker walker = nodeFactory.createNodeWalker();

		Link self = toLink("self", "dictionary", vocabularyRelease, nodeFactory.getRootNode(), node);
		Link alt = IcdNodeType.CATEGORY.equals(node.getNodeType()) ? Link
				.fromUri(baseUrl + "/" + OidDictionaryWebservice.PATH + "/" + nodeFactory.getOid() + "/"
						+ node.getCode())
				.rel("alternate").title(node.getCode() + " (" + node.getLabel() + ")").type("oid").build() : null;

		List<Link> excludes = toLinks("excludes", "dictionary", vocabularyRelease, nodeFactory.getRootNode(),
				node.getExclusions(walker::getNodesBySudoCode).sorted(Comparator.comparing(IcdNode::getPath)));
		List<Link> includes = toLinks("includes", "dictionary", vocabularyRelease, nodeFactory.getRootNode(),
				node.getInclusions(walker::getNodesBySudoCode).sorted(Comparator.comparing(IcdNode::getPath)));

		Link previous = null;
		if (node.getPrevious().isPresent())
		{
			IcdNode previousNode = node.getPrevious().get();
			IcdNodeFactory previousNodeFactory = service.getNodeFactory(nodeFactory.getPreviousVersion());

			if (previousNodeFactory != null)
				previous = Link
						.fromUri(baseUrl + "/" + vocabulary + "/" + previousNodeFactory.getSortIndex()
								+ previousNode.getUri())
						.rel("previous").title(previousNode.getCode() + " (" + previousNode.getLabel() + ")")
						.type("dictionary").build();
		}

		String parentTitle;
		if (nodeFactory.getRootNode().equals(node.getParent()))
			parentTitle = nodeFactory.getName();
		else if (node.getParent() != null)
			parentTitle = node.getParent().getCode() + " (" + node.getParent().getLabel() + ")";
		else
			parentTitle = null;

		Link parent = toLink("parent", "dictionary", parentTitle, parentUri.stream());

		List<Link> links = new ArrayList<>();
		links.add(self);
		if (nodeFactory.getOid() != null && !nodeFactory.getOid().isEmpty())
			links.add(alt);
		links.add(previous);
		links.addAll(excludes);
		links.addAll(includes);
		links.add(parent);
		links.addAll(children);

		return new IcdNodeDto(links, nodeFactory.getOid(), nodeFactory.getName(), node.getCode(), node.getLabel(),
				StringConverter.toString(node.getNodeType()), StringConverter.toString(node.getNodeUsage()));
	}
}

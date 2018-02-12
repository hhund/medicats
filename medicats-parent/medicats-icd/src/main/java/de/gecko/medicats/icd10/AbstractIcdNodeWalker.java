package de.gecko.medicats.icd10;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractIcdNodeWalker implements IcdNodeWalker
{
	private static final String[] INDENTENTIONS = { "", "\t", "\t\t", "\t\t\t", "\t\t\t\t", "\t\t\t\t\t",
			"\t\t\t\t\t\t", "\t\t\t\t\t\t\t", "\t\t\t\t\t\t\t\t", "\t\t\t\t\t\t\t\t\t" };

	public static final Pattern ICD_CODE_PATTERN = Pattern.compile("[A-Z][0-9]{2}(?:\\.[0-9]{1,2})?");
	public static final Pattern ICD_CODE_PATTERN_EXCLAMATION_MARK = Pattern
			.compile("([A-Z][0-9]{2}(?:\\.[0-9]{1,2})?)[!]");
	public static final Pattern ICD_FROM_TO_PATTERN = Pattern
			.compile("([A-Z][0-9]{2}(?:\\.[0-9]{1,2})?)-([A-Z][0-9]{2}(?:\\.[0-9]{1,2})?)");
	public static final Pattern ICD_CODE_PATTERN_DASH_1 = Pattern.compile("([A-Z][0-9]{2})\\.-");
	public static final Pattern ICD_CODE_PATTERN_DASH_2 = Pattern.compile("([A-Z][0-9]{2}\\.[0-9])-");

	private final IcdNode root;

	private Map<String, IcdNode> nodesByCode;
	private List<IcdNode> nodesPreOrder;

	public AbstractIcdNodeWalker(IcdNode root)
	{
		this.root = root;
	}

	@Override
	public IcdNode getRootNode()
	{
		return root;
	}

	@Override
	public List<IcdNode> getNodesBySudoCode(String code)
	{
		if (code == null || code.isEmpty())
			return null;

		code = code.trim();

		if (ICD_CODE_PATTERN.matcher(code).matches())
		{
			IcdNode node = getNodeByCode(code);
			if (node != null)
				return Collections.singletonList(node);
		}

		{
			Matcher matcher = ICD_CODE_PATTERN_EXCLAMATION_MARK.matcher(code);
			if (matcher.find() && matcher.end() >= code.length())
			{
				IcdNode node = getNodeByCode(matcher.group(1));
				if (node != null)
					return Collections.singletonList(node);
			}
		}

		{
			Matcher matcher = ICD_CODE_PATTERN_DASH_1.matcher(code);
			if (matcher.find() && matcher.end() >= code.length())
			{
				IcdNode node = getNodeByCode(matcher.group(1));
				if (node != null)
					return Collections.singletonList(node);
			}
		}

		{
			Matcher matcher = ICD_CODE_PATTERN_DASH_2.matcher(code);
			if (matcher.find() && matcher.end() >= code.length())
			{
				IcdNode node = getNodeByCode(matcher.group(1));
				if (node != null)
					return Collections.singletonList(node);
			}
		}

		{
			Matcher matcher = ICD_FROM_TO_PATTERN.matcher(code);
			if (matcher.find() && matcher.end() >= code.length())
				return getNodesFiltered(matcher.group(1), matcher.group(2));
		}

		return fromSpecialCasesOrEmptyList(code);
	}

	protected List<IcdNode> fromSpecialCasesOrEmptyList(String code)
	{
		return Collections.emptyList();
	}

	@Override
	public Map<String, IcdNode> getNodesByCode()
	{
		if (nodesByCode == null)
		{
			Map<String, IcdNode> nodes = preOrderStream()
					.collect(Collectors.toMap(IcdNode::getCode, Function.identity(), selectMostSpecificCode()));

			nodesByCode = Collections.unmodifiableMap(nodes);
		}

		return nodesByCode;
	}

	private BinaryOperator<IcdNode> selectMostSpecificCode()
	{
		return (n1, n2) -> n1.getParentCount() > n2.getParentCount() ? n1 : n2;
	}

	@Override
	public List<IcdNode> getNodes(String codeFrom, String codeTo)
	{
		return new ArrayList<>(getNodes(getNodeByCode(codeFrom), getNodeByCode(codeTo)));
	}

	@Override
	public List<IcdNode> getNodesFiltered(String codeFrom, String codeTo)
	{
		List<IcdNode> nodesNotFiltered = getNodes(getNodeByCode(codeFrom), getNodeByCode(codeTo));

		if (nodesNotFiltered.isEmpty())
			throw new IllegalStateException("No nodes found from " + codeFrom + " to " + codeTo);

		return Collections.unmodifiableList(filterChildrenAndNewListObject(nodesNotFiltered));
	}

	private List<IcdNode> getNodes(IcdNode from, IcdNode to)
	{
		if (from == null || to == null)
			return Collections.emptyList();

		List<IcdNode> nodesPreOrder = getNodesPreOrder();
		List<IcdNode> subList = new ArrayList<>(
				nodesPreOrder.subList(nodesPreOrder.indexOf(from), nodesPreOrder.indexOf(to) + 1));

		IcdNode parent = from;
		while (parent.getParent() != null && parent.getParent().getParent() != null)
			subList.add(0, parent = parent.getParent());

		List<IcdNode> toChildren = preOrder(to);
		toChildren.remove(to);
		subList.addAll(toChildren);

		return subList;
	}

	private List<IcdNode> filterChildrenAndNewListObject(List<IcdNode> nodes)
	{
		// filter subtrees with all children in list
		List<IcdNode> filteredNodes = new ArrayList<>(nodes);
		for (int i = nodes.size() - 1; i >= 0; i--)
		{
			IcdNode n = nodes.get(i);
			List<IcdNode> children = preOrder(n);

			if (!n.getChildren().isEmpty() && nodes.containsAll(children))
			{
				children.remove(n);
				filteredNodes.removeAll(children);
			}

			// filter node if not all children in list
			if (!nodes.containsAll(children))
				filteredNodes.remove(n);
		}

		// special case for first node
		IcdNode firstNode = nodes.get(0);
		IcdNode firstNodeParent = firstNode.getParent();
		if (firstNodeParent != null && nodes.containsAll(firstNodeParent.getChildren()))
		{
			filteredNodes.removeAll(firstNodeParent.getChildren());
			filteredNodes.add(0, firstNodeParent);
		}

		return filteredNodes;
	}

	@Override
	public void walkNodesPreOrder(IcdNodeConsumer consumer)
	{
		walkNodesPreOrder(Objects.requireNonNull(consumer, "consumer"), getRootNode());
	}

	private void walkNodesPreOrder(IcdNodeConsumer consumer, IcdNode node)
	{
		Stream<IcdNode> exclusions = node.getExclusions(this::getNodesBySudoCode);
		Stream<IcdNode> inclusions = node.getInclusions(this::getNodesBySudoCode);

		consumer.accept(node, exclusions, inclusions);

		List<IcdNode> children = node.getChildren();

		if (!children.isEmpty())
			for (IcdNode chiledNode : children)
				walkNodesPreOrder(consumer, chiledNode);
	}

	@Override
	public void walkNodesPostOrder(IcdNodeConsumer consumer)
	{
		walkNodesPostOrder(Objects.requireNonNull(consumer, "consumer"), getRootNode());
	}

	private void walkNodesPostOrder(IcdNodeConsumer consumer, IcdNode node)
	{
		List<IcdNode> children = node.getChildren();

		if (!children.isEmpty())
			for (IcdNode chiledNode : children)
				walkNodesPostOrder(consumer, chiledNode);

		Stream<IcdNode> exclusions = node.getExclusions(this::getNodesBySudoCode);
		Stream<IcdNode> inclusions = node.getInclusions(this::getNodesBySudoCode);

		consumer.accept(node, exclusions, inclusions);
	}

	private List<IcdNode> getNodesPreOrder()
	{
		if (nodesPreOrder == null)
			nodesPreOrder = preOrder(getRootNode());

		return Collections.unmodifiableList(nodesPreOrder);
	}

	private List<IcdNode> preOrder(IcdNode node)
	{
		return preOrderStream(node).collect(Collectors.toList());
	}

	@Override
	public Consumer<IcdNode> printer(PrintStream out)
	{
		return n ->
		{
			final String identention = INDENTENTIONS[n.getParentCount()];

			out.println(identention + n.toString());

			List<IcdNode> exclusionsList = n.getExclusionList(this::getNodesBySudoCode);
			List<IcdNode> inclusionsList = n.getInclusionList(this::getNodesBySudoCode);

			if (!exclusionsList.isEmpty())
				out.println(identention + " -" + exclusionsList);
			if (!inclusionsList.isEmpty())
				out.println(identention + " +" + inclusionsList);
		};
	}
}

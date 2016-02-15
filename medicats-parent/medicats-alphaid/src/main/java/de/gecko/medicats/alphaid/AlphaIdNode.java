package de.gecko.medicats.alphaid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.gecko.medicats.VersionedNode;
import de.gecko.medicats.icd10.IcdNode;
import de.gecko.medicats.icd10.IcdNode.IcdNodeUsage;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.IcdNodeWrapper;

public class AlphaIdNode implements VersionedNode<AlphaIdNode>
{
	private final AlphaIdNode parent;

	private final String alphaId;
	private final boolean valid;

	private final String primaryIcdCode;
	private final String asterixIcdCode;
	private final String additionalIcdCode;

	private final String orphaId;
	private final String label;

	private final List<AlphaIdNode> children = new ArrayList<>();

	public AlphaIdNode(AlphaIdNode parent, String alphaId, boolean valid, String primaryIcdCode, String asterixIcdCode,
			String additionalIcdCode, String orphaId, String label)
	{
		this.parent = parent;
		this.alphaId = alphaId;
		this.valid = valid;
		this.primaryIcdCode = primaryIcdCode;
		this.asterixIcdCode = asterixIcdCode;
		this.additionalIcdCode = additionalIcdCode;
		this.orphaId = orphaId;
		this.label = label;
	}

	protected void addToParent()
	{
		if (getParent() != null)
			getParent().children.add(this);
	}

	public boolean isRoot()
	{
		return getParent() == null;
	}

	public AlphaIdNode getParent()
	{
		return parent;
	}

	public String getVersion()
	{
		return getParent().getVersion();
	}

	public String getPreviousVersion()
	{
		return getParent().getPreviousVersion();
	}

	public String getIcdVersion()
	{
		return getParent().getIcdVersion();
	}

	protected AlphaIdNodeWalker getPreviousAlphaIdNodeWalker()
	{
		return getParent().getPreviousAlphaIdNodeWalker();
	}

	protected IcdNodeWalker getIcdNodeWalker()
	{
		return getParent().getIcdNodeWalker();
	}

	@Override
	public String getCode()
	{
		return getAlphaId();
	}

	public String getAlphaId()
	{
		return alphaId;
	}

	@Override
	public Optional<String> getPreviousCode()
	{
		return Optional.ofNullable(getAlphaId());
	}

	public boolean isValid()
	{
		return valid;
	}

	public String getPrimaryIcdCode()
	{
		return primaryIcdCode;
	}

	public String getAsterixIcdCode()
	{
		return asterixIcdCode;
	}

	public String getAdditionalIcdCode()
	{
		return additionalIcdCode;
	}

	@Override
	public Optional<AlphaIdNode> getPrevious()
	{
		if (getAlphaId() == null)
			return Optional.empty();

		AlphaIdNodeWalker walker = getPreviousAlphaIdNodeWalker();

		if (walker == null)
			return Optional.empty();

		return walker.getNodeByCode(getPreviousCode());
	}

	public IcdNode getPrimaryIcdNode()
	{
		return getIcdNode(getPrimaryIcdCode());
	}

	public IcdNode getAsterixIcdNode()
	{
		return getIcdNode(getAsterixIcdCode());
	}

	public IcdNode getAdditionalIcdNode()
	{
		return getIcdNode(getAdditionalIcdCode());
	}

	private IcdNode getIcdNode(String code)
	{
		if (code == null)
			return null;

		IcdNodeWalker walker = getIcdNodeWalker();

		if (walker == null)
			return null;

		return wrappIfUsageDifferent(walker.getNodeByCode(removeUsage(code)), fromCode(code));
	}

	private String removeUsage(String code)
	{
		if (code.endsWith("*") || code.endsWith("+") || code.endsWith("!"))
			return code.substring(0, code.length() - 1);
		else
			return code;
	}

	private IcdNodeUsage fromCode(String code)
	{
		if (code.endsWith("*"))
			return IcdNodeUsage.ASTER;
		else if (code.endsWith("+"))
			return IcdNodeUsage.DAGGER;
		else if (code.endsWith("!"))
			return IcdNodeUsage.OPTIONAL;
		else
			return IcdNodeUsage.UNDEFINED;
	}

	protected IcdNode wrappIfUsageDifferent(IcdNode toWrapp, IcdNodeUsage usage)
	{
		if (toWrapp == null)
			return null;
		else if (toWrapp.getNodeUsage().equals(usage))
			return toWrapp;
		else
			return new IcdNodeWrapper(toWrapp, usage);
	}

	public boolean hasOrphaId()
	{
		return getOrphaId() != null;
	}

	public String getOrphaId()
	{
		return orphaId;
	}

	@Override
	public String getLabel()
	{
		return label;
	}

	public List<AlphaIdNode> getChildren()
	{
		return Collections.unmodifiableList(children);
	}

	@Override
	public Stream<AlphaIdNode> children()
	{
		return children.stream();
	}

	@Override
	public String toString()
	{
		List<IcdNode> icdNodes = Arrays.asList(getPrimaryIcdNode(), getAsterixIcdNode(), getAdditionalIcdNode());

		String icdNodesString = icdNodes.stream().filter(n -> n != null).map(IcdNode::toString)
				.collect(Collectors.joining(", "));

		return getAlphaId() + " - " + getLabel() + (icdNodesString.isEmpty() ? "" : " [" + icdNodesString + "]");
	}

	public String getPath()
	{
		return getParent().getPath() + "/" + getAlphaId();
	}
}

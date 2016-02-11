package de.gecko.medicats.ops.ver_2015;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.gecko.medicats.ops.OpsNode;
import de.gecko.medicats.ops.claml.AbstractClaMLOpsNodeWalker;
import de.gecko.medicats.ops.claml.ClaMLOpsNode;

public class Ops2015NodeWalker extends AbstractClaMLOpsNodeWalker
{
	public Ops2015NodeWalker(ClaMLOpsNode root)
	{
		super(root);
	}

	@Override
	public List<OpsNode> getNodesBySudoCode(String code)
	{
		if (code == null)
			return Collections.emptyList();
		else if (code.endsWith(" ff."))
			code = code.substring(0, code.length() - 4);
		else if (code.endsWith(" f."))
			code = code.substring(0, code.length() - 3);
		else if (code.contains(" bis "))
			code = code.replace(" bis ", "...");

		List<OpsNode> nodes;
		if (code.equals("5-916.0-9"))
		{
			nodes = new ArrayList<>();
			nodes.addAll(super.getNodesBySudoCode("5-916.0"));
			nodes.addAll(super.getNodesBySudoCode("5-916.1"));
			nodes.addAll(super.getNodesBySudoCode("5-916.2"));
			nodes.addAll(super.getNodesBySudoCode("5-916.3"));
			nodes.addAll(super.getNodesBySudoCode("5-916.4"));
			nodes.addAll(super.getNodesBySudoCode("5-916.5"));
			nodes.addAll(super.getNodesBySudoCode("5-916.6"));
			nodes.addAll(super.getNodesBySudoCode("5-916.7"));
			nodes.addAll(super.getNodesBySudoCode("5-916.8"));
			nodes.addAll(super.getNodesBySudoCode("5-916.9"));
		}
		else if (code.equals("8-524.0-2"))
		{
			nodes = new ArrayList<>();
			nodes.addAll(super.getNodesBySudoCode("8-524.0"));
			nodes.addAll(super.getNodesBySudoCode("8-524.1"));
			nodes.addAll(super.getNodesBySudoCode("8-524.2"));
		}
		else
			nodes = super.getNodesBySudoCode(code);

		if (nodes.isEmpty())
			System.err.println(code);

		return nodes;
	}
}

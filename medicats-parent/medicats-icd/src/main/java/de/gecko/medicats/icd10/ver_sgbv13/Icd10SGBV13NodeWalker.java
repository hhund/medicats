package de.gecko.medicats.icd10.ver_sgbv13;

import java.util.Collections;
import java.util.List;

import de.gecko.medicats.icd10.IcdNode;
import de.gecko.medicats.icd10.sgml.AbstractSgmlIcdNodeWalker;
import de.gecko.medicats.icd10.sgml.SgmlIcdNode;

public class Icd10SGBV13NodeWalker extends AbstractSgmlIcdNodeWalker
{
	public Icd10SGBV13NodeWalker(SgmlIcdNode root)
	{
		super(root);
	}

	@Override
	public List<IcdNode> getNodesBySudoCode(String code)
	{
		if ("C81-C87".equals(code) || "Z61.4-Z61.6".equals(code) || "T20-T32".equals(code) || "Z30.4-Z30.5".equals(code)
				|| "B38.0-B38.2".equals(code))
			return Collections.emptyList();
		else
			return super.getNodesBySudoCode(code);
	}
}

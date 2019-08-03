package de.gecko.medicats.ops.ver_2018;

import java.util.List;
import java.util.Optional;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.ZipSource;
import de.gecko.medicats.claml.ClamlClass;
import de.gecko.medicats.claml.ModifierClass;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.claml.AbstractClaMLOpsNodeFactory;
import de.gecko.medicats.ops.claml.ClaMLOpsNode;

public class Ops2018NodeFactory extends AbstractClaMLOpsNodeFactory implements OpsNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "ops2018.zip", 3112603067L);

	private final FileSource clamlDtd = new FileSource(zip, "p1sec2018", "Klassifikationsdateien", "ClaML.dtd");
	private final FileSource clamlXml = new FileSource(zip, "p1sec2018", "Klassifikationsdateien",
			"ops2018syst_claml_20171018.xml");
	private FileSource transitionFile = new FileSource(zip, "p1sut2018", "Klassifikationsdateien",
			"ops2018syst_umsteiger_2017_2018.txt");
	private FileSource systFile = new FileSource(zip, "p1sut2018", "Klassifikationsdateien", "ops2018syst.txt");

	@Override
	public String getName()
	{
		return "OPS 2018";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.472";
	}

	@Override
	public String getPreviousVersion()
	{
		return "ops2017";
	}

	@Override
	public String getVersion()
	{
		return "ops2018";
	}

	@Override
	public int getSortIndex()
	{
		return 2018;
	}

	@Override
	protected FileSource getClamlXml()
	{
		return clamlXml;
	}

	@Override
	protected FileSource getClamlDtd()
	{
		return clamlDtd;
	}

	@Override
	protected FileSource getTransitionFile()
	{
		return transitionFile;
	}

	@Override
	protected FileSource getSystFile()
	{
		return systFile;
	}

	@Override
	public OpsNodeWalker createNodeWalker()
	{
		return new Ops2018NodeWalker(getRootNode());
	}

	@Override
	protected boolean isSpecialCaseWithPrimaryModifier(String modifier)
	{
		return "ST538".equals(modifier);
	}

	@Override
	protected void createSpecialNode(ClaMLOpsNode parent, ClamlClass clamlClass, ModifierClass primaryModifier,
			List<ModifierClass> modifierClasses)
	{
		String superModifierCode = primaryModifier.getSuperClass().getCode();
		Optional<ModifierClass> newPrimaryModifier = modifierClasses.stream()
				.filter(m -> m.getCode().equals(superModifierCode)).findFirst();

		if (newPrimaryModifier.isPresent())
			ClaMLOpsNode.createNode(parent, clamlClass, newPrimaryModifier.get(), primaryModifier);
		else
			ClaMLOpsNode.createNode(parent, clamlClass, primaryModifier);
	}

	@Override
	protected int getPreviousCodesForwardsCompatibleColumn()
	{
		return 4;
	}

	@Override
	protected int getCurrentCodesBackwardsCompatibleColumn()
	{
		return 5;
	}
}

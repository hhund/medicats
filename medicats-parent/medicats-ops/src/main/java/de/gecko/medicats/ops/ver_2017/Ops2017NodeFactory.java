package de.gecko.medicats.ops.ver_2017;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import de.gecko.medicats.claml.ClaMLClass;
import de.gecko.medicats.claml.ModifierClass;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.claml.AbstractClaMLOpsNodeFactory;
import de.gecko.medicats.ops.claml.ClaMLOpsNode;

public class Ops2017NodeFactory extends AbstractClaMLOpsNodeFactory implements OpsNodeFactory
{
	private static final String XML_RESOURCE_FILENAME = "ops2017syst_claml_20161019.xml";
	private static final String UMSTEIGER_RESOURCE_FILENAME = "ops2017syst_umsteiger_2016_2017.txt";
	private static final String PREVIOUS_VERSION = "ops2016";
	private static final String VERSION = "ops2017";

	@Override
	public String getName()
	{
		return "OPS 2017";
	}
	
	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.464";
	}
	
	@Override
	protected String getXmlResourceFileName()
	{
		return XML_RESOURCE_FILENAME;
	}

	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public int getSortIndex()
	{
		return 2017;
	}

	@Override
	public String getPreviousVersion()
	{
		return PREVIOUS_VERSION;
	}

	@Override
	protected String getPreviousCodesFileName()
	{
		return UMSTEIGER_RESOURCE_FILENAME;
	}

	@Override
	public OpsNodeWalker createNodeWalker()
	{
		return new Ops2017NodeWalker(getRootNode());
	}

	@Override
	protected boolean isSpecialCaseWithPrimaryModifier(String modifier)
	{
		return "ST538".equals(modifier);
	}

	@Override
	protected void createSpecialNode(ClaMLOpsNode parent, ClaMLClass clamlClass, ModifierClass primaryModifier,
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
	protected Path getTaxonomyZipFileName(Path basePath)
	{
		return basePath.resolve("p1sec2017.zip");
	}

	@Override
	protected long getTaxonomyZipChecksum()
	{
		return 2630446764L;
	}

	@Override
	protected Path getTransitionZipFileName(Path basePath)
	{
		return basePath.resolve("p1sut2017.zip");
	}

	@Override
	protected long getTransitionZipChecksum()
	{
		return 520553580L;
	}

	@Override
	protected Path getClaMLDtdPath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath("Klassifikationsdateien", "ClaML.dtd");
	}

	@Override
	protected Path getXmlResourcePath(FileSystem taxonomyZip)
	{
		return taxonomyZip.getPath("Klassifikationsdateien", getXmlResourceFileName());
	}

	@Override
	protected Path getTransitionFilePath(FileSystem transitionZip)
	{
		return transitionZip.getPath("Klassifikationsdateien", getPreviousCodesFileName());
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
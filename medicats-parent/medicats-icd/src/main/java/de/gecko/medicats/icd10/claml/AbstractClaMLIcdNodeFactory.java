package de.gecko.medicats.icd10.claml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.gecko.medicats.claml.ClaML;
import de.gecko.medicats.claml.ClaMLClass;
import de.gecko.medicats.claml.ClaMLClassKind;
import de.gecko.medicats.claml.ClaMLReader;
import de.gecko.medicats.claml.ModifiedBy;
import de.gecko.medicats.claml.ModifierClass;
import de.gecko.medicats.claml.SubClass;
import de.gecko.medicats.claml.ValidModifierClass;
import de.gecko.medicats.icd10.AbstractIcdNodeFactory;

public abstract class AbstractClaMLIcdNodeFactory extends AbstractIcdNodeFactory
{
	private static final List<String> DIABETES_MELLITUS = Arrays.asList("E10", "E11", "E12", "E13", "E14");
	private static final String DIABETES_MELLITUS_MODIFIER_4 = "S04E10_4";
	private static final String DIABETES_MELLITUS_MODIFIER_5 = "S04E10_5";
	private static final String META_EXCLUDE_ON_PRECEDING_MODIFIER = "excludeOnPrecedingModifier";

	private ClaML claML;

	protected abstract String getXmlResourceFileName();

	protected InputStream getClaMLDtd()
	{
		try
		{
			return Files.newInputStream(getClaMLDtdPath(getTaxonomyZip()));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected abstract Path getClaMLDtdPath(FileSystem taxonomyZip);

	protected InputStream getXmlResource()
	{
		try
		{
			return Files.newInputStream(getXmlResourcePath(getTaxonomyZip()));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected abstract Path getXmlResourcePath(FileSystem taxonomyZip);

	protected ClaML getClaML()
	{
		if (claML == null)
			claML = ClaMLReader.read(getXmlResource(), getClaMLDtd());

		return claML;
	}

	@Override
	public ClaMLIcdNodeRoot getRootNode()
	{
		ClaML claML = getClaML();
		ClaMLIcdNodeRoot root = new ClaMLIcdNodeRoot(claML, getVersion(), getSortIndex(), getPreviousCodes(),
				getPreviousVersion());

		Optional<ClaMLClassKind> chapterKind = claML.getClaMLClassKinds().getClaMLClassKinds().stream()
				.filter(k -> "chapter".equals(k.getName())).findFirst();

		List<ClaMLClass> chapters = claML.getClaMLClasses().stream().filter(c -> c.getKind().equals(chapterKind.get()))
				.collect(Collectors.toList());

		Map<String, ClaMLClass> icdClassesByCode = claML.getClaMLClasses().parallelStream()
				.collect(Collectors.toMap(c -> c.getCode(), Function.identity()));

		Map<String, List<ModifierClass>> modifierClassesByModifier = claML.getModifierClasses().stream()
				.collect(Collectors.groupingBy(c -> c.getModifier()));

		for (ClaMLClass chapter : chapters)
			createIcdNodes(root, chapter, icdClassesByCode, modifierClassesByModifier);

		return root;
	}

	private void createIcdNodes(ClaMLIcdNode parent, ClaMLClass icdClass, Map<String, ClaMLClass> icdClassesByCode,
			Map<String, List<ModifierClass>> modifierClassesByModifier)
	{
		ClaMLIcdNode node = createIcdNodes(parent, icdClass, modifierClassesByModifier);

		for (SubClass s : icdClass.getSubClasses())
		{
			ClaMLClass icdSubClass = icdClassesByCode.get(s.getCode());

			if (icdSubClass != null)
				createIcdNodes(node, icdSubClass, icdClassesByCode, modifierClassesByModifier);
			// ignore icd codes such as Y35.6, Y36.0, Y36.5
			// else
			// System.out.println(INDENTENTIONS[currentDepth + 1] +
			// s.getCode()
			// + " - ERROR");
		}
	}

	private ClaMLIcdNode createIcdNodes(ClaMLIcdNode parent, ClaMLClass icdClass,
			Map<String, List<ModifierClass>> modifierClassesByModifier)
	{
		ClaMLIcdNode node = ClaMLIcdNode.createNode(parent, icdClass);

		if (icdClass.getModifiedByElements().size() == 1)
		{
			ModifiedBy modifiedBy = icdClass.getModifiedByElements().get(0);
			List<ModifierClass> modifierClasses = modifierClassesByModifier.get(modifiedBy.getCode());

			if (modifiedBy.getValidModifierClasses().size() > 0)
			{
				Map<String, ModifierClass> byCode = modifierClasses.parallelStream()
						.collect(Collectors.toMap(c -> c.getCode(), Function.identity()));
				for (ValidModifierClass validModifier : modifiedBy.getValidModifierClasses())
				{
					ModifierClass primaryModifier = byCode.get(validModifier.getCode());
					ClaMLIcdNode.createNode(node, icdClass, primaryModifier);
				}
			}
			else
			{
				for (ModifierClass primaryModifier : modifierClasses)
				{
					ClaMLIcdNode.createNode(node, icdClass, primaryModifier);
				}
			}
		}
		else if (isSpecialCase(icdClass))
		{
			createSpecialNodes(node, icdClass, modifierClassesByModifier);
		}
		else if (!icdClass.getModifiedByElements().isEmpty())
			throw new IllegalStateException("unsupported mods: " + icdClass.getModifiedByElements().stream()
					.map(m -> m.getCode()).collect(Collectors.joining(", ")));

		return node;
	}

	protected boolean isSpecialCase(ClaMLClass icdClass)
	{
		return DIABETES_MELLITUS.contains(icdClass.getCode()) && icdClass.getModifiedByElements().size() == 2;
	}

	protected void createSpecialNodes(ClaMLIcdNode node, ClaMLClass icdClass,
			Map<String, List<ModifierClass>> modifierClassesByModifier)
	{
		List<ModifiedBy> modifiedBy = icdClass.getModifiedByElements();

		Optional<ModifiedBy> s04e104 = modifiedBy.stream().filter(m -> DIABETES_MELLITUS_MODIFIER_4.equals(m.getCode()))
				.findFirst();
		Optional<ModifiedBy> s04e105 = modifiedBy.stream().filter(m -> DIABETES_MELLITUS_MODIFIER_5.equals(m.getCode()))
				.findFirst();

		if (s04e104.isPresent() && s04e105.isPresent())
		{
			List<ModifierClass> modifiedByS04e104 = modifierClassesByModifier.get(s04e104.get().getCode());
			List<ModifierClass> modifiedByS04e105 = modifierClassesByModifier.get(s04e105.get().getCode());

			for (ModifierClass primaryModifier : modifiedByS04e104)
			{
				ClaMLIcdNode nodeWithPrimaryModifier = ClaMLIcdNode.createNode(node, icdClass, primaryModifier);

				for (ModifierClass secondaryModifier : modifiedByS04e105)
				{
					if (!secondaryModifier.getMetaElements().stream()
							.filter(m -> META_EXCLUDE_ON_PRECEDING_MODIFIER.equals(m.getName())).map(m -> m.getValue())
							.anyMatch(v -> (primaryModifier.getModifier() + " " + primaryModifier.getCode()).equals(v)))
					{
						ClaMLIcdNode.createNode(nodeWithPrimaryModifier, icdClass, primaryModifier, secondaryModifier);
					}
				}
			}
		}
		else
			throw new IllegalStateException("unsupported mods: " + icdClass.getModifiedByElements().stream()
					.map(m -> m.getCode()).collect(Collectors.joining(", ")));
	}
}

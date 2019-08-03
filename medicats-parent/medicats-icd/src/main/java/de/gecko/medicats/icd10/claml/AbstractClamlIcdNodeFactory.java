package de.gecko.medicats.icd10.claml;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.gecko.medicats.FileSource;
import de.gecko.medicats.claml.ClaMLClassKind;
import de.gecko.medicats.claml.ClaMLReader;
import de.gecko.medicats.claml.Claml;
import de.gecko.medicats.claml.ClamlClass;
import de.gecko.medicats.claml.ModifiedBy;
import de.gecko.medicats.claml.ModifierClass;
import de.gecko.medicats.claml.SubClass;
import de.gecko.medicats.claml.ValidModifierClass;
import de.gecko.medicats.icd10.AbstractIcdNodeFactory;

public abstract class AbstractClamlIcdNodeFactory extends AbstractIcdNodeFactory
{
	private static final List<String> DIABETES_MELLITUS = Arrays.asList("E10", "E11", "E12", "E13", "E14");
	private static final String DIABETES_MELLITUS_MODIFIER_4 = "S04E10_4";
	private static final String DIABETES_MELLITUS_MODIFIER_5 = "S04E10_5";
	private static final String META_EXCLUDE_ON_PRECEDING_MODIFIER = "excludeOnPrecedingModifier";

	private Claml claml;
	private ClamlIcdNodeRoot root;

	protected abstract FileSource getClamlXml();

	protected abstract FileSource getClamlDtd();

	protected synchronized Claml getClaml()
	{
		if (claml == null)
			claml = ClaMLReader.read(getClamlXml().getInputStream(), getClamlDtd().getInputStream());

		return claml;
	}

	@Override
	public synchronized ClamlIcdNodeRoot getRootNode()
	{
		if (root == null)
		{
			Claml claml = getClaml();
			ClamlIcdNodeRoot root = new ClamlIcdNodeRoot(claml, getVersion(), getSortIndex(), getPreviousCodes(),
					getPreviousVersion());

			Optional<ClaMLClassKind> chapterKind = claml.getClaMLClassKinds().getClaMLClassKinds().stream()
					.filter(k -> "chapter".equals(k.getName())).findFirst();

			List<ClamlClass> chapters = claml.getClaMLClasses().stream()
					.filter(c -> c.getKind().equals(chapterKind.get())).collect(Collectors.toList());

			Map<String, ClamlClass> icdClassesByCode = claml.getClaMLClasses().parallelStream()
					.collect(Collectors.toMap(c -> c.getCode(), Function.identity()));

			Map<String, List<ModifierClass>> modifierClassesByModifier = claml.getModifierClasses().stream()
					.collect(Collectors.groupingBy(c -> c.getModifier()));

			for (ClamlClass chapter : chapters)
				createIcdNodes(root, chapter, icdClassesByCode, modifierClassesByModifier);

			this.root = root;
		}

		return root;
	}

	private void createIcdNodes(ClamlIcdNode parent, ClamlClass icdClass, Map<String, ClamlClass> icdClassesByCode,
			Map<String, List<ModifierClass>> modifierClassesByModifier)
	{
		ClamlIcdNode node = createIcdNodes(parent, icdClass, modifierClassesByModifier);

		for (SubClass s : icdClass.getSubClasses())
		{
			ClamlClass icdSubClass = icdClassesByCode.get(s.getCode());

			if (icdSubClass != null)
				createIcdNodes(node, icdSubClass, icdClassesByCode, modifierClassesByModifier);
			// ignore icd codes such as Y35.6, Y36.0, Y36.5
			// else
			// System.out.println(INDENTENTIONS[currentDepth + 1] +
			// s.getCode()
			// + " - ERROR");
		}
	}

	private ClamlIcdNode createIcdNodes(ClamlIcdNode parent, ClamlClass icdClass,
			Map<String, List<ModifierClass>> modifierClassesByModifier)
	{
		ClamlIcdNode node = ClamlIcdNode.createNode(parent, icdClass);

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
					ClamlIcdNode.createNode(node, icdClass, primaryModifier);
				}
			}
			else
			{
				for (ModifierClass primaryModifier : modifierClasses)
				{
					ClamlIcdNode.createNode(node, icdClass, primaryModifier);
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

	protected boolean isSpecialCase(ClamlClass icdClass)
	{
		return DIABETES_MELLITUS.contains(icdClass.getCode()) && icdClass.getModifiedByElements().size() == 2;
	}

	protected void createSpecialNodes(ClamlIcdNode node, ClamlClass icdClass,
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
				ClamlIcdNode nodeWithPrimaryModifier = ClamlIcdNode.createNode(node, icdClass, primaryModifier);

				for (ModifierClass secondaryModifier : modifiedByS04e105)
				{
					if (!secondaryModifier.getMetaElements().stream()
							.filter(m -> META_EXCLUDE_ON_PRECEDING_MODIFIER.equals(m.getName())).map(m -> m.getValue())
							.anyMatch(v -> (primaryModifier.getModifier() + " " + primaryModifier.getCode()).equals(v)))
					{
						ClamlIcdNode.createNode(nodeWithPrimaryModifier, icdClass, primaryModifier, secondaryModifier);
					}
				}
			}
		}
		else
			throw new IllegalStateException("unsupported mods: " + icdClass.getModifiedByElements().stream()
					.map(m -> m.getCode()).collect(Collectors.joining(", ")));
	}
}

package de.gecko.medicats.ops.claml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
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
import de.gecko.medicats.ops.AbstractOpsNodeFactory;

public abstract class AbstractClaMLOpsNodeFactory extends AbstractOpsNodeFactory
{
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
	public ClaMLOpsNodeRoot getRootNode()
	{
		ClaML claML = getClaML();
		ClaMLOpsNodeRoot root = new ClaMLOpsNodeRoot(claML, getVersion(), getPreviousCodes(), getPreviousVersion());

		Optional<ClaMLClassKind> chapterKind = claML.getClaMLClassKinds().getClaMLClassKinds().stream()
				.filter(k -> "chapter".equals(k.getName())).findFirst();

		List<ClaMLClass> chapters = claML.getClaMLClasses().stream().filter(c -> c.getKind().equals(chapterKind.get()))
				.collect(Collectors.toList());

		Map<String, ClaMLClass> clamlClassesByCode = claML.getClaMLClasses().parallelStream()
				.collect(Collectors.toMap(c -> c.getCode(), Function.identity()));

		Map<String, List<ModifierClass>> modifierClassesByModifier = claML.getModifierClasses().stream()
				.collect(Collectors.groupingBy(c -> c.getModifier()));

		for (ClaMLClass chapter : chapters)
			createOpsNodes(root, chapter, clamlClassesByCode, modifierClassesByModifier);

		return root;
	}

	private void createOpsNodes(ClaMLOpsNode parent, ClaMLClass clamlClass, Map<String, ClaMLClass> clamlClassesByCode,
			Map<String, List<ModifierClass>> modifierClassesByModifier)
	{
		ClaMLOpsNode node = createOpsNodes(parent, clamlClass, modifierClassesByModifier);

		for (SubClass s : clamlClass.getSubClasses())
		{
			ClaMLClass clamlSubClass = clamlClassesByCode.get(s.getCode());

			if (clamlSubClass != null)
				createOpsNodes(node, clamlSubClass, clamlClassesByCode, modifierClassesByModifier);
			// ignore icd codes such as Y35.6, Y36.0, Y36.5
			// else
			// System.out.println(INDENTENTIONS[currentDepth + 1] +
			// s.getCode()
			// + " - ERROR");
		}
	}

	private ClaMLOpsNode createOpsNodes(ClaMLOpsNode parent, ClaMLClass clamlClass,
			Map<String, List<ModifierClass>> modifierClassesByModifier)
	{
		ClaMLOpsNode node = ClaMLOpsNode.createNode(parent, clamlClass);

		if (clamlClass.getModifiedByElements().size() == 1)
		{
			ModifiedBy modifiedBy = clamlClass.getModifiedByElements().get(0);
			List<ModifierClass> modifierClasses = modifierClassesByModifier.get(modifiedBy.getCode());

			if (modifiedBy.getValidModifierClasses().size() > 0)
			{
				Map<String, ModifierClass> byCode = modifierClasses.parallelStream()
						.collect(Collectors.toMap(c -> c.getCode(), Function.identity()));

				if (node.getCodeLength() == 5)
				{
					modifiedBy.getValidModifierClasses().stream().filter(v -> v.getCode().length() == 2)
							.forEach(vMod1 ->
							{
								ModifierClass modifier1 = byCode.get(vMod1.getCode());
								ClaMLOpsNode withPrimaryModifier = ClaMLOpsNode.createNode(node, clamlClass, modifier1);

								modifiedBy.getValidModifierClasses().stream()
										.filter(code2 -> code2.getCode().length() > 2
												&& code2.getCode().startsWith(vMod1.getCode()))
										.forEach(vMod2 ->
								{
									ModifierClass modifier2 = byCode.get(vMod2.getCode());
									if (modifier2 != null)
										ClaMLOpsNode.createNode(withPrimaryModifier, clamlClass, modifier2);
								});
							});
				}
				else if (node.getCodeLength() == 7)
				{
					for (ValidModifierClass validModifier : modifiedBy.getValidModifierClasses())
					{
						ModifierClass primaryModifier = byCode.get(validModifier.getCode());

						if (isSpecialCaseWithPrimaryModifier(primaryModifier.getModifier()))
							createSpecialNode(node, clamlClass, primaryModifier, modifierClasses);
						else
							ClaMLOpsNode.createNode(node, clamlClass, primaryModifier);
					}
				}
				else
					throw new IllegalStateException("node code length of " + node.getCodeLength() + " not supported");
			}
			else
			{
				for (ModifierClass primaryModifier : modifierClasses)
				{
					ClaMLOpsNode.createNode(node, clamlClass, primaryModifier);
				}
			}
		}
		else if (!clamlClass.getModifiedByElements().isEmpty())
			throw new IllegalStateException("unsupported mods: " + clamlClass.getModifiedByElements().stream()
					.map(m -> m.getCode()).collect(Collectors.joining(", ")));

		return node;
	}

	protected abstract void createSpecialNode(ClaMLOpsNode parent, ClaMLClass clamlClass, ModifierClass primaryModifier,
			List<ModifierClass> modifierClasses);

	protected abstract boolean isSpecialCaseWithPrimaryModifier(String modifier);
}

package de.gecko.medicats.fhir;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemHierarchyMeaning;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptPropertyComponent;
import org.hl7.fhir.dstu3.model.CodeSystem.PropertyType;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import de.gecko.medicats.NodeWalker;
import de.gecko.medicats.VersionedNode;
import de.gecko.medicats.VersionedNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNode;
import de.gecko.medicats.alphaid.AlphaIdNodeFactory;
import de.gecko.medicats.alphaid.AlphaIdNodeWalker;
import de.gecko.medicats.alphaid.AlphaIdService;
import de.gecko.medicats.icd10.IcdNode;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.IcdService;
import de.gecko.medicats.ops.OpsNode;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.OpsService;

public class CodeSystemProvider implements IResourceProvider
{
	private static final String PROPERTY_CODE_PREVIOUS = "previous";
	private static final String PROPERTY_CODE_PREVIOUS_VALUE = "previous-release";
	private static final String PROPERTY_CODE_INCLUDES = "includes";
	private static final String PROPERTY_CODE_EXCLUDES = "excludes";
	private static final String PROPERTY_CODE_ICD_CODE_PRIMARY = "icd-code-primary";
	private static final String PROPERTY_CODE_ICD_CODE_RELEASE = "icd-code-release";
	private static final String PROPERTY_CODE_ICD_CODE_ASTERIX = "icd-code-asterix";
	private static final String PROPERTY_CODE_ICD_CODE_ADDITIONAL = "icd-code-additional";

	private static final Logger logger = LoggerFactory.getLogger(CodeSystemProvider.class);

	private final List<IcdNodeFactory> icdNodeFactories;
	private final List<OpsNodeFactory> opsNodeFactories;
	private final List<AlphaIdNodeFactory> alphaIdNodeFactories;

	private final Map<String, VersionedNodeFactory<?, ?>> nodeFactoryById = new HashMap<>();

	public CodeSystemProvider(IcdService icdService, OpsService opsService, AlphaIdService alphaIdService)
	{
		icdNodeFactories = icdService.getNodeFactories();
		opsNodeFactories = opsService.getNodeFactories();
		alphaIdNodeFactories = alphaIdService.getNodeFactories();

		icdNodeFactories.forEach(f -> nodeFactoryById.put(f.getVersion(), f));
		opsNodeFactories.forEach(f -> nodeFactoryById.put(f.getVersion(), f));
		alphaIdNodeFactories.forEach(f -> nodeFactoryById.put(f.getVersion(), f));

		logger.debug("Registered CodeSystem Ids {}", nodeFactoryById.keySet().stream().collect(Collectors.toList()));
	}

	@Override
	public Class<? extends IBaseResource> getResourceType()
	{
		return CodeSystem.class;
	}

	@Read
	public CodeSystem getCodeSystemById(@IdParam IIdType theId)
	{
		if (theId.hasVersionIdPart())
		{
			logger.warn("No versioning allowed");
			throw new ResourceNotFoundException(theId);
		}

		String idPart = theId.getIdPart();
		if (!nodeFactoryById.containsKey(idPart))
		{
			logger.warn("No codesystem with id {}", idPart);
			throw new ResourceNotFoundException(theId);
		}

		return createCodeSystem(nodeFactoryById.get(idPart));
	}

	private CodeSystem createCodeSystem(VersionedNodeFactory<?, ?> nodeFactory)
	{
		CodeSystem system = new CodeSystem();
		system.setId(nodeFactory.getVersion());
		system.setName(nodeFactory.getVersion());
		system.setTitle(nodeFactory.getName());

		addVersion(system, nodeFactory);

		if (nodeFactory.getOid() != null && !nodeFactory.getOid().isEmpty())
			system.getIdentifier().setUse(IdentifierUse.OFFICIAL).setSystem("urn:ietf:rfc:3986")
					.setValue("urn:oid:" + nodeFactory.getOid());
		addProperties(system, nodeFactory);
		system.setContent(CodeSystemContentMode.COMPLETE);
		system.setExperimental(false);
		system.setPublisher("Deutschen Instituts für Medizinische Dokumentation und Information (DIMDI)");
		system.getContactFirstRep().getTelecomFirstRep().setSystem(ContactPointSystem.URL)
				.setValue("http://www.dimdi.de");
		setHierachyMeaning(system, nodeFactory);
		system.setCount((int) nodeFactory.createNodeWalker().preOrderStream().count() - 1); // not counting root node
		system.getJurisdictionFirstRep().getCodingFirstRep().setSystem("urn:iso:std:iso:3166").setCode("DE");

		system.setConcept(createConcepts(nodeFactory).collect(Collectors.toList()));

		return system;
	}

	private void setHierachyMeaning(CodeSystem system, VersionedNodeFactory<?, ?> nodeFactory)
	{
		if (nodeFactory instanceof IcdNodeFactory || nodeFactory instanceof OpsNodeFactory)
			system.setHierarchyMeaning(CodeSystemHierarchyMeaning.CLASSIFIEDWITH);
		else if (nodeFactory instanceof AlphaIdNodeFactory)
			system.setHierarchyMeaning(CodeSystemHierarchyMeaning.GROUPEDBY);
	}

	private void addVersion(CodeSystem system, VersionedNodeFactory<?, ?> nodeFactory)
	{
		if (nodeFactory instanceof AlphaIdNodeFactory)
		{
			system.setVersionNeeded(true);
			system.setVersion(String.valueOf(nodeFactory.getSortIndex()));
		}
		else if (nodeFactory instanceof IcdNodeFactory || nodeFactory instanceof OpsNodeFactory)
			system.setVersionNeeded(false);

	}

	private void addProperties(CodeSystem system, VersionedNodeFactory<?, ?> nodeFactory)
	{
		if (nodeFactory instanceof IcdNodeFactory || nodeFactory instanceof OpsNodeFactory)
		{
			system.addProperty().setCode(PROPERTY_CODE_EXCLUDES).setDescription("Exkludiert")
					.setType(PropertyType.CODE);
			system.addProperty().setCode(PROPERTY_CODE_INCLUDES).setDescription("Inkludiert")
					.setType(PropertyType.CODE);
			system.addProperty().setCode(PROPERTY_CODE_PREVIOUS)
					.setDescription("Bisher verwendeter Code im vorherigen Release des Code-Systems")
					.setType(PropertyType.CODE);
			system.addProperty().setCode(PROPERTY_CODE_PREVIOUS_VALUE)
					.setDescription(
							"CodeSystem Version zum bisher verwendeten Code im vorherigen Release des Code-Systems")
					.setType(PropertyType.STRING);
		}
		else if (nodeFactory instanceof AlphaIdNodeFactory)
		{
			system.addProperty().setCode(PROPERTY_CODE_ICD_CODE_RELEASE).setDescription("Release des ICD Codes")
					.setType(PropertyType.STRING);
			system.addProperty().setCode(PROPERTY_CODE_ICD_CODE_ADDITIONAL).setDescription("Sekundärer ICD Code")
					.setType(PropertyType.CODE);
			system.addProperty().setCode(PROPERTY_CODE_ICD_CODE_ASTERIX).setDescription("Asterix ICD Code")
					.setType(PropertyType.CODE);
			system.addProperty().setCode(PROPERTY_CODE_ICD_CODE_PRIMARY).setDescription("Primärer ICD Code")
					.setType(PropertyType.CODE);
		}
	}

	private <N extends VersionedNode<N>, W extends NodeWalker<N>> Stream<ConceptDefinitionComponent> createConcepts(
			VersionedNodeFactory<N, W> nodeFactory)
	{
		NodeWalker<N> w = nodeFactory.createNodeWalker();
		return toConcepts(w, w.getRootNode().children());
	}

	private <N extends VersionedNode<N>> Stream<ConceptDefinitionComponent> toConcepts(NodeWalker<N> walker,
			Stream<N> nodes)
	{
		return nodes.map(n -> toConcept(walker, n));
	}

	private <N extends VersionedNode<N>> ConceptDefinitionComponent toConcept(NodeWalker<N> walker, N node)
	{
		ConceptDefinitionComponent c = toConceptDefinitionComponent(node);
		c.setConcept(Stream.concat(toConcepts(walker, node.children()), createRelatedNodes(walker, node))
				.collect(Collectors.toList()));
		return c;
	}

	private <N extends VersionedNode<N>> ConceptDefinitionComponent toConceptDefinitionComponent(N node,
			ConceptPropertyComponent... properties)
	{
		ConceptDefinitionComponent c = new ConceptDefinitionComponent();
		c.setCode(node.getCode());
		c.setDisplay(node.getLabel());
		if (properties != null && properties.length > 0)
			c.setProperty(Arrays.asList(properties));
		return c;
	}

	private <N extends VersionedNode<N>> Stream<ConceptDefinitionComponent> createRelatedNodes(NodeWalker<N> walker,
			N n)
	{
		if (n instanceof IcdNode)
			return createRelatedIcdNodes((IcdNodeWalker) walker, (IcdNode) n);
		else if (n instanceof OpsNode)
			return createRelatedOpsNodes((OpsNodeWalker) walker, (OpsNode) n);
		else if (n instanceof AlphaIdNode)
			return createRelatedAlphaIdNodes((AlphaIdNodeWalker) walker, (AlphaIdNode) n);
		else
			return Stream.empty();
	}

	private Stream<ConceptDefinitionComponent> createRelatedIcdNodes(IcdNodeWalker walker, IcdNode node)
	{
		// TODO better way to link to previous version of code system
		Stream<ConceptDefinitionComponent> previous = node.getPrevious().map(n -> toConceptDefinitionComponent(n,
				new ConceptPropertyComponent().setCode(PROPERTY_CODE_PREVIOUS), new ConceptPropertyComponent()
						.setCode(PROPERTY_CODE_PREVIOUS_VALUE).setValue(new StringType(n.getPreviousVersion()))))
				.map(Stream::of).orElseGet(Stream::empty);

		Stream<ConceptDefinitionComponent> inclusions = node.getInclusions(walker::getNodesBySudoCode).map(
				n -> toConceptDefinitionComponent(n, new ConceptPropertyComponent().setCode(PROPERTY_CODE_INCLUDES)));
		Stream<ConceptDefinitionComponent> exclusions = node.getExclusions(walker::getNodesBySudoCode).map(
				n -> toConceptDefinitionComponent(n, new ConceptPropertyComponent().setCode(PROPERTY_CODE_EXCLUDES)));

		return Stream.concat(previous, Stream.concat(inclusions, exclusions));
	}

	private Stream<ConceptDefinitionComponent> createRelatedOpsNodes(OpsNodeWalker walker, OpsNode node)
	{
		// TODO better way to link to previous version of code system
		Stream<ConceptDefinitionComponent> previous = node.getPrevious().map(n -> toConceptDefinitionComponent(n,
				new ConceptPropertyComponent().setCode(PROPERTY_CODE_PREVIOUS), new ConceptPropertyComponent()
						.setCode(PROPERTY_CODE_PREVIOUS_VALUE).setValue(new StringType(n.getPreviousVersion()))))
				.map(Stream::of).orElseGet(Stream::empty);

		Stream<ConceptDefinitionComponent> inclusions = node.getInclusions(walker::getNodesBySudoCode).map(
				n -> toConceptDefinitionComponent(n, new ConceptPropertyComponent().setCode(PROPERTY_CODE_INCLUDES)));
		Stream<ConceptDefinitionComponent> exclusions = node.getExclusions(walker::getNodesBySudoCode).map(
				n -> toConceptDefinitionComponent(n, new ConceptPropertyComponent().setCode(PROPERTY_CODE_EXCLUDES)));

		return Stream.concat(previous, Stream.concat(inclusions, exclusions));
	}

	private Stream<ConceptDefinitionComponent> createRelatedAlphaIdNodes(AlphaIdNodeWalker walker, AlphaIdNode node)
	{
		Stream<ConceptDefinitionComponent> primary = node.getPrimaryIcdNode()
				.map(toAlphaIdIcdNodeComponent(PROPERTY_CODE_ICD_CODE_PRIMARY)).map(Stream::of)
				.orElseGet(Stream::empty);
		Stream<ConceptDefinitionComponent> additional = node.getAdditionalIcdNode()
				.map(toAlphaIdIcdNodeComponent(PROPERTY_CODE_ICD_CODE_ADDITIONAL)).map(Stream::of)
				.orElseGet(Stream::empty);
		Stream<ConceptDefinitionComponent> asterix = node.getAsterixIcdNode()
				.map(toAlphaIdIcdNodeComponent(PROPERTY_CODE_ICD_CODE_ASTERIX)).map(Stream::of)
				.orElseGet(Stream::empty);
		
		return Stream.concat(primary, Stream.concat(additional, asterix));
	}

	private Function<IcdNode, ConceptDefinitionComponent> toAlphaIdIcdNodeComponent(String property)
	{
		return n -> toConceptDefinitionComponent(n, new ConceptPropertyComponent().setCode(property),
				new ConceptPropertyComponent().setCode(PROPERTY_CODE_ICD_CODE_RELEASE)
						.setValue(new StringType(n.getVersion())));
	}
}

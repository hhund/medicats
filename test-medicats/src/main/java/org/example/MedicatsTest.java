package org.example;

import de.gecko.medicats.NodeFactory;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.IcdService;
import de.gecko.medicats.icd10.sgml.SgmlIcdNode;
import de.gecko.medicats.ops.OpsNode;
import de.gecko.medicats.ops.OpsNodeFactory;
import de.gecko.medicats.ops.OpsNodeWalker;
import de.gecko.medicats.ops.OpsService;
import de.gecko.medicats.ops.sgml.SgmlOpsNode;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MedicatsTest
{
    public static void main( String[] args )
    {
        testOps();
        System.out.println("\n\n##############\n\n");
        testIcd();
    }

    private static void testOps() {
        System.out.println("OPS Test");
        System.setProperty("dimdi.files.path", "Y:\\Terminology-Resources\\OPS");
        OpsService opsService = OpsService.getService();
        OpsNodeFactory nodeFactory = opsService.getNodeFactory("ops2004");
        System.out.printf("\tLoaded version: %s%n", nodeFactory.getName());
        OpsNodeWalker nodeWalker = nodeFactory.createNodeWalker();
        String code = "1-209";
        SgmlOpsNode targetNode = (SgmlOpsNode) nodeWalker.getNodeByCode(code);
        if (targetNode == null) System.exit(1);
        System.out.printf("\tCode='%s', Display='%s'%n", targetNode.getCode(), targetNode.getLabel());
        System.out.printf("\tInclusions:%n");
        System.out.printf("\t\tcodes=[%s]%n", targetNode.getInclusions(nodeWalker::getNodesBySudoCode).map(OpsNode::getCode).collect(Collectors.joining("; ")));
        System.out.printf("\t\tstrings=[%s]%n", String.join(", ", targetNode.getInclusionStrings()));
        System.out.printf("\tExclusions:%n");
        System.out.printf("\t\tcodes=[%s]%n", targetNode.getExclusions(nodeWalker::getNodesBySudoCode).map(OpsNode::getCode).collect(Collectors.joining("; ")));
        System.out.printf("\t\tstrings=[%s]%n", String.join(", ", targetNode.getExclusionStrings()));
    }

    @SuppressWarnings("unused")
    private static void testIcd() {
        System.out.println("ICD-10-GM Test");
        System.setProperty("dimdi.files.path", "Y:\\Terminology-Resources\\ICD-10-GM");
        IcdService icdService = IcdService.getService();
        IcdNodeFactory nodeFactory = icdService.getNodeFactory("icd10gm2004");
        IcdNodeWalker nodeWalker = nodeFactory.createNodeWalker();
        Scanner scanner = new Scanner(System.in);
        System.out.printf("\tLoaded version: %s%n", nodeFactory.getName());
        String code = "A02.2";
        SgmlIcdNode targetNode = (SgmlIcdNode) nodeWalker.getNodeByCode(code);
        if (targetNode == null) System.exit(1);
        System.out.printf("\tCode='%s', Display='%s'%n", targetNode.getCode(), targetNode.getLabel());
        System.out.printf("\tInclusions:%n");
        System.out.printf("\t\tcodes=[%s]%n", String.join("; ", targetNode.getInclusionCodes()));
        System.out.printf("\t\tstrings=[%s]%n", String.join("; ", targetNode.getInclusionStrings()));
        System.out.printf("\tExclusions:%n");
        System.out.printf("\t\tcodes=[%s]%n", String.join("; ", targetNode.getExclusionCodes()));
        System.out.printf("\t\tstrings=[%s]%n", String.join("; ", targetNode.getExclusionStrings()));
    }
}

package org.example;

import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.IcdService;
import de.gecko.medicats.icd10.sgml.SgmlIcdNode;

import java.util.Scanner;

public class MedicatsTest
{
    public static void main( String[] args )
    {
        System.setProperty("dimdi.files.path", "Y:\\Terminology-Resources\\ICD-10-GM");
        IcdService icdService = IcdService.getService();
        IcdNodeFactory nodeFactory = icdService.getNodeFactory("icd10gm2004");
        IcdNodeWalker nodeWalker = nodeFactory.createNodeWalker();
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Loaded version: %s%n", nodeFactory.getName());
        System.out.print("Code? ");
        String code = scanner.nextLine().trim();
        SgmlIcdNode targetNode = (SgmlIcdNode) nodeWalker.getNodeByCode(code);
        if (targetNode == null) System.exit(1);
        System.out.printf("\tCode='%s', Display='%s'%n", targetNode.getCode(), targetNode.getLabel());
        System.out.printf("\tInclusions:%n");
        System.out.printf("\t\tcodes=[%s]%n", String.join(", ", targetNode.getInclusionCodes()));
        System.out.printf("\t\tstrings=[%s]%n", String.join(", ", targetNode.getInclusionStrings()));
        System.out.printf("\tExclusions:%n");
        System.out.printf("\t\tcodes=[%s]%n", String.join(", ", targetNode.getExclusionCodes()));
        System.out.printf("\t\tstrings=[%s]%n", String.join(", ", targetNode.getExclusionStrings()));
    }
}

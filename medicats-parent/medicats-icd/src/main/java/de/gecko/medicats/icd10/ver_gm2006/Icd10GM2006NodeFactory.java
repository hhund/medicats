package de.gecko.medicats.icd10.ver_gm2006;

import java.util.Arrays;
import java.util.stream.Stream;

import de.gecko.medicats.icd10.FileSource;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.ZipSource;
import de.gecko.medicats.icd10.sgml.AbstractSgmlIcdNodeFactory;

public class Icd10GM2006NodeFactory extends AbstractSgmlIcdNodeFactory implements IcdNodeFactory
{
	private final ZipSource zip = new ZipSource(ZipSource.getBasePath(), "icd10gm2006.zip", 3851066544L);

	private FileSource[] chapterFiles = new FileSource[] { new FileSource(zip, "x1ges2006", "KAP01.sgm"),
			new FileSource(zip, "x1ges2006", "KAP02.sgm"), new FileSource(zip, "x1ges2006", "KAP03.sgm"),
			new FileSource(zip, "x1ges2006", "KAP04.sgm"), new FileSource(zip, "x1ges2006", "KAP05.sgm"),
			new FileSource(zip, "x1ges2006", "KAP06.sgm"), new FileSource(zip, "x1ges2006", "KAP07.sgm"),
			new FileSource(zip, "x1ges2006", "KAP08.sgm"), new FileSource(zip, "x1ges2006", "KAP09.sgm"),
			new FileSource(zip, "x1ges2006", "KAP10.sgm"), new FileSource(zip, "x1ges2006", "KAP11.sgm"),
			new FileSource(zip, "x1ges2006", "KAP12.sgm"), new FileSource(zip, "x1ges2006", "KAP13.sgm"),
			new FileSource(zip, "x1ges2006", "KAP14.sgm"), new FileSource(zip, "x1ges2006", "KAP15.sgm"),
			new FileSource(zip, "x1ges2006", "KAP16.sgm"), new FileSource(zip, "x1ges2006", "KAP17.sgm"),
			new FileSource(zip, "x1ges2006", "KAP18.sgm"), new FileSource(zip, "x1ges2006", "KAP19.sgm"),
			new FileSource(zip, "x1ges2006", "KAP20.sgm"), new FileSource(zip, "x1ges2006", "KAP21.sgm"),
			new FileSource(zip, "x1ges2006", "KAP22.sgm") };

	private FileSource transitionFile = new FileSource(zip, "x1ueb2005_2006", "umsteiger.txt");
	private FileSource systFile = new FileSource(zip, "x1ueb2005_2006", "ICD10V2006.txt");

	@Override
	public String getName()
	{
		return "ICD-10-GM 2006";
	}

	@Override
	public String getOid()
	{
		return "1.2.276.0.76.5.311";
	}

	@Override
	public String getPreviousVersion()
	{
		return "icd10gm2005";
	}

	@Override
	public String getVersion()
	{
		return "icd10gm2006";
	}

	@Override
	public int getSortIndex()
	{
		return 2006;
	}

	@Override
	protected int getChapterCount()
	{
		return chapterFiles.length;
	}

	@Override
	protected Stream<FileSource> getChapterFiles()
	{
		return Arrays.stream(chapterFiles);
	}

	@Override
	protected FileSource getSystFile()
	{
		return systFile;
	}

	@Override
	protected FileSource getTransitionFile()
	{
		return transitionFile;
	}

	@Override
	public IcdNodeWalker createNodeWalker()
	{
		return new Icd10GM2006NodeWalker(getRootNode());
	}
}

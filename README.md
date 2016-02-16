# medicats
Medical classification and terminology systems library for Java 1.8

#### Supported classifications and terminologies

The following classification and terminologies are currently supported by medicats

##### ICD 10
SGB-V 1.3, SGB-V 2.0, GM 2004, GM 2005, GM 2006, GM 2007, GM 2008, GM 2009, GM 2010, GM 2011, GM 2012, GM 2013, GM 2014, GM 2015

##### OPS
SGB-V 2.0, SGB-V, 2.1, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015

##### Alpha-ID
2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015

#### License

medicats is open source under the Apache License - Version 2.0: http://www.apache.org/licenses/LICENSE-2.0

#### Usage

##### Compiling medicats

Checkout medicats-parent and run maven install using maven version >= 3 (https://maven.apache.org):

```
mvn install
```

##### Runtime Dependencies

In order to use this library you need to download additional zip-files from http://www.dimdi.de and place them in a folder defined by the java system property "dimdi.files.path". **Do not unpack or modify the downloaded zip-files.**
The default value for this property is "dimdi". The property can be changed via runtime flag or source code:

```
java -Ddimdi.files.path="special/folder" -jar foo.jar
```

```java
System.setProperty("dimdi.files.path", "special/folder");
```

##### Example Usage

```java
import de.gecko.medicats.icd10.IcdNode;
import de.gecko.medicats.icd10.IcdNodeFactory;
import de.gecko.medicats.icd10.IcdNodeWalker;
import de.gecko.medicats.icd10.IcdService;

public class Example
{
	public static void main(String[] args)
	{
		IcdService icdService = IcdService.getService();
		
		IcdNodeFactory nodeFactory = icdService.getNodeFactory("icd10gm2015");
		
		IcdNodeWalker nodeWalker = nodeFactory.createNodeWalker();
		
		IcdNode dilatedCardiomyopathy = nodeWalker.getNodeByCode("I42.0");
	}
}
```

#### Download Links DIMDI

##### ICD

###### ICD 10 GM 2015
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/version2015/systematik/x1gec2015.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/version2015/metadaten-ueberleitung/x1gut2015.zip

###### ICD 10 GM 2014
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2014/systematik/x1gec2014-20131015.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2014/metadaten-ueberleitung/x1gua2014.zip

###### ICD 10 GM 2013
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2013/systematik/x1gec2013.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2013/metadaten-ueberleitung/x1gua2013.zip

###### ICD 10 GM 2012
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2012/systematik/x1gec2012.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2012/metadaten-ueberleitung/x1ueb2011_2012.zip

###### ICD 10 GM 2011
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2011/systematik/x1gec2011.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2011/metadaten-ueberleitung/x1ueb2010_2011.zip

###### ICD 10 GM 2010
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2010/systematik/x1gec2010.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2010/ueberleitung/x1ueb2009_2010.zip

###### ICD 10 GM 2009
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2009/systematik/x1gex2009.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2009/ueberleitung/x1ueb2008_2009.zip

###### ICD 10 GM 2008
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2008/systematik/x1ges2008.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2008/ueberleitung/x1ueb2007_2008.zip

###### ICD 10 GM 2007
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2007/systematik/x1ges2007.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2007/ueberleitung/x1ueb2006_2007.zip

###### ICD 10 GM 2006
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2006/systematik/x1ges2006.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2006/ueberleitung/x1ueb2005_2006.zip

###### ICD 10 GM 2005
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2005/systematik/x1ges2005.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2005/ueberleitung/x1ueb2004_2005.zip

###### ICD 10 GM 2004
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2004/systematik/x1ges2004.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version2004/ueberleitung/x1ueb20_2004.zip

###### ICD 10 SGB-V 2.0
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version20/systematik/x1ses2_0.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version20/ueberleitung/x1ueb13_20_v11.zip

###### ICD 10 SGB-V 1.3
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version13/systematik/x1ses1_3.zip
- (http://www.dimdi.de/dynamic/de/klassi/downloadcenter/icd-10-gm/vorgaenger/version20/ueberleitung/x1ueb13_20_v11.zip) for testing medicats

##### OPS

###### OPS 2015
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/version2015/systematik/p1sec2015.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/version2015/metadaten-ueberleitung/p1sut2015.zip

###### OPS 2014
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2014/systematik/p1sec2014-20131104.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2014/metadaten-ueberleitung/p1sua2014-20131104.zip

###### OPS 2013
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2013/systematik/p1sec2013.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2013/metadaten-ueberleitung/p1sua2013.zip

###### OPS 2012
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2012/systematik/p1sec2012.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2012/metadaten-ueberleitung/p1ueb2011_2012.zip

###### OPS 2011
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2011/systematik/p1sec2011.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2011/metadaten-ueberleitung/p1ueb2010_2011.zip

###### OPS 2010
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2010/systematik/p1sec2010.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2010/ueberleitung/p1ueb2009_2010.zip

###### OPS 2009
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2009/systematik/p1ses2009.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2009/ueberleitung/p1ueb2008_2009.zip

###### OPS 2008 mit Erweiterung
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2008miterw/systematik/p1ees2008.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2008miterw/ueberleitung/p1ueberw2007_2008.zip

###### OPS 2007 mit Erweiterung
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2007miterw/systematik/p1ees2007.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2007miterw/ueberleitung/p1ueberw2006_2007.zip

###### OPS 2006 mit Erweiterung
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2006miterw/systematik/p1ees2006.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2006miterw/ueberleitung/p1ueberw2005_2006.zip

###### OPS 2005 mit Erweiterung
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2005miterw/systematik/p1ees2005.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2005miterw/ueberleitung/p1ueberw2004_2005_v10.zip

###### OPS 2004 mit Erweiterung
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2004miterw/systematik/p1ees2004.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version2004/ueberleitung/p1ueb21_2004_v10.zip

###### OPS 2.1 mit Erweiterung
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version21miterw/systematik/p1ees2_1.zip
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version21miterw/ueberleitung/p1ueb20_21_v10.zip
- (http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version21miterw/systematik/p1ema2_1.zip) for testing medicats

###### OPS 2.0
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version20/systematik/p1ses2_0.zip
- (http://www.dimdi.de/dynamic/de/klassi/downloadcenter/ops/vorgaenger/version20/ueberleitung/p1ueb11_20_v11.zip) for testing medicats

##### Alpha-ID

###### 2015
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/alpha-id/version2015/alphaid2015.zip

###### 2014
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/alpha-id/vorgaenger/alphaid2014.zip

###### 2013
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/alpha-id/vorgaenger/alphaid2013.zip

###### 2012
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/alpha-id/vorgaenger/alphaid2012.zip

###### 2011
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/alpha-id/vorgaenger/alphaid2011.zip

###### 2010
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/alpha-id/vorgaenger/alphaid2010.zip

###### 2009
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/alpha-id/vorgaenger/alphaid2009.zip

###### 2008
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/alpha-id/vorgaenger/alphaid2008.zip

###### 2007
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/alpha-id/vorgaenger/alphaid2007.zip

###### 2006
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/alpha-id/vorgaenger/alphaid-2006.zip

###### 2005
- http://www.dimdi.de/dynamic/de/klassi/downloadcenter/alpha-id/vorgaenger/dimdi-idt2005.zip

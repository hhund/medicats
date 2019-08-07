# medicats
Medical classification and terminology systems library for Java 1.8

#### Supported classifications and terminologies

The following classification and terminologies are currently supported by medicats

##### ICD 10
SGB-V 1.3, SGB-V 2.0, GM 2004, GM 2005, GM 2006, GM 2007, GM 2008, GM 2009, GM 2010, GM 2011, GM 2012, GM 2013, GM 2014, GM 2015, GM 2016, GM 2017, GM 2018, GM 2019

##### OPS
SGB-V 2.0, SGB-V, 2.1, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019

##### Alpha-ID
2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019

#### License

medicats is open source under the Apache License - Version 2.0: http://www.apache.org/licenses/LICENSE-2.0

#### Usage

##### Compiling medicats

Checkout medicats-parent and run maven install using maven version >= 3 (https://maven.apache.org):

```
mvn install
```

##### Additional Test/Runtime Dependencies

In order to run tests during the build or use this library at runtime you need to download additional zip-files from http://www.dimdi.de and place them in a folder defined by the java system property "dimdi.files.path". **Do not unpack or modify the downloaded zip-files.**
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

##### Medicats Webservice
A current version of the medicats webservice (module medicats-web) can be accessed at: https://medicats.gecko.hs-heilbronn.de

#### Download Links DIMDI

##### ICD

###### ICD 10 GM 2019
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/version2019/icd10gm2019syst-claml.zip
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/version2019/icd10gm2019syst-ueberl.zip

###### ICD 10 GM 2018
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2018.zip

###### ICD 10 GM 2017
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2017.zip

###### ICD 10 GM 2016
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2016.zip

###### ICD 10 GM 2015
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2015.zip

###### ICD 10 GM 2014
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2014.zip

###### ICD 10 GM 2013
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2013.zip

###### ICD 10 GM 2012
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2012.zip

###### ICD 10 GM 2011
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2011.zip

###### ICD 10 GM 2010
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2010.zip

###### ICD 10 GM 2009
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2009.zip

###### ICD 10 GM 2008
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2008.zip

###### ICD 10 GM 2007
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2007.zip

###### ICD 10 GM 2006
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2006.zip

###### ICD 10 GM 2005
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2005.zip

###### ICD 10 GM 2004
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm2004.zip

###### ICD 10 SGB-V 2.0
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm20.zip

###### ICD 10 SGB-V 1.3
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/icd-10-gm/vorgaenger/icd10gm13.zip

##### OPS

###### OPS 2019
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/version2019/ops2019syst-claml.zip
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/version2019/ops2019syst-ueberl.zip

###### OPS 2018
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2018.zip

###### OPS 2017
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2017.zip

###### OPS 2016
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2016.zip

###### OPS 2015
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2015.zip

###### OPS 2014
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2014.zip

###### OPS 2013
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2013.zip

###### OPS 2012
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2012.zip

###### OPS 2011
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2011.zip

###### OPS 2010
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2010.zip

###### OPS 2009
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2009.zip

###### OPS 2008 mit Erweiterung
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2008.zip

###### OPS 2007 mit Erweiterung
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2007.zip

###### OPS 2006 mit Erweiterung
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2006.zip

###### OPS 2005 mit Erweiterung
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2005.zip

###### OPS 2004 mit Erweiterung
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops2004.zip

###### OPS 2.1 mit Erweiterung
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops21.zip

###### OPS 2.0
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/ops/vorgaenger/ops20.zip

##### Alpha-ID

###### 2019
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/version2019/alphaid2019.zip

###### 2018
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/version2018/alphaid2018.zip

###### 2017
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/vorgaenger/alphaid2017.zip

###### 2016
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/vorgaenger/alphaid2016.zip

###### 2015
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/vorgaenger/alphaid2015.zip

###### 2014
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/vorgaenger/alphaid2014.zip

###### 2013
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/vorgaenger/alphaid2013.zip

###### 2012
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/vorgaenger/alphaid2012.zip

###### 2011
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/vorgaenger/alphaid2011.zip

###### 2010
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/vorgaenger/alphaid2010.zip

###### 2009
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/vorgaenger/alphaid2009.zip

###### 2008
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/vorgaenger/alphaid2008.zip

###### 2007
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/vorgaenger/alphaid2007.zip

###### 2006
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/vorgaenger/alphaid2006.zip

###### 2005
- https://www.dimdi.de/dynamic/.downloads/klassifikationen/alpha-id/vorgaenger/dimdi-idt2005.zip

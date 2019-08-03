package de.gecko.medicats.claml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ClaMLReader
{
	private static final String FEATURE_NAMESPACES = "http://xml.org/sax/features/namespaces";
	private static final String FEATURE_NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";

	public static Claml read(InputStream xmlResource, InputStream clamlDtd)
	{
		Objects.requireNonNull(xmlResource, "xmlResource");
		Objects.requireNonNull(clamlDtd, "clamlDtd");

		try
		{
			JAXBContext context = JAXBContext.newInstance(Claml.class);

			Unmarshaller unmarshaller = context.createUnmarshaller();

			XMLReader xmlreader = XMLReaderFactory.createXMLReader();
			xmlreader.setFeature(FEATURE_NAMESPACES, true);
			xmlreader.setFeature(FEATURE_NAMESPACE_PREFIXES, true);
			xmlreader.setEntityResolver((publicId, systemId) ->
			{
				if (systemId.endsWith("ClaML.dtd"))
					return new InputSource(clamlDtd);
				else
					throw new IOException("Entity with id '" + systemId + "' not found");
			});

			InputSource input = new InputSource(xmlResource);
			Source source = new SAXSource(xmlreader, input);

			return (Claml) unmarshaller.unmarshal(source);
		}
		catch (JAXBException | SAXException e)
		{
			throw new RuntimeException(e);
		}
	}
}

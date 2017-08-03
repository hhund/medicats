package de.gecko.medicats.web.rest;

import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XsltTransformer
{
	public StreamingOutput transform(Object o, String xslt)
	{
		return out ->
		{
			try
			{
				JAXBContext context = JAXBContext.newInstance(o.getClass());
				JAXBSource source = new JAXBSource(context, o);

				TransformerFactory factory = TransformerFactory.newInstance();
				factory.setURIResolver(
						(href, base) -> new StreamSource(MedicatsWebservice.class.getResourceAsStream(href)));

				Transformer transformer = factory
						.newTransformer(new StreamSource(MedicatsWebservice.class.getResourceAsStream(xslt)));

				transformer.transform(source, new StreamResult(out));
			}
			catch (JAXBException | TransformerFactoryConfigurationError | TransformerException e)
			{
				throw new RuntimeException(e);
			}
		};
	}
}

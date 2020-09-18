package de.gecko.medicats.ops.sgml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class OpsSgmlReader
{
	private static Map<String, String> cleanupMap = new LinkedHashMap<>();

	static
	{
		cleanupMap.put("<!DOCTYPE OP301 SYSTEM \\\"OP301.DTD\\\">", "");
		cleanupMap.put("(< 24 Stunden)", "(&lt; 24 Stunden)");
		cleanupMap.put("&auml;", "ä");
		cleanupMap.put("&Auml;", "Ä");
		cleanupMap.put("&ouml;", "ö");
		cleanupMap.put("&Ouml;", "Ö");
		cleanupMap.put("&szlig;", "ß");
		cleanupMap.put("&uuml;", "ü");
		cleanupMap.put("&Uuml;", "Ü");

		cleanupMap.put("&sect;", "§");
		cleanupMap.put("&sup1;", "\u00B9");
		cleanupMap.put("&sup2;", "²");
		cleanupMap.put("&sup2;", "²");
		cleanupMap.put("&sup3;", "³");
		cleanupMap.put("&deg;", "°");

		// cleanupMap.put("<!DOCTYPE KAP SYSTEM \\\"KAP.DTD\\\">", "");
		// cleanupMap.put("<TS>", " ");
		// cleanupMap.put("<FNREF REFID=\\\"[A-Z]*[0-9]*\\\">", "");
		// cleanupMap.put("<TABELLE DATEI=\\\"KAP07TAB\\\">", "");
		// cleanupMap.put(" < 35&nbsp;% des Sollwertes", "");
		// cleanupMap.put(" >= 35&nbsp;% und < 50&nbsp;% des Sollwertes", "");
		// cleanupMap.put(" >= 50&nbsp;% und < 70 % des Sollwertes", "");
		// cleanupMap.put(" >= 70&nbsp;% des Sollwertes", "");
		// cleanupMap.put("&aacute;", "á");
		// cleanupMap.put("&Aacute;", "Á");
		// cleanupMap.put("&acirc;", "â");
		// cleanupMap.put("&Acirc;", "Â");
		// cleanupMap.put("&agrave;", "à");
		// cleanupMap.put("&Agrave;", "À");
		// cleanupMap.put("&aring;", "å");
		// cleanupMap.put("&Aring;", "Å");
		// cleanupMap.put("&atilde;", "ã");
		// cleanupMap.put("&Atilde;", "Ã");
		// cleanupMap.put("&auml;", "ä");
		// cleanupMap.put("&Auml;", "Ä");
		// cleanupMap.put("&aelig;", "æ");
		// cleanupMap.put("&AElig;", "Æ");
		// cleanupMap.put("&ccedil;", "ç");
		// cleanupMap.put("&Ccedil;", "Ç");
		// cleanupMap.put("&eth;", "ð");
		// cleanupMap.put("&ETH;", "Ð");
		// cleanupMap.put("&eacute;", "é");
		// cleanupMap.put("&Eacute;", "É");
		// cleanupMap.put("&ecirc;", "ê");
		// cleanupMap.put("&Ecirc;", "Ê");
		// cleanupMap.put("&egrave;", "è");
		// cleanupMap.put("&Egrave;", "È");
		// cleanupMap.put("&euml;", "ë");
		// cleanupMap.put("&Euml;", "Ë");
		// cleanupMap.put("&iacute;", "í");
		// cleanupMap.put("&Iacute;", "Í");
		// cleanupMap.put("&icirc;", "î");
		// cleanupMap.put("&Icirc;", "Î");
		// cleanupMap.put("&igrave;", "Î");
		// cleanupMap.put("&Igrave;", "Í");
		// cleanupMap.put("&iuml;", "ï");
		// cleanupMap.put("&Iuml;", "Ï");
		// cleanupMap.put("&ntilde;", "ñ");
		// cleanupMap.put("&Ntilde;", "Ñ");
		// cleanupMap.put("&oacute;", "ó");
		// cleanupMap.put("&Oacute;", "Ó");
		// cleanupMap.put("&ocirc;", "ô");
		// cleanupMap.put("&Ocirc;", "Ô");
		// cleanupMap.put("&oelig;", "oe");
		// cleanupMap.put("&OElig;", "OE");
		// cleanupMap.put("&ograve;", "ò");
		// cleanupMap.put("&Ograve;", "Ò");
		// cleanupMap.put("&oslash;", "ø");
		// cleanupMap.put("&Oslash;", "Ø");
		// cleanupMap.put("&otilde;", "õ");
		// cleanupMap.put("&Otilde;", "Õ");
		// cleanupMap.put("&ouml;", "ö");
		// cleanupMap.put("&Ouml;", "Ö");
		// cleanupMap.put("&scaron;", "s");
		// cleanupMap.put("&szlig;", "ß");
		// cleanupMap.put("&thorn;", "þ");
		// cleanupMap.put("&THORN;", "Þ");
		// cleanupMap.put("&uacute;", "ú");
		// cleanupMap.put("&Uacute;", "Ú");
		// cleanupMap.put("&ucirc;", "û");
		// cleanupMap.put("&Ucirc;", "Û");
		// cleanupMap.put("&ugrave;", "ù");
		// cleanupMap.put("&Ugrave;", "Ù");
		// cleanupMap.put("&uuml;", "ü");
		// cleanupMap.put("&Uuml;", "Ü");
		// cleanupMap.put("&yacute;", "ý");
		// cleanupMap.put("&Yacute;", "Ý");
		// cleanupMap.put("&yuml;", "y");
		// cleanupMap.put("&dagger;", "+");
		// cleanupMap.put("&ast;", "*");
		// cleanupMap.put("&deg;", "º");
		// cleanupMap.put("&alpha;", "alpha");
		// cleanupMap.put("&beta;", "beta");
		// cleanupMap.put("&gamma;", "gamma");
		// cleanupMap.put("&sect;", "§");
		// cleanupMap.put("&num;", "#");
		// cleanupMap.put("&loz;", "<>");
		// cleanupMap.put("&minus;", "");
		// cleanupMap.put("&reg;", "(R)");
		// cleanupMap.put("&nbsp;", " ");
		// cleanupMap.put("&nbhy;", "");
	}

	public static Element read(String sgmlChapterFileName)
	{
		try
		{
			String data = IOUtils.toString(OpsSgmlReader.class.getResourceAsStream("/" + sgmlChapterFileName),
					StandardCharsets.UTF_8);
			for (Entry<String, String> e : cleanupMap.entrySet())
				data = data.replaceAll(e.getKey(), e.getValue());

			InputStream in = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setIgnoringComments(true);
			dbFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			return dBuilder.parse(in).getDocumentElement();
		}
		catch (IOException | ParserConfigurationException | SAXException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Element read(InputStream sgmlStream)
	{
		try
		{
			String data = IOUtils.toString(sgmlStream, StandardCharsets.UTF_8);
			for (Entry<String, String> e : cleanupMap.entrySet())
				data = data.replaceAll(e.getKey(), e.getValue());

			InputStream in = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setIgnoringComments(true);
			dbFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			return dBuilder.parse(in).getDocumentElement();
		}
		catch (IOException | ParserConfigurationException | SAXException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				sgmlStream.close();
			}
			catch (IOException e)
			{
				throw new RuntimeException();
			}
		}
	}
}

package fr.utbm.vi51.group11.lemmings.utils.configuration;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import fr.utbm.vi51.group11.lemmings.controller.ErrorController;
import fr.utbm.vi51.group11.lemmings.utils.statics.FileUtils1;

public class GlobalConfiguration extends HashMap<String, Object>
{

	/** Instance of the singleton */
	private final static GlobalConfiguration	s_globalConfiguration	= new GlobalConfiguration();

	/** Default private constructor called only once */
	private GlobalConfiguration()
	{
		try
		{
			/* Instantiates all Objects and builders to allow xpath to work */
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder;
			documentBuilder = documentBuilderFactory.newDocumentBuilder();

			/* Parses the file containing the configuration of the maps */
			Document document = documentBuilder.parse(FileUtils1.USER_CONFIGURATION_DIR.resolve(
					FileUtils1.LEVEL_CONF_FILENAME).toString());
			XPath xpath = XPathFactory.newInstance().newXPath();

			Node s = (Node) xpath.compile("camera/width").evaluate(document, XPathConstants.NODE);
			System.out.println("string : " + s.getTextContent());
		} catch (ParserConfigurationException | SAXException | IOException
				| XPathExpressionException exception)
		{
			ErrorController.addPendingException(exception);
		}
	}

	/**
	 * @return Instance of the singleton.
	 */
	public static GlobalConfiguration getInstance()
	{
		return s_globalConfiguration;
	}

}

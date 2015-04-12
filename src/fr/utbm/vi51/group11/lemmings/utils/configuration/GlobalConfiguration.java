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

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fr.utbm.vi51.group11.lemmings.utils.enums.GlobalConfigurationEnum;
import fr.utbm.vi51.group11.lemmings.utils.statics.FileUtils1;

public class GlobalConfiguration extends HashMap<GlobalConfigurationEnum, Object>
{

	private static final long					serialVersionUID		= 1L;

	/** Logger of the class */
	private final static Logger					s_LOGGER				= LoggerFactory
																				.getLogger(GlobalConfiguration.class);

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
			Document document = documentBuilder.parse(FileUtils1.RESOURCES_DIR.resolve(
					FileUtils1.LEVEL_CONF_FILENAME).toString());
			// Document document =
			// documentBuilder.parse("resources/configuration.xml");
			XPath xpath = XPathFactory.newInstance().newXPath();

			parseInstallationPath(document, xpath);

			parseCamera(document, xpath);

		} catch (ParserConfigurationException | SAXException | IOException
				| XPathExpressionException exception)
		{
			s_LOGGER.error("{}", exception);
		}
	}

	private void parseInstallationPath(
			final Document document,
			final XPath xpath) throws XPathExpressionException
	{
		String installationPath = (String) xpath.compile("configuration/installationPath")
				.evaluate(document, XPathConstants.STRING);
		this.put(GlobalConfigurationEnum.INSTALLATION_PATH, installationPath);
	}

	private void parseCamera(
			final Document document,
			final XPath xpath) throws XPathExpressionException
	{
		float cameraXCoords = Float.parseFloat((String) xpath.compile("configuration/camera/x")
				.evaluate(document, XPathConstants.STRING));
		float cameraYCoords = Float.parseFloat((String) xpath.compile("configuration/camera/y")
				.evaluate(document, XPathConstants.STRING));
		float cameraWidth = Float.parseFloat((String) xpath.compile("configuration/camera/width")
				.evaluate(document, XPathConstants.STRING));
		float cameraHeight = Float.parseFloat((String) xpath.compile("configuration/camera/height")
				.evaluate(document, XPathConstants.STRING));

		this.put(GlobalConfigurationEnum.CAMERA_RECTANGLE, new Rectangle2f(cameraXCoords,
				cameraYCoords, cameraWidth, cameraHeight));
	}

	/**
	 * @return Instance of the singleton.
	 */
	public static GlobalConfiguration getInstance()
	{
		return s_globalConfiguration;
	}
}
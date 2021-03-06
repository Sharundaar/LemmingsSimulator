package fr.utbm.vi51.group11.lemmings.utils.configuration.level;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.vmutil.Resources;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.utbm.vi51.group11.lemmings.gui.texture.TextureBank;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsFile;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class LevelPropertiesMap extends HashMap<String, LevelProperties>
{
	/**
	 * 
	 */
	private static final long				serialVersionUID		= 1L;

	/** Logger of the class */
	private final static Logger				s_LOGGER				= LoggerFactory
																			.getLogger(LevelPropertiesMap.class);

	/** Instance of the singleton */
	private final static LevelPropertiesMap	s_levelPropertiesMap	= new LevelPropertiesMap();

	/*----------------------------------------------*/

	private LevelPropertiesMap()
	{
		s_LOGGER.debug("Creation of the MapProperties...");

		NodeList nodeList;
		NodeList levelList;
		String tempString = "";
		String tileSpriteSheet = "";
		String id;
		int nbRow, nbCol;
		int[][] tileGrid;
		Set<String> textureIDs = new HashSet<String>();
		MultivaluedMap<String, WorldEntityConfiguration> worldEntitiesConfiguration;
		Point2f entityCoord = null;

		try
		{
			/* Instantiates all Objects and builders to allow xpath to work */
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder;
			documentBuilder = documentBuilderFactory.newDocumentBuilder();

			/* Parses the file containing the configuration of the maps */
			// Document document =
			// documentBuilder.parse(FileUtils1.RESOURCES_DIR.resolve(
			// FileUtils1.LEVEL_CONF_FILENAME).toString()); TODO change path
			Document document = documentBuilder.parse(Resources
					.getResourceAsStream(UtilsFile.LEVEL_CONF_FILENAME));

			XPath xpath = XPathFactory.newInstance().newXPath();

			levelList = (NodeList) xpath.compile("levels/level").evaluate(document,
					XPathConstants.NODESET);

			if (levelList.getLength() == 0)
				s_LOGGER.error("List of level is empty. Please check the resource file 'level.xml' for any errors concerning <levels>/<level> tags.");

			/* Pour chaque balise <level> */
			for (int index = 0; index < levelList.getLength(); ++index)
			{

				worldEntitiesConfiguration = new MultivaluedMapImpl<String, WorldEntityConfiguration>();
				/* Gets the ID of the level. */
				id = levelList.item(index).getAttributes().item(0).getTextContent();

				/* Retrieves the tilespriteSheet. */
				tileSpriteSheet = (String) xpath.compile(
						"levels/level[@id='" + id + "']/tileSpriteSheet").evaluate(document,
						XPathConstants.STRING);
				textureIDs.add(tileSpriteSheet);

				/* Retrieves the Texture tile grid of the map */
				tempString = StringUtils.removePattern(
						(String) xpath.compile("levels/level[@id='" + id + "']/tileGrid").evaluate(
								document, XPathConstants.STRING), "\t\t");
				nbRow = StringUtils.countMatches(tempString, "\n") - 1;
				nbCol = (StringUtils.countMatches(tempString, "\t") / nbRow) + 1;

				tileGrid = new int[nbCol][nbRow];

				/* Creation of a scanner to get the next integers */
				Scanner sc = new Scanner(tempString);

				/* Transforms the String into a matrix of integers */
				for (int i = 0; i < nbRow; ++i)
					for (int j = 0; j < nbCol; ++j)
						tileGrid[j][i] = sc.nextInt();

				/* Closes the scanner */
				sc.close();

				/*
				 * Retrieves the list of world entities and adds them to
				 * the list
				 */
				nodeList = (NodeList) xpath.compile(
						"levels/level[@id='" + id + "']/worldEntities/worldEntity").evaluate(
						document, XPathConstants.NODESET);
				for (int i = 0; i < nodeList.getLength(); ++i)
				{
					/* Retrieves the entity's world coordinates */
					entityCoord = new Point2f(Float.parseFloat(nodeList.item(i).getChildNodes()
							.item(1).getTextContent()), Float.parseFloat(nodeList.item(i)
							.getChildNodes().item(3).getTextContent()));
					if ((entityCoord.x() >= (nbCol * UtilsLemmings.s_tileWidth))
							|| (entityCoord.x() < 0)
							|| (entityCoord.y() >= (nbRow * UtilsLemmings.s_tileHeight))
							|| (entityCoord.y() < 0))
					{
						s_LOGGER.error(
								"Entity-{} : Wrong coordinates, entityCoords.x > or < to bounds (or y).",
								i);
					} else
					{

						/* Adds a new WorldEntity to the map */
						worldEntitiesConfiguration.add(nodeList.item(i).getAttributes().item(0)
								.getTextContent(), new WorldEntityConfiguration(entityCoord,
						/* Retrieves the textureID */
						nodeList.item(i).getChildNodes().item(5).getTextContent()));

						/*
						 * Adds the textureID to the list given afterwards to
						 * the
						 * textureBank
						 */
						textureIDs.add(nodeList.item(i).getChildNodes().item(5).getTextContent());
					}
				}

				/* Creates and add a new LevelProperties to this */
				this.put(id, new LevelProperties(id, tileSpriteSheet, tileGrid,
						worldEntitiesConfiguration));
			}

			s_LOGGER.debug("MapProperties created.\n{}", this.toString());

			/* Loads texture with the given ids */
			TextureBank.getInstance().loadTextures(textureIDs);

		} catch (ParserConfigurationException | SAXException | IOException
				| XPathExpressionException exception)
		{
			s_LOGGER.error("{}", exception);
		}
	}

	/*----------------------------------------------*/

	/**
	 * @return Instance of the singleton.
	 */
	public static LevelPropertiesMap getInstance()
	{
		return s_levelPropertiesMap;
	}

	/*----------------------------------------------*/

	@Override
	public String toString()
	{
		String disp = "";
		for (String s : this.keySet())
			disp += this.get(s);

		disp += "\n";

		return disp;
	}

	/*----------------------------------------------*/

	public void destroy()
	{
		s_levelPropertiesMap.clear();
	}
}
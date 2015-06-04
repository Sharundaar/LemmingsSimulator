package fr.utbm.vi51.group11.lemmings.utils.configuration.level;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelProperties
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger										s_LOGGER	= LoggerFactory
																						.getLogger(LevelProperties.class);

	/** ID of the Level */
	private final String											m_id;

	/** Matrix representing the texture for each cell of the grid of the map */
	private final int[][]											m_tileGrid;

	/** List of cells containing a starting point */
	private final MultivaluedMap<String, WorldEntityConfiguration>	m_worldEntitiesConfiguration;

	/*----------------------------------------------*/

	public LevelProperties(final String _id, final int[][] _tileGrid,
			final MultivaluedMap<String, WorldEntityConfiguration> worldEntities)
	{
		m_id = _id;
		m_tileGrid = _tileGrid;
		m_worldEntitiesConfiguration = worldEntities;
	}

	/*----------------------------------------------*/

	public int getNbRow()
	{
		return m_tileGrid[0].length;
	}

	/*----------------------------------------------*/

	public int getNbCol()
	{
		return m_tileGrid.length;
	}

	/*----------------------------------------------*/

	public int[][] getTileGrid()
	{
		return m_tileGrid;
	}

	/*----------------------------------------------*/

	public MultivaluedMap<String, WorldEntityConfiguration> getWorldEntitiesConfiguration()
	{
		return m_worldEntitiesConfiguration;
	}

	/*----------------------------------------------*/

	@Override
	public String toString()
	{
		int nbRow = getNbRow();
		int nbCol = getNbCol();
		String disp = "";
		disp += "\n************************************";
		disp += "\n*   LevelProperties ID : '" + m_id + "'";
		disp += "\n*   Size (w*h) : " + nbCol + "*" + nbRow;
		disp += "\n*   TileGrid   : ";

		for (int i = 0; i < nbRow; ++i)
			for (int j = 0; j < nbCol; ++j)
			{
				if ((i != 0) && (j == 0))
					disp += "\n*                ";
				disp += "\t" + m_tileGrid[j][i];
			}

		disp += "\n*";
		disp += "\n*   WorldEntities : ";

		for (String key : m_worldEntitiesConfiguration.keySet())
		{
			disp += "\n*                   ";
			disp += m_worldEntitiesConfiguration.get(key).size() + " " + key + " - ";
			disp += m_worldEntitiesConfiguration.get(key);
		}
		disp += "\n*";
		disp += "\n************************************";

		return disp;
	}
}
package fr.utbm.vi51.group11.lemmings.utils.configuration;

import java.util.Iterator;

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

	/** Height of the map */
	private final int												m_nbRow;

	/** Width of the map */
	private final int												m_nbCol;

	/** Matrix representing the texture for each cell of the grid of the map */
	private final int[][]											m_tileGrid;

	/** List of cells containing a starting point */
	private final MultivaluedMap<String, WorldEntityConfiguration>	m_worldEntitiesConfiguration;

	/*----------------------------------------------*/

	public LevelProperties(final String _id, final int _nbRow, final int _nbCol,
			final int[][] _tileGrid,
			final MultivaluedMap<String, WorldEntityConfiguration> worldEntities)
	{
		m_id = _id;
		m_nbRow = _nbRow;
		m_nbCol = _nbCol;
		m_tileGrid = _tileGrid;
		m_worldEntitiesConfiguration = worldEntities;
	}

	/*----------------------------------------------*/

	public int getNbRow()
	{
		return m_nbRow;
	}

	/*----------------------------------------------*/

	public int getNbCol()
	{
		return m_nbCol;
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
		String disp = "";
		disp += "\n************************************";
		disp += "\n*   LevelProperties ID : '" + m_id + "'";
		disp += "\n*   Size (w*h) : " + m_nbCol + "*" + m_nbRow;
		disp += "\n*   TileGrid   : ";

		for (int i = 0; i < m_nbRow; ++i)
			for (int j = 0; j < m_nbCol; ++j)
			{
				if ((i != 0) && (j == 0))
					disp += "\n*                ";
				disp += "\t" + m_tileGrid[i][j];
			}

		disp += "\n*";
		disp += "\n*   WorldEntities : ";

		Iterator<String> iterator = m_worldEntitiesConfiguration.keySet().iterator();
		String key = iterator.next();
		String oldKey = key;
		disp += key + " - " + m_worldEntitiesConfiguration.get(key);

		while (iterator.hasNext())
		{
			key = iterator.next();
			disp += "\n*                   ";
			if (!oldKey.equals(key))
				disp += key + " - ";
			else
				disp += "   ";
			disp += m_worldEntitiesConfiguration.get(key);
		}
		disp += "\n*";
		disp += "\n************************************";

		return disp;
	}
}
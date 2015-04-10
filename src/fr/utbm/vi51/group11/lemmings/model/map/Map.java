package fr.utbm.vi51.group11.lemmings.model.map;

import org.arakhne.afc.math.discrete.object2d.Vector2i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelProperties;
import fr.utbm.vi51.group11.lemmings.utils.enums.CellType;

public class Map extends WorldEntity
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(Map.class);

	/** Discrete Grid of cells containing the environment */
	private final Grid			m_grid;

	/*----------------------------------------------*/

	/**
	 * Only constructor. Initializes the grid.
	 * 
	 * @param _rowNb
	 *            Number of rows splitting the screen.
	 * @param _colNb
	 *            Number of columns splitting the screen.
	 */
	public Map(final LevelProperties _levelProperties)
	{
		m_grid = new Grid(_levelProperties.getNbRow(), _levelProperties.getNbCol(),
				_levelProperties.getTileGrid());
	}

	/*----------------------------------------------*/

	/**
	 * Method used to get the type of a cell located in _x and in _y.
	 * 
	 * @param _x
	 *            x float coordinate.
	 * @param _y
	 *            y float coordinate.
	 * @return The type of the cell that contains this (x,y) couple.
	 */
	public CellType getCellType(
			final float _x,
			final float _y)
	{
		final Vector2i cellPosition = continuToDiscreteCoordinates(_x, _y);
		return m_grid.getCell(cellPosition.x(), cellPosition.y()).getCellType();
	}

	/*----------------------------------------------*/

	/**
	 * Method used to transform the continue coordinates of an entity into a
	 * [row][col] format. Returns the cell in the grid containing these
	 * coordinates.
	 * 
	 * @param _x
	 *            Continu x coordinate of the entity.
	 * @param _y
	 *            Continu x coordinate of the entity.
	 * @return Vector of integer containing the position of the cell in the grid
	 *         where the entity is located.
	 */
	private Vector2i continuToDiscreteCoordinates(
			final float _x,
			final float _y)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
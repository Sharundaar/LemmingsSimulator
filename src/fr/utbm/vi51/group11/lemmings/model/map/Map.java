package fr.utbm.vi51.group11.lemmings.model.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.discrete.object2d.Vector2i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.javafx.iio.ImageStorage.ImageType;

import fr.utbm.vi51.group11.lemmings.gui.texture.Sprite;
import fr.utbm.vi51.group11.lemmings.gui.texture.Texture;
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
	
	private final static int CELL_SIZE = 32;
	private final static int TYPE_ARGB = 2;
	
	private Texture m_texture;
	private BufferedImage m_image;
	private Graphics2D m_imageGraphics;
	

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
		
		this.m_worldCoords = new Point2f(0, 0); 
		
		m_image = new BufferedImage(_levelProperties.getNbRow()*CELL_SIZE, _levelProperties.getNbCol()*CELL_SIZE, TYPE_ARGB);
		m_imageGraphics = m_image.createGraphics();
		m_texture = new Texture("Map", m_image);
		
		this.m_sprite = new Sprite(
				m_worldCoords.getX(), m_worldCoords.getY(), m_grid.getWidth() * CELL_SIZE, m_grid.getHeight() * CELL_SIZE, // world components 
				0, 0, m_grid.getWidth() * CELL_SIZE, m_grid.getHeight() * CELL_SIZE, // texture components
				m_texture); // image
	
		
	}
	
	/*----------------------------------------------*/
	
	/**
	 * Redraw the map in the m_image attribute
	 */
	public void redrawMap()
	{
		m_imageGraphics.clearRect(0, 0, m_image.getWidth(), m_image.getHeight());
		
		for(int i=0 ; i<m_grid.getWidth() ; ++i)
		{
			for(int j=0 ; j<m_grid.getHeight() ; ++j)
			{
				Vector2f cellPos = discreteToContinuCoordinates(i, j);
				switch(getCellType(i, j, true))
				{
				case BACK_WALL:
					m_imageGraphics.setColor(Color.GRAY);
					break;
				case DIRT:
				case GRASS:
				case STONE:
				case TOXIC:
				case PIT:
				case ATTRACTIVE_FIELD:
				case REPULSIVE_FIELD:
					m_imageGraphics.setColor(Color.GREEN);
					break;
					
				default:
					break;
				
				}
				
				m_imageGraphics.fillRect(cellPos.x(), cellPos.y(), CELL_SIZE, CELL_SIZE);
			}
		}
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
			final float _y,
			boolean _gridCoord)
	{
		if(_gridCoord)
			return m_grid.getCell((int)_x, (int)_y).getCellType();
		
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
		/* First go from world space to map space */
		float mapX = _x - this.m_worldCoords.getX();
		float mapY = _y - this.m_worldCoords.getY();
		
		/* Then from map space to grid space */
		int gridX = (int) (mapX % m_grid.getWidth());
		int gridY = (int) (mapY / m_grid.getHeight());
		
		return new Vector2i(gridX, gridY);
	}
	
	/*----------------------------------------------*/
	/**
	 * Invert of the continuToDiscreteCoordinates
	 * 
	 * @param _x coordinate in grid space
	 * @param _y coordinate in grid space
	 * @return vector equivalent to the upper-left coordinate of the [_x][_y] grid cell
	 */
	private Vector2f discreteToContinuCoordinates(
			final int _x,
			final int _y)
	{
		
		return new Vector2f(m_worldCoords.getX() + _x * CELL_SIZE, m_worldCoords.getY() + _y * CELL_SIZE);
	}
}
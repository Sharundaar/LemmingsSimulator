package fr.utbm.vi51.group11.lemmings.model.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.discrete.object2d.Vector2i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.gui.texture.Sprite;
import fr.utbm.vi51.group11.lemmings.gui.texture.Texture;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.physics.properties.CollisionProperty;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionMask;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.RectangleShape;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelProperties;
import fr.utbm.vi51.group11.lemmings.utils.enums.CellType;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;

public class Map extends WorldEntity
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(Map.class);

	/** Discrete Grid of cells containing the environment */
	private final Grid			m_grid;

	public 	final static int	CELL_SIZE	= 32;
	private final static int	TYPE_ARGB	= 2;

	private final Texture		m_texture;
	private final BufferedImage	m_image;
	private final Graphics2D	m_imageGraphics;

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

		m_image = new BufferedImage(_levelProperties.getNbCol() * CELL_SIZE,
				_levelProperties.getNbRow() * CELL_SIZE, TYPE_ARGB);
		m_imageGraphics = m_image.createGraphics();
		m_texture = new Texture("Map", m_image);
		m_type = WorldEntityEnum.MAP;
		
		this.m_sprite = new Sprite(m_worldCoords.getX(), m_worldCoords.getY(), m_grid.getWidth()
				* CELL_SIZE, m_grid.getHeight() * CELL_SIZE, // world components
				0, 0, m_grid.getWidth() * CELL_SIZE, m_grid.getHeight() * CELL_SIZE, // texture
																						// components
				m_texture); // image
		
		updateCollisionMask();
	}

	/*----------------------------------------------*/

	/**
	 * Redraw the map in the m_image attribute
	 */
	public void redrawMap()
	{
		m_imageGraphics.clearRect(0, 0, m_image.getWidth(), m_image.getHeight());

		for (int i = 0; i < m_grid.getWidth(); ++i)
		{
			for (int j = 0; j < m_grid.getHeight(); ++j)
			{
				Vector2f cellPos = discreteToContinueCoordinates(i, j);
				switch (getCellType(i, j, true))
				{
					case BACK_WALL:
						m_imageGraphics.setColor(Color.GRAY);
						break;
					case DIRT:
					case GRASS:
					case STONE:
						m_imageGraphics.setColor(Color.GREEN);
						break;
					case TOXIC:
					case PIT:
					case ATTRACTIVE_FIELD:
					case REPULSIVE_FIELD:
						m_imageGraphics.setColor(Color.YELLOW);
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
			final boolean _gridCoord)
	{
		if (_gridCoord)
			return m_grid.getCell((int) _x, (int) _y).getCellType();

		final Vector2i cellPosition = continueToDiscreteCoordinates(_x, _y);
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
	private Vector2i continueToDiscreteCoordinates(
			final float _x,
			final float _y)
	{
		/* First go from world space to map space */
		float mapX = _x - this.m_worldCoords.getX();
		float mapY = _y - this.m_worldCoords.getY();

		/* Then from map space to grid space */
		int gridX = (int) Math.floor((_x / CELL_SIZE));
		int gridY = (int) Math.floor(_y / CELL_SIZE);

		return new Vector2i(gridX, gridY);
	}

	/*----------------------------------------------*/
	/**
	 * Invert of the continuToDiscreteCoordinates
	 * 
	 * @param _x
	 *            coordinate in grid space
	 * @param _y
	 *            coordinate in grid space
	 * @return vector equivalent to the upper-left coordinate of the [_x][_y]
	 *         grid cell
	 */
	private Vector2f discreteToContinueCoordinates(
			final int _x,
			final int _y)
	{

		return new Vector2f(m_worldCoords.getX() + (_x * CELL_SIZE), m_worldCoords.getY()
				+ (_y * CELL_SIZE));
	}
	
	private void updateCollisionMask()
	{
		m_collisionMask = new CollisionMask(m_worldCoords);
		for(int i=0; i<m_grid.getHeight(); ++i)
		{
			int start = 0, end = -1;
			boolean thisisasecretboolean = false;
			for(int j=0; j<m_grid.getWidth(); ++j)
			{
				CellType tcell = this.getCellType(j, i, true);
				// we wan't to test collision in these two cases (not crossable or dangerous)
				if(!tcell.isCrossable() || tcell.isDangerous())
				{
					if(end < 0)
					{
						end = start;
						if(!tcell.isCrossable())
							thisisasecretboolean = false;
						else if(tcell.isDangerous())
							thisisasecretboolean = true;		
					}
					else
					{
						if((!tcell.isCrossable() && thisisasecretboolean) || (tcell.isDangerous() && !thisisasecretboolean))
						{
							RectangleShape rect = buildRectangleShape(start, end, i);
							m_collisionMask.addChild(rect);
							
							start = j;
							end = start;
							if(!tcell.isCrossable())
								thisisasecretboolean = false;
							else if(tcell.isDangerous())
								thisisasecretboolean = true;	
						}
						else
						{
							end++;
						}
					}
				}
				else 
				{
					if(end >= 0) // two cases, or end >= 0 in which case we need to create a collision box of at least 1 cell
					{
						RectangleShape rect = buildRectangleShape(start, end, i);
						m_collisionMask.addChild(rect);
						
						start = end+2;
						end = -1;
					}
					else // or we're not creating a collision box so we move start point
					{
						start++;
					}
				}
			}
			
			if(end != -1)
			{
				RectangleShape rect = buildRectangleShape(start, end, i);
				m_collisionMask.addChild(rect);
			}
		}
		
		m_collisionMask.updateShape();
	}
	
	private RectangleShape buildRectangleShape(int _start, int _end, int _height)
	{
		RectangleShape rect = new RectangleShape(_start * CELL_SIZE, _height * CELL_SIZE, ((_end+1)-_start) * CELL_SIZE, CELL_SIZE, m_collisionMask);
		CollisionProperty prop = new CollisionProperty();
		switch(this.getCellType(_start, _height, true))
		{
		case DIRT:
		case GRASS:
		case STONE:
			prop.setCrossable(false);
			break;
		case TOXIC:
			prop.setCrossable(true);
			RectangleShape killZone = new RectangleShape(0, 12, rect.getWidth(), rect.getHeight()-12, rect);
			prop.setDangerous(true, killZone);
			rect.addChild(killZone);
			killZone.updateShape();
			break;
		
		// These ones should not generate collision boxes
		case BACK_WALL:
			break;
		case PIT:
			break;
			
		// no property for now
		case ATTRACTIVE_FIELD:
			break;
		case REPULSIVE_FIELD:
			break;
			
		default:
			break;
		}
		
		prop.setEntity(this);
		rect.setProperty(prop);
		return rect;
	}
	
	public LinkedList<Cell> getCellInArea(Point2f _position, CollisionShape _area)
	{
		LinkedList<Cell> result = new LinkedList<Cell>();
		
		
		Vector2i mapPos = continueToDiscreteCoordinates(_position.getX(), _position.getY());
		int i=mapPos.x();
		int j=mapPos.y();
		while(i<m_grid.getWidth() && _area.collide(m_grid.getCell(i, j).getRectangle()))
		{
			while(j<m_grid.getHeight() && _area.collide(m_grid.getCell(i, j).getRectangle()))
			{
				result.add(m_grid.getCell(i, j));
				++j;
			}
			j=mapPos.y();
			++i;
		}
		
		
		return result;
	}
}
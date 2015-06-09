package fr.utbm.vi51.group11.lemmings.model.entity.immobile.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.discrete.object2d.Vector2i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.gui.texture.Sprite;
import fr.utbm.vi51.group11.lemmings.gui.texture.Texture;
import fr.utbm.vi51.group11.lemmings.gui.texture.TextureBank;
import fr.utbm.vi51.group11.lemmings.model.Environment;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.physics.properties.CollisionProperty;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionMask;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.RectangleShape;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape.PhysicType;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelProperties;
import fr.utbm.vi51.group11.lemmings.utils.enums.CellType;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.ITextureHandler;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class Map extends WorldEntity implements ITextureHandler
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(Map.class);

	/** Discrete Grid of cells containing the environment */
	private final Grid			m_grid;

	private final static int	TYPE_ARGB	= 2;

	private Texture				m_spriteSheet;
	private final Texture		m_texture;
	private final BufferedImage	m_image;
	private final Graphics2D	m_imageGraphics;
	
	private Environment m_environment;

	/*----------------------------------------------*/

	/**
	 * Only constructor. Initializes the grid.
	 * 
	 * @param _rowNb
	 *            Number of rows splitting the screen.
	 * @param _colNb
	 *            Number of columns splitting the screen.
	 */
	public Map(final LevelProperties _levelProperties, Environment _environment)
	{
		m_grid = new Grid(_levelProperties.getNbRow(), _levelProperties.getNbCol(),
				_levelProperties.getTileGrid());

		this.m_worldCoords = new Point2f(0, 0);

		TextureBank.getInstance().getTexture(_levelProperties.getTileSpriteSheet(), this);

		m_image = new BufferedImage(_levelProperties.getNbCol() * UtilsLemmings.s_tileWidth,
				_levelProperties.getNbRow() * UtilsLemmings.s_tileHeight, TYPE_ARGB);
		m_imageGraphics = m_image.createGraphics();
		m_texture = new Texture("Map", m_image);

		this.m_sprite = new Sprite(
		/* world coordinates */
		m_worldCoords.getX(), m_worldCoords.getY(), m_grid.getWidth() * UtilsLemmings.s_tileWidth,
				m_grid.getHeight() * UtilsLemmings.s_tileHeight,
				/* texture coordinates */
				0, 0, m_grid.getWidth() * UtilsLemmings.s_tileWidth, m_grid.getHeight()
						* UtilsLemmings.s_tileHeight,
				/* associated Texture. */
				m_texture);
		m_type = WorldEntityEnum.MAP;

		m_environment = _environment;
		
		updateCollisionMask();
	}
	
	/*----------------------------------------------*/
	public boolean canDigCell(float x, float y)
	{
		if(x>getWidth() || y > getHeight())
			return false;
		
		CellType cellType = getCellType(x, y, false);
		return !cellType.isCrossable();
	}
	
	/*----------------------------------------------*/
	public boolean canFillCell(float x, float y)
	{
		if(x>getWidth() || y > getHeight())
			return false;
		
		CellType cellType = getCellType(x, y, false);
		return cellType.isCrossable() && !cellType.isDangerous();
	}
	
	/*----------------------------------------------*/
	public void digCell(float x, float y)
	{
		if(canDigCell(x, y))
		{
			Cell cell = m_grid.getCell((int) (x / UtilsLemmings.s_tileWidth), (int)(y / UtilsLemmings.s_tileHeight));
			cell.setCellType(CellType.BACK_WALL);
			for(CollisionShape shape : m_collisionMask.getChilds())
			{
				m_environment.getPhysicEngine().removeShape(shape);
			}
			updateCollisionMask();
			for(CollisionShape shape : m_collisionMask.getChilds())
			{
				m_environment.getPhysicEngine().addShape(shape, PhysicType.STATIC);
			}
		}
	}

	/*----------------------------------------------*/
	public void fillCell(float x, float y)
	{
		if(canFillCell(x, y))
		{
			Cell cell = m_grid.getCell((int) (x / UtilsLemmings.s_tileWidth), (int)(y / UtilsLemmings.s_tileHeight));
			cell.setCellType(CellType.STONE);
			for(CollisionShape shape : m_collisionMask.getChilds())
			{
				m_environment.getPhysicEngine().removeShape(shape);
			}
			updateCollisionMask();
			for(CollisionShape shape : m_collisionMask.getChilds())
			{
				m_environment.getPhysicEngine().addShape(shape, PhysicType.STATIC);
			}
		}
	}
	
	/*----------------------------------------------*/
	public int getGridWidth()
	{
		return m_grid.getWidth();
	}

	/*----------------------------------------------*/
	public int getGridHeight()
	{
		return m_grid.getHeight();
	}

	/*----------------------------------------------*/

	/**
	 * Redraw the map in the m_image attribute
	 */
	public void redrawMap()
	{
		BufferedImage image = null;
		m_imageGraphics.clearRect(0, 0, m_image.getWidth(), m_image.getHeight());

		for (int i = 0; i < m_grid.getWidth(); ++i)
		{
			for (int j = 0; j < m_grid.getHeight(); ++j)
			{
				Vector2f cellPos = discreteToContinueCoordinates(i, j);
				switch (getCellType(i, j, true))
				{
					case BACK_WALL:
						drawCell(image, cellPos, Color.GRAY, 0, 0);
						break;
					case DIRT:
						drawCell(image, cellPos, Color.ORANGE, 4, 0); // TODO
						break;
					case GRASS:
						drawCell(image, cellPos, Color.GREEN, 5, 0); // TODO
					case STONE:
						drawCell(image, cellPos, Color.YELLOW, 3, 0); // TODO
						break;
					case TOXIC:
						/* Dark green color otherwise. */
						drawCell(image, cellPos, Color.getHSBColor(120, 100, 50), 1, 0);
					case PIT:
						drawCell(image, cellPos, Color.PINK, 2, 0); // TODO
					case ATTRACTIVE_FIELD:
						drawCell(image, cellPos, Color.LIGHT_GRAY, 6, 0); // TODO
					case REPULSIVE_FIELD:
						drawCell(image, cellPos, Color.DARK_GRAY, 7, 0); // TODO
						break;

					default:
						break;

				}
			}
		}
	}

	private void drawCell(
			BufferedImage _image,
			final Vector2f _cellPos,
			final Color _color,
			final int _xTexCoord,
			final int _yTexCoord)
	{
		if (m_spriteSheet == null)
		{
			m_imageGraphics.setColor(_color);
			m_imageGraphics.fillRect(_cellPos.x(), _cellPos.y(), UtilsLemmings.s_tileWidth,
					UtilsLemmings.s_tileHeight);
		} else
		{
			_image = m_spriteSheet.getImage().getSubimage(_xTexCoord * UtilsLemmings.s_tileWidth,
					_yTexCoord * UtilsLemmings.s_tileHeight, UtilsLemmings.s_tileWidth,
					UtilsLemmings.s_tileHeight);
			m_imageGraphics.drawImage(_image, _cellPos.x(), _cellPos.y(), null);
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
		int gridX = (int) Math.floor((_x / UtilsLemmings.s_tileWidth));
		int gridY = (int) Math.floor(_y / UtilsLemmings.s_tileHeight);

		return new Vector2i(gridX, gridY);
	}
	
	/*----------------------------------------------*/
	public float getWidth()
	{
		return getGridWidth() * UtilsLemmings.s_tileWidth;
	}
	
	/*----------------------------------------------*/
	public float getHeight()
	{
		return getGridHeight() * UtilsLemmings.s_tileHeight;
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

		return new Vector2f(m_worldCoords.getX() + (_x * UtilsLemmings.s_tileWidth),
				m_worldCoords.getY() + (_y * UtilsLemmings.s_tileHeight));
	}

	private void updateCollisionMask()
	{
		m_collisionMask = new CollisionMask(m_worldCoords);
		for (int i = 0; i < m_grid.getHeight(); ++i)
		{
			int start = 0, end = -1;
			boolean thisisasecretboolean = false;
			for (int j = 0; j < m_grid.getWidth(); ++j)
			{
				CellType tcell = this.getCellType(j, i, true);
				// we wan't to test collision in these two cases (not crossable
				// or dangerous)
				if (!tcell.isCrossable() || tcell.isDangerous())
				{
					if (end < 0)
					{
						end = start;
						if (!tcell.isCrossable())
							thisisasecretboolean = false;
						else if (tcell.isDangerous())
							thisisasecretboolean = true;
					} else
					{
						if ((!tcell.isCrossable() && thisisasecretboolean)
								|| (tcell.isDangerous() && !thisisasecretboolean))
						{
							RectangleShape rect = buildRectangleShape(start, end, i);
							m_collisionMask.addChild(rect);

							start = j;
							end = start;
							if (!tcell.isCrossable())
								thisisasecretboolean = false;
							else if (tcell.isDangerous())
								thisisasecretboolean = true;
						} else
						{
							end++;
						}
					}
				} else
				{
					if (end >= 0) // two cases, or end >= 0 in which case we
									// need to create a collision box of at
									// least 1 cell
					{
						RectangleShape rect = buildRectangleShape(start, end, i);
						m_collisionMask.addChild(rect);

						start = end + 2;
						end = -1;
					} else
					// or we're not creating a collision box so we move start
					// point
					{
						start++;
					}
				}
			}

			if (end != -1)
			{
				RectangleShape rect = buildRectangleShape(start, end, i);
				m_collisionMask.addChild(rect);
			}
		}

		m_collisionMask.updateShape();
	}

	private RectangleShape buildRectangleShape(
			final int _start,
			final int _end,
			final int _height)
	{
		RectangleShape rect = new RectangleShape(_start * UtilsLemmings.s_tileWidth, _height
				* UtilsLemmings.s_tileHeight, ((_end + 1) - _start) * UtilsLemmings.s_tileWidth,
				UtilsLemmings.s_tileHeight, m_collisionMask);
		CollisionProperty prop = new CollisionProperty();
		switch (this.getCellType(_start, _height, true))
		{
			case DIRT:
			case GRASS:
			case STONE:
				prop.setCrossable(false);
				break;
			case TOXIC:
				prop.setCrossable(true);
				RectangleShape killZone = new RectangleShape(0, 12, rect.getWidth(),
						rect.getHeight() - 12, rect);
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

	public LinkedList<Cell> getCellInArea(
			final Point2f _position,
			final CollisionShape _area)
	{
		LinkedList<Cell> result = new LinkedList<Cell>();

		Vector2i mapPos = continueToDiscreteCoordinates(_position.getX(), _position.getY());
		int i = mapPos.x();
		int j = mapPos.y();
		while ((i < m_grid.getWidth()) && _area.collide(m_grid.getCell(i, j).getRectangle()))
		{
			while ((j < m_grid.getHeight()) && _area.collide(m_grid.getCell(i, j).getRectangle()))
			{
				result.add(m_grid.getCell(i, j));
				++j;
			}
			j = mapPos.y();
			++i;
		}

		return result;
	}

	@Override
	public void receiveTexture(
			final Texture _texture)
	{
		m_spriteSheet = _texture;
	}
}
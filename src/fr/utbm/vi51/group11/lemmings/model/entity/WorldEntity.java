package fr.utbm.vi51.group11.lemmings.model.entity;

import org.arakhne.afc.math.continous.object2d.Point2f;

import fr.utbm.vi51.group11.lemmings.gui.texture.Sprite;
import fr.utbm.vi51.group11.lemmings.model.physics.CollisionMask;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;

/**
 * Super Abstract Class representing an entity existing in an environment.
 * 
 * @author jnovak
 *
 */
public abstract class WorldEntity implements IPerceivable
{
	/** Type of the WorldEntity */
	protected WorldEntityEnum	m_type;

	/** Position of the WorldEntity on the grid */
	protected Point2f			m_coordinates;

	/** Shape of the entity. Used for collisions */
	protected CollisionMask		m_collisionMask;

	/** Sprite of the entity */
	protected Sprite			m_sprite;

	/*----------------------------------------------*/

	/**
	 * @return ID of the WorldEntity.
	 */
	public WorldEntityEnum getType()
	{
		return m_type;
	}

	/*----------------------------------------------*/

	/**
	 * @return Position (x,y) of the WorldEntity.
	 */
	public Point2f getCoordinates()
	{
		return m_coordinates;
	}

	/*----------------------------------------------*/

	/**
	 * @return Shape of the WorldEntity.
	 */
	public CollisionMask getCollisionMask()
	{
		return m_collisionMask;
	}

	/*----------------------------------------------*/

	/**
	 * Method used to set the ID of a WorldEntity.
	 * 
	 * @param m_id
	 *            ID of the WorldEntity.
	 */
	public void setType(
			final WorldEntityEnum _type)
	{
		this.m_type = _type;
	}

	/*----------------------------------------------*/

	/**
	 * Method used to set the position of the WorldEntity.
	 * 
	 * @param _position
	 *            New Position of the WorldEntity.
	 */
	public void setCoordinates(
			final Point2f _position)
	{
		this.m_coordinates.set(_position);
	}

	/*----------------------------------------------*/

	/**
	 * Method used to set the position of the WorldEntity.
	 * 
	 * @param _x
	 *            New x coordinate of the WorldEntity.
	 * @param _y
	 *            New y coordinate of the WorldEntity.
	 * 
	 */
	public void setCoordinates(
			final float _x,
			final float _y)
	{
		this.m_coordinates.setX(_x);
		this.m_coordinates.setY(_y);
	}

	public Sprite getSprite()
	{
		return m_sprite;
	}
}
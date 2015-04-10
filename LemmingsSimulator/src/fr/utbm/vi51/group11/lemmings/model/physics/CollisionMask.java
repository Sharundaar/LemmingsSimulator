package fr.utbm.vi51.group11.lemmings.model.physics;

import org.arakhne.afc.math.continous.object2d.Point2f;

public class CollisionMask
{
	/** Absolute coordinates */
	private Point2f	m_coordinates;

	public CollisionMask()
	{
		m_coordinates = new Point2f();
	}
	
	/**
	 * Get the coordinate of the CollisionMask
	 * @return coordinates in World space (Point2f)
	 */
	public Point2f getCoordinates()
	{
		return m_coordinates;
	}
}

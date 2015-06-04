package fr.utbm.vi51.group11.lemmings.model.physics;

import java.util.LinkedList;

import org.arakhne.afc.math.continous.object2d.Point2f;

public class CollisionMask
{
	/** Absolute coordinates */
	private Point2f	m_coordinates;
	
	/** List of sub-shapes */
	private LinkedList<CollisionShape> m_shapes = new LinkedList<CollisionShape>();

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
	
	public LinkedList<CollisionShape> getShapes()
	{
		return m_shapes;
	}
}

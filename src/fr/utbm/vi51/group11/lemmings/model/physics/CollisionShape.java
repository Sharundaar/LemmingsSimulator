package fr.utbm.vi51.group11.lemmings.model.physics;

import java.util.LinkedList;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;

public abstract class CollisionShape {
	
	private CollisionShapeType m_type;
	
	protected Point2f m_coordinates; // Global coordinates if m_parent == null, coordinate in parent ref if not
	protected LinkedList<CollisionShape> m_childs = new LinkedList<CollisionShape>();
	
	protected CollisionShape m_parent;
	
	protected Shape2f m_shape;
	
	protected CollisionShape(CollisionShapeType _type, CollisionShape _parent)
	{
		m_parent = _parent;
		m_type = _type;
		
		m_coordinates = new Point2f();
	}
	
	public enum CollisionShapeType
	{
		CIRCLE,
		RECTANGLE,
		MASK,
	}
	
	public Point2f computeGlobalCoordinates()
	{
		CollisionShape current = this;
		Point2f result = new Point2f();
		while(current != null)
		{
			final Point2f currentCoords = current.getCoordinates();
			result.addX(currentCoords.getX());
			result.addY(currentCoords.getY());
			
			current = current.getParent();
		}
		
		return result;
	}
	
	public CollisionShape getParent()
	{
		return m_parent;
	}
	
	public LinkedList<CollisionShape> getChilds()
	{
		return m_childs;
	}
	
	public CollisionShapeType getType()
	{
		return m_type;
	}
	
	public boolean collide(CollisionShape _shape)
	{
		updateShape();
		
		switch(_shape.getType())
		{
		case CIRCLE:
			return m_shape.intersects((Circle2f)_shape.getShape());
		case MASK:
			return ((CollisionMask)_shape).collide(this);
		case RECTANGLE:
			return m_shape.intersects((Rectangle2f)_shape.getShape());
		default:
			return false;
		}
	}
	
	public Shape2f getShape()
	{
		return m_shape;
	}
	
	/**
	 * Get the coordinate (can be interpreted as global or local depending on the shape, see computeGlobalCoordinates())
	 * @return coordinates in World space (Point2f)
	 */
	public Point2f getCoordinates()
	{
		return m_coordinates;
	}
	
	public abstract void updateShape();
}

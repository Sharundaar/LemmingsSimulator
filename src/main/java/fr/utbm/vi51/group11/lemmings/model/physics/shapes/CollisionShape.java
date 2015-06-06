package fr.utbm.vi51.group11.lemmings.model.physics.shapes;

import java.util.LinkedList;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;

import fr.utbm.vi51.group11.lemmings.model.physics.properties.CollisionProperty;

public abstract class CollisionShape {
	
	private CollisionShapeType m_type;
	
	protected Point2f m_coordinates; // Global coordinates if m_parent == null, coordinate in parent ref if not
	protected LinkedList<CollisionShape> m_childs = new LinkedList<CollisionShape>();
	
	protected CollisionShape m_parent;
	protected PhysicType m_phyType;
	
	protected Shape2f m_shape;
	
	protected Point2f m_computedCoordinates = new Point2f();
	
	protected CollisionProperty m_property = null;
	
	protected CollisionShape(CollisionShapeType _type, CollisionShape _parent)
	{
		setParent(_parent);
		m_type = _type;
		
		m_coordinates = new Point2f();
	}
	
	public enum CollisionShapeType
	{
		CIRCLE,
		RECTANGLE,
		MASK,
	}
	
	public enum PhysicType
	{
		STATIC,
		DYNAMIC
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
		
		m_computedCoordinates.set(result.getX(), result.getY());
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
		
		try
		{
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
		catch(NullPointerException _e)
		{
			System.out.println(_e.getMessage());
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
		return this.m_coordinates;
	}
	
	public Point2f getCoordinates(boolean _global)
	{
		if(_global)
			return m_computedCoordinates;
		else
			return m_coordinates;
	}
	
	private void setParent(CollisionShape _shape)
	{
		if(m_parent != null && m_parent != _shape)
			m_parent.removeShape(this);
		m_parent = _shape;
	}
	
	public void addChild(CollisionShape _shape)
	{
		if(!m_childs.contains(_shape))
		{
			m_childs.add(_shape);
			_shape.setParent(this);
		}
	}
	
	public void removeShape(CollisionShape _shape)
	{
		if(m_childs.contains(_shape))
		{
			m_childs.remove(_shape);
			_shape.setParent(null);
		}
	}
	
	public PhysicType getPhysicType()
	{
		return m_phyType;
	}
	
	public void setPhysicType(PhysicType _type)
	{
		m_phyType = _type;
	}
	
	public CollisionProperty getProperty()
	{
		return m_property;
	}
	
	public void setProperty(CollisionProperty _property)
	{
		m_property = _property;
	}
	
	public abstract void updateShape();
}

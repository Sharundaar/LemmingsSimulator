package fr.utbm.vi51.group11.lemmings.model.physics;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Point2f;

public class CircleShape extends CollisionShape {

	public CircleShape(float _radius, CollisionShape _parent)
	{
		super(CollisionShapeType.CIRCLE, _parent);
		m_shape = new Circle2f(0, 0, _radius);
	}
	
	public CircleShape(Point2f _coordinates, float _radius, CollisionShape _parent)
	{
		super(CollisionShapeType.CIRCLE, _parent);
		m_coordinates.set(_coordinates);
		m_shape = new Circle2f(0, 0, _radius);
	}
	
	public void updateShape()
	{
		((Circle2f)m_shape).setCenter(computeGlobalCoordinates());
	}
	
	public float getRadius()
	{
		return ((Circle2f)m_shape).getRadius();
	}
	
	public Circle2f getCircle()
	{
		return (Circle2f)m_shape;
	}
}

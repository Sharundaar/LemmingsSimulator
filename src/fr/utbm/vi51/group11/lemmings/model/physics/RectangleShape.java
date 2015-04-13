package fr.utbm.vi51.group11.lemmings.model.physics;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;

public class RectangleShape extends CollisionShape {
	
	public RectangleShape(float _width, float _height, CollisionShape _parent) {
		super(CollisionShapeType.RECTANGLE, _parent);

		m_shape = new Rectangle2f(0, 0, _width, _height);
	}
	
	public RectangleShape(float _x, float _y, float _width, float _height, CollisionShape _parent) {
		super(CollisionShapeType.RECTANGLE, _parent);

		m_coordinates.set(_x, _y);
		m_shape = new Rectangle2f(0, 0, _width, _height);
	}
	
	public void updateShape()
	{
		Point2f globalCoords = computeGlobalCoordinates();
		Rectangle2f rectangle = getRectangle();
		
		rectangle.set(globalCoords.getX(), globalCoords.getY(), rectangle.getWidth(), rectangle.getHeight());
	}

	public Rectangle2f getRectangle()
	{
		return (Rectangle2f)m_shape;
	}
	
	
}

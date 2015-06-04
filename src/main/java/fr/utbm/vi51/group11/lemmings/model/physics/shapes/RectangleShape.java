package fr.utbm.vi51.group11.lemmings.model.physics.shapes;

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
	
	public RectangleShape(Point2f _p1, Point2f _p2, CollisionShape _parent)
	{
		super(CollisionShapeType.RECTANGLE, _parent);
		
		m_coordinates.set(_p1.x(), _p1.y());
		m_shape = new Rectangle2f(0, 0, _p2.x() - _p1.x(), _p2.y() - _p1.y());
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
	
	public Point2f getCenter()
	{
		Rectangle2f rect = getRectangle();
		return new Point2f(rect.getCenterX(), rect.getCenterY());
	}
	
	public Point2f getUpperLeft()
	{
		Rectangle2f rect = getRectangle();
		return new Point2f(rect.getMinX(), rect.getMinY());
	}
	
	public Point2f getUpperRight()
	{
		Rectangle2f rect = getRectangle();
		return new Point2f(rect.getMaxX(), rect.getMinY());
	}
	
	public Point2f getBottomLeft()
	{
		Rectangle2f rect = getRectangle();
		return new Point2f(rect.getMinX(), rect.getMaxY());
	}
	
	public Point2f getBottomRight()
	{
		Rectangle2f rect = getRectangle();
		return new Point2f(rect.getMaxX(), rect.getMaxY());
	}
	
	public Point2f getUpper()
	{
		Rectangle2f rect = getRectangle();
		return new Point2f(rect.getCenterX(), rect.getMinY());
	}
	
	public Point2f getBottom()
	{
		Rectangle2f rect = getRectangle();
		return new Point2f(rect.getCenterX(), rect.getMaxY());
	}
	
	public Point2f getLeft()
	{
		Rectangle2f rect = getRectangle();
		return new Point2f(rect.getMinX(), rect.getCenterY());
	}
	
	public Point2f getRight()
	{
		Rectangle2f rect = getRectangle();
		return new Point2f(rect.getMaxX(), rect.getCenterY());
	}
	
	public float getWidth()
	{
		return getRectangle().getWidth();
	}
	
	public float getHeight()
	{
		return getRectangle().getHeight();
	}
	
}

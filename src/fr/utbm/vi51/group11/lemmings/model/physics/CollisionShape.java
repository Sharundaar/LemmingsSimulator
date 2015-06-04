package fr.utbm.vi51.group11.lemmings.model.physics;

import java.util.LinkedList;

public abstract class CollisionShape {
	private LinkedList<CollisionShape> m_childs = new LinkedList<CollisionShape>();
	
	public enum CollisionShapeType
	{
		CIRCLE,
		RECTANGLE,
	}
	
	public LinkedList<CollisionShape> getChilds()
	{
		return m_childs;
	}
	
	public abstract CollisionShapeType getType();
	public abstract boolean collide(CollisionShape _shape);
}

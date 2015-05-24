package fr.utbm.vi51.group11.lemmings.model.physics;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.physics.quadtree.QuadTree;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionMask;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape;

public class PhysicEngine
{
	/** Logger of the class */
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(PhysicEngine.class);

	/** Value of the force of Gravity */
	private final static float	s_GRAVITY	= 9.8f;

	/** QuadTree splitting the screen */
	private final QuadTree		m_quadTree;
	private final LinkedList<CollisionShape> m_collisionShapes = new LinkedList<>();

	public PhysicEngine()
	{
		s_LOGGER.debug("Creation of the PhysicsEngine...");

		m_quadTree = new QuadTree();

		s_LOGGER.debug("PhysicsEngine created.");
	}

	public boolean collide(CollisionMask _m1, CollisionMask _m2)
	{
		/** TODO */
		return _m1.collide(_m2);
	}
	
	public void addShape(CollisionShape _shape)
	{
		if(_shape == null)
			return;
		
		m_collisionShapes.add(_shape);
		m_quadTree.add(_shape);
	}
	
	public void removeShape(CollisionShape _shape)
	{
		m_collisionShapes.remove(_shape);
		m_quadTree.remove(_shape);
	}
	
	public LinkedList<CollisionShape> getCollidingEntities()
	{
		LinkedList<CollisionShape> result = new LinkedList<CollisionShape>();
		
		
		
		return result;
	}

	/**
	 * @return the quadTree
	 */
	public QuadTree getQuadTree() {
		return m_quadTree;
	}

	public void update() {
		// TODO Auto-generated method stub
		m_quadTree.updateElements(m_collisionShapes);
	}
}

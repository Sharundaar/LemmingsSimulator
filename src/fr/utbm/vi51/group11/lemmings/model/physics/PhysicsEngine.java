package fr.utbm.vi51.group11.lemmings.model.physics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhysicsEngine
{
	/** Logger of the class */
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(PhysicsEngine.class);

	/** Value of the force of Gravity */
	private final static float	s_GRAVITY	= 9.8f;

	/** QuadTree splitting the screen */
	private final QuadTree		m_quadTree;

	public PhysicsEngine()
	{
		s_LOGGER.debug("Creation of the PhysicsEngine...");

		m_quadTree = new QuadTree();

		s_LOGGER.debug("PhysicsEngine created.");
	}

	public boolean collide(CollisionMask _m1, CollisionMask _m2)
	{
		/** TODO */
		return false;
	}
}

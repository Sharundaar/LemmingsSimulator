package fr.utbm.vi51.group11.lemmings.utils.misc;

import java.awt.geom.Point2D;
import java.util.Iterator;

import fr.utbm.vi51.group11.lemmings.model.Environment;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;

/**
 * Class designed to represent the field of perception of a Mobile World Entity.
 * Its children must implement the Iterator pattern enabling the environment to
 * iterate through the environment with a specific iterator. It optimizes the
 * search for objects according to specific conditions. Child Frustrums
 * (inheriting this class) must contain a private static Iterator class that
 * defines the different functions of an iterator (hasNext() and next(),
 * remove() is not mandatory).
 * 
 * @author jnovak
 *
 */
public abstract class Frustrum
{
	protected CollisionShape m_shape;
	
	public CollisionShape getShape()
	{
		return m_shape;
	}
}

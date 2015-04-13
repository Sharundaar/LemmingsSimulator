package fr.utbm.vi51.group11.lemmings.model.physics;

import java.util.LinkedList;

import org.arakhne.afc.math.continous.object2d.Point2f;

public class CollisionMask extends CollisionShape
{
	public CollisionMask()
	{
		super(CollisionShapeType.MASK, null);
	}
	
	public CollisionMask(Point2f _coordinates)
	{
		super(CollisionShapeType.MASK, null);
		m_coordinates.set(_coordinates);
	}

	@Override
	public CollisionShapeType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean collide(CollisionShape _shape) {
		/** TODO: implementation */
		return false;
	}

	@Override
	public void updateShape() {
		// Recursive refresh ?
		
	}
	
}

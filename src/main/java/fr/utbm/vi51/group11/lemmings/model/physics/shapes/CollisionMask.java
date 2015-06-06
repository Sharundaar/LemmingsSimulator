package fr.utbm.vi51.group11.lemmings.model.physics.shapes;

import java.util.LinkedList;

import org.arakhne.afc.math.continous.object2d.Point2f;

import fr.utbm.vi51.group11.lemmings.model.physics.properties.CollisionProperty;

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
	public boolean collide(CollisionShape _shape) {
		/** TODO: implementation */
		
		for(CollisionShape shape : m_childs)
		{
			if(collideRec(shape, _shape))
			{
				return true;
			}
			else
				continue;
		}
		
		return false;
	}
	
	private boolean collideRec(CollisionShape _s1, CollisionShape _s2)
	{
		if(!_s1.collide(_s2))
		{
			return false;
		}
		else
		{
			for(CollisionShape shape : _s2.getChilds()) // here we go down one level on _s2
			{
				if(!_s1.collide(shape))
					continue;
				else
				{
					return collideRec(shape, _s1); // invert the parameters here so we go down one level on _s1
				}
			}
			
			// If we arrive here it means that _s2 has no childs but maybe _s1 has
			for(CollisionShape shape : _s1.getChilds())
			{
				if(!shape.collide(_s2)) // shape doesn't collide _s2
					continue;
				else
				{
					return collideRec(_s2, shape); // s2 has no child so we want to go deeper in shape (no inuendo intended)
				}
			}
			
			// If we arrive here then s1 collide with s2, and s1/s2 are leafs so the conclusion should be true
			return true;
		}
	}

	@Override
	public void updateShape() {
		// Recursive refresh ?
		for(CollisionShape shape : this.m_childs)
			updateShape(shape);
	}
	
	private void updateShape(CollisionShape _shape)
	{
		_shape.updateShape();
		for(CollisionShape shape : _shape.getChilds())
			updateShape(shape);
	}
}

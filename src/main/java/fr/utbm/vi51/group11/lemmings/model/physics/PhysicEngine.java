package fr.utbm.vi51.group11.lemmings.model.physics;

import java.util.LinkedList;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.DynamicEntity;
import fr.utbm.vi51.group11.lemmings.model.map.Cell;
import fr.utbm.vi51.group11.lemmings.model.map.Map;
import fr.utbm.vi51.group11.lemmings.model.physics.collidingobjects.CollidingObjects;
import fr.utbm.vi51.group11.lemmings.model.physics.collidingobjects.CollidingObjectsSet;
import fr.utbm.vi51.group11.lemmings.model.physics.quadtree.QuadTree;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionMask;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape.PhysicType;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.RectangleShape;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;
import fr.utbm.vi51.group11.lemmings.utils.statics.LemmingUtils;

public class PhysicEngine
{
	/** Logger of the class */
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(PhysicEngine.class);

	/** Value of the force of Gravity */
	private final static float	s_GRAVITY	= 2920.96f / (1000.0f*1000.0f);

	/** QuadTree splitting the screen */
	private final QuadTree		m_quadTree;
	private final LinkedList<CollisionShape> m_staticObjects = new LinkedList<>();
	private final LinkedList<CollisionShape> m_dynamicObjects = new LinkedList<>();

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
	
	public void addShape(CollisionShape _shape, PhysicType _type)
	{
		if(_shape == null)
			return;
		
		_shape.setPhysicType(_type);
		switch(_type)
		{
		case DYNAMIC:
			m_dynamicObjects.add(_shape);
			break;
		case STATIC:
			m_staticObjects.add(_shape);
			break;
		default:
			break;
			
		}
		m_quadTree.add(_shape);
	}
	
	public void removeShape(CollisionShape _shape)
	{
		m_staticObjects.remove(_shape);
		m_dynamicObjects.remove(_shape);
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
		m_quadTree.updateElements(m_dynamicObjects);
	}
	
	public void updateQuadTree()
	{
		m_quadTree.updateElements(m_dynamicObjects);
	}
	
	public void applyExternForces(long _dt)
	{
		for(CollisionShape shape : m_dynamicObjects)
		{
			DynamicEntity ent = (DynamicEntity) shape.getData();
			applyGravity(ent, _dt);
			
			clampSpeed(ent);
		}
	}
	
	public void clampSpeed(DynamicEntity _ent)
	{
		if(_ent.getSpeed().getX() > LemmingUtils.s_lemmingMaxVelocity)
			_ent.getSpeed().setX(LemmingUtils.s_lemmingMaxVelocity);
		if(_ent.getSpeed().getX() < -LemmingUtils.s_lemmingMaxVelocity)
			_ent.getSpeed().setX(-LemmingUtils.s_lemmingMaxVelocity);
		
		if(_ent.getSpeed().getY() > LemmingUtils.MAXIMUM_FALLING_SPEED)
			_ent.getSpeed().setY(LemmingUtils.MAXIMUM_FALLING_SPEED);
		if(_ent.getSpeed().getY() < -LemmingUtils.MAXIMUM_FALLING_SPEED)
			_ent.getSpeed().setY(-LemmingUtils.MAXIMUM_FALLING_SPEED);
	}
	
	public void applyGravity(DynamicEntity _ent, long _dt)
	{
		// _ent.getSpeed().add(0, _dt * s_GRAVITY);
		// s_LOGGER.debug("Speed: {}\t{}", ent.getSpeed().getX(), ent.getSpeed().getY());
	}
	
	public void solveCollisions()
	{
		CollidingObjectsSet objects = m_quadTree.getCollidingObjects(null);
		for(CollidingObjects obj : objects)
		{
			if(obj.m_s1.getPhysicType() == PhysicType.STATIC && obj.m_s2.getPhysicType() == PhysicType.STATIC)
				continue; // if both objects are static we skip
			
			if(obj.m_s1.getPhysicType() == PhysicType.STATIC && obj.m_s2.getPhysicType() == PhysicType.DYNAMIC)		
			{
				// if one is static and the other is dynamic
				handleCollision((WorldEntity)obj.m_s1.getData(), (WorldEntity)obj.m_s2.getData());
			} 
			else if(obj.m_s1.getPhysicType() == PhysicType.DYNAMIC && obj.m_s2.getPhysicType() == PhysicType.STATIC)
			{
				// if one is static and the other is dynamic
				handleCollision((WorldEntity)obj.m_s2.getData(), (WorldEntity)obj.m_s1.getData());
			}
			
			if(obj.m_s1.getPhysicType() == PhysicType.DYNAMIC && obj.m_s2.getPhysicType() == PhysicType.DYNAMIC)
				continue; // if both objects are dynamic we skip for now
		}
	}
	
	public void handleCollision(WorldEntity _static, WorldEntity _dynamic)
	{
		if(_static.getType() == WorldEntityEnum.MAP)
			handleMapCollision((Map)_static, (DynamicEntity)_dynamic);
		else if(_static.getType() == WorldEntityEnum.LEVEL_END)
			handleEndLevelCollision(_dynamic);
	}
	
	public void handleMapCollision(Map _map, DynamicEntity _ent)
	{
		LinkedList<Cell> cells = _map.getCellInArea(_ent.getCoordinates(), _ent.getCollisionMask());
		for(Cell cell : cells)
		{
			if(!cell.getCellType().isCrossable() && !cell.getCellType().isDangerous())
			{
				// lets see how we replace the entity
				RectangleShape d = (RectangleShape)_ent.getCollisionMask().getChilds().getFirst();
				RectangleShape s = cell.getRectangle();
				
				Point2f P1 = d.getUpperLeft();
				Point2f P2 = d.getUpperRight();
				Point2f P3 = d.getBottomLeft();
				Point2f P4 = d.getBottomRight();
				
				float x1 = s.getCoordinates(true).getX();
				float y1 = s.getCoordinates(true).getY();
				float x2 = s.getCoordinates(true).getX() + s.getWidth();
				float y2 = s.getCoordinates(true).getY() + s.getHeight();
				
				if((P2.getX() < x2 && P2.getY() < y1 && P2.getX() > x1)
						|| (P1.getX() < x2 && P1.getY() < y1 && P1.getX() > x1)) // replace north
				{
					_ent.getCoordinates().setY(y1 - d.getHeight());
				}
				else if((P1.getX() < x1 && P1.getY() > y1 && P1.getY() < y2)
						|| (P3.getX() < x1 && P3.getY() > y1 && P3.getY() < y2)) // replace west
				{
					_ent.getCoordinates().setX(x1 - d.getWidth());
				}
				else if((P3.getX() < x2 && P3.getY() > y2 && P3.getX() > x1)
						|| (P4.getX() < x2 && P4.getY() > y2 && P4.getX() > x1)) // replace south
				{
					_ent.getCoordinates().setY(y2);
				}
				else if((P2.getX() > x2 && P2.getY() > y1 && P2.getY() < y2)
						|| (P4.getX() > x2 && P4.getY() > y1 && P4.getY() < y2)) // replace east
				{
					_ent.getCoordinates().setX(x2);
				}
				
				_ent.updateExterns();
			}
		}
	}
	
	public void handleEndLevelCollision(WorldEntity _ent)
	{
		
	}

	public void computeMovements(long _dt) {
		// TODO Auto-generated method stub
		for(CollisionShape shape : m_dynamicObjects)
		{
			DynamicEntity ent = (DynamicEntity) shape.getData();
			ent.savePosition();
			ent.getCoordinates().add(ent.getSpeed().getX() * _dt, ent.getSpeed().getY() * _dt);
			ent.updateExterns();
			// s_LOGGER.debug("Speed: {}\t{}", ent.getSpeed().getX(), ent.getSpeed().getY());
		}
		
	}
}

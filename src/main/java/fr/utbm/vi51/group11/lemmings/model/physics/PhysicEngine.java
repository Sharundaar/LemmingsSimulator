package fr.utbm.vi51.group11.lemmings.model.physics;

import java.util.LinkedList;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.DynamicEntity;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.Body;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.LemmingBody;
import fr.utbm.vi51.group11.lemmings.model.physics.collidingobjects.CollidingObjects;
import fr.utbm.vi51.group11.lemmings.model.physics.collidingobjects.CollidingObjectsSet;
import fr.utbm.vi51.group11.lemmings.model.physics.properties.CollisionProperty;
import fr.utbm.vi51.group11.lemmings.model.physics.quadtree.QuadTree;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionMask;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape.PhysicType;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.RectangleShape;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class PhysicEngine
{
	/** Logger of the class */
	private final static Logger							s_LOGGER			= LoggerFactory
																					.getLogger(PhysicEngine.class);

	/** Value of the force of Gravity */
	private final static float							s_gravity			= 2920.96f / (1000.0f * 1000.0f);

	/** QuadTree splitting the screen */
	private final QuadTree								m_quadTree;
	private final LinkedList<CollisionShape>			m_staticObjects		= new LinkedList<>();
	private final LinkedList<CollisionShape>			m_dynamicObjects	= new LinkedList<>();

	public final LinkedList<SolvedCollisionProperty>	m_groundedEntity	= new LinkedList<>();
	public final LinkedList<SolvedCollisionProperty>	m_wallBlockedEntity	= new LinkedList<>();
	public final LinkedList<SolvedCollisionProperty>	m_activeEntity		= new LinkedList<>();
	public final LinkedList<SolvedCollisionProperty>	m_deadZoneEntity	= new LinkedList<>();

	/* --------------------------------------------------- */

	public class SolvedCollisionProperty
	{
		public SolvedCollisionProperty(final DynamicEntity _entity,
				final CollisionProperty _property)
		{
			m_entity = _entity;
			m_property = _property;
		}

		public DynamicEntity		m_entity;
		public CollisionProperty	m_property;
	}

	/* --------------------------------------------------- */

	public PhysicEngine(final int _width, final int _height)
	{
		s_LOGGER.debug("Creation of the PhysicsEngine...");

		m_quadTree = new QuadTree(_width, _height);

		s_LOGGER.debug("PhysicsEngine created.");
	}

	/* --------------------------------------------------- */

	public boolean collide(
			final CollisionMask _m1,
			final CollisionMask _m2)
	{
		/** TODO */
		return _m1.collide(_m2);
	}

	/* --------------------------------------------------- */

	public void addShape(
			final CollisionShape _shape,
			final PhysicType _type)
	{
		if (_shape == null)
			return;

		_shape.setPhysicType(_type);
		switch (_type)
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

	/* --------------------------------------------------- */

	public void removeShape(
			final CollisionShape _shape)
	{
		m_staticObjects.remove(_shape);
		m_dynamicObjects.remove(_shape);
		m_quadTree.remove(_shape);
	}

	/* --------------------------------------------------- */

	public LinkedList<CollisionShape> getCollidingEntities()
	{
		LinkedList<CollisionShape> result = new LinkedList<CollisionShape>();

		return result;
	}

	/* --------------------------------------------------- */

	/**
	 * @return the quadTree
	 */
	public QuadTree getQuadTree()
	{
		return m_quadTree;
	}

	/* --------------------------------------------------- */

	public void update()
	{
		m_quadTree.updateElements(m_dynamicObjects);
	}

	/* --------------------------------------------------- */

	public void updateQuadTree()
	{
		m_quadTree.updateElements(m_dynamicObjects);
	}

	/* --------------------------------------------------- */

	public void applyExternForces(
			final long _dt)
	{
		for (CollisionShape shape : m_dynamicObjects)
		{
			if (shape.getProperty() != null)
			{
				DynamicEntity ent = (DynamicEntity) shape.getProperty().getEntity();
				if (ent.getMass() > 0)
					applyGravity(ent, _dt);

				clampSpeed(ent);
			}
		}
	}

	/* --------------------------------------------------- */

	public void clampSpeed(
			final DynamicEntity _ent)
	{
		if (_ent.getSpeed().getX() > UtilsLemmings.s_lemmingMaxVelocity)
			_ent.getSpeed().setX(UtilsLemmings.s_lemmingMaxVelocity);
		if (_ent.getSpeed().getX() < -UtilsLemmings.s_lemmingMaxVelocity)
			_ent.getSpeed().setX(-UtilsLemmings.s_lemmingMaxVelocity);

		if (_ent.getSpeed().getY() > UtilsLemmings.s_maximumFallingSpeed)
			_ent.getSpeed().setY(UtilsLemmings.s_maximumFallingSpeed);
		if (_ent.getSpeed().getY() < -UtilsLemmings.s_maximumFallingSpeed)
			_ent.getSpeed().setY(-UtilsLemmings.s_maximumFallingSpeed);
	}

	/* --------------------------------------------------- */

	public void applyGravity(
			final DynamicEntity _ent,
			final long _dt)
	{
		_ent.getSpeed().add(0, _dt * s_gravity);
		// s_LOGGER.debug("Speed: {}\t{}", ent.getSpeed().getX(),
		// ent.getSpeed().getY());
	}

	/* --------------------------------------------------- */

	public void solveCollisions()
	{
		m_activeEntity.clear();
		m_deadZoneEntity.clear();
		m_groundedEntity.clear();
		m_wallBlockedEntity.clear();

		CollidingObjectsSet objects = m_quadTree.getCollidingObjects(null);
		for (CollidingObjects obj : objects)
		{
			if ((obj.m_s1.getProperty() == null) || (obj.m_s2.getProperty() == null))
				continue;

			if ((obj.m_s1.getPhysicType() == PhysicType.STATIC)
					&& (obj.m_s2.getPhysicType() == PhysicType.STATIC))
				continue; // if both objects are static we skip

			if ((obj.m_s1.getPhysicType() == PhysicType.STATIC)
					&& (obj.m_s2.getPhysicType() == PhysicType.DYNAMIC))
			{
				// if one is static and the other is dynamic
				handleStaticDynamicCollision(obj.m_s1, obj.m_s2);
			} else if ((obj.m_s1.getPhysicType() == PhysicType.DYNAMIC)
					&& (obj.m_s2.getPhysicType() == PhysicType.STATIC))
			{
				// if one is static and the other is dynamic
				handleStaticDynamicCollision(obj.m_s2, obj.m_s1);
			}

			if ((obj.m_s1.getPhysicType() == PhysicType.DYNAMIC)
					&& (obj.m_s2.getPhysicType() == PhysicType.DYNAMIC))
				continue; // if both objects are dynamic we skip for now
		}
	}

	/* --------------------------------------------------- */

	public void handleStaticDynamicCollision(
			final CollisionShape _static,
			final CollisionShape _dynamic)
	{
		if (_static.getProperty().getEntity().getType() == WorldEntityEnum.MAP)
		{
			// We're against the map
			handleMapCollision(_static, _dynamic);
		} else
		{
			handleOtherCollision(_static, _dynamic);
		}

		_dynamic.getProperty().getEntity().updateExterns();
	}

	/* --------------------------------------------------- */

	public void handleOtherCollision(
			final CollisionShape _static,
			final CollisionShape _dynamic)
	{
		if (_static.getProperty().isDangerous())
		{
			if (_static.getProperty().getKillZone().collide(_dynamic))
			{
				m_deadZoneEntity.add(new SolvedCollisionProperty((Body) _dynamic.getProperty()
						.getEntity(), _static.getProperty()));
			}
		}

		if (_static.getProperty().isActivable())
		{
			if (_static.getProperty().getActivationZone().collide(_dynamic))
			{
				m_activeEntity.add(new SolvedCollisionProperty((Body) _dynamic.getProperty()
						.getEntity(), _static.getProperty()));
			}
		}
	}

	/* --------------------------------------------------- */

	public void handleMapCollision(
			final CollisionShape _static,
			final CollisionShape _dynamic)
	{
		if (!_static.getProperty().isCrossable())
		{
			// lets see how we replace the entity
			LemmingBody ent = (LemmingBody) _dynamic.getProperty().getEntity();
			RectangleShape dnew = (RectangleShape) ent.getCollisionMask().getChilds().getFirst();
			RectangleShape d = new RectangleShape(ent.getSavedPosition().getX(), ent
					.getSavedPosition().getY(), dnew.getWidth(), dnew.getHeight(), null);
			d.updateShape();
			RectangleShape s = (RectangleShape) _static;

			Point2f P1 = d.getUpperLeft();
			Point2f P2 = d.getUpperRight();
			Point2f P3 = d.getBottomLeft();
			Point2f P4 = d.getBottomRight();

			float x1 = s.getCoordinates(true).getX();
			float y1 = s.getCoordinates(true).getY();
			float x2 = s.getCoordinates(true).getX() + s.getWidth();
			float y2 = s.getCoordinates(true).getY() + s.getHeight();

			if (((P2.getX() < x2) && (P2.getY() < y1) && (P2.getX() > x1))
					|| ((P1.getX() < x2) && (P1.getY() < y1) && (P1.getX() > x1))) // replace north
			{
				float y = y1 - (d.getCoordinates(true).getY() + d.getHeight());
				ent.getCoordinates().setY(d.getCoordinates(true).getY() + y);
				m_groundedEntity.add(new SolvedCollisionProperty(ent, _static.getProperty()));
			}
			if (((P1.getX() < x1) && (P1.getY() > y1) && (P1.getY() < y2))
					|| ((P3.getX() < x1) && (P3.getY() > y1) && (P3.getY() < y2))) // replace west
			{
				ent.getCoordinates().setX(d.getCoordinates(true).getX());
				m_wallBlockedEntity.add(new SolvedCollisionProperty(ent, _static.getProperty()));
			}
			if (((P3.getX() < x2) && (P3.getY() > y2) && (P3.getX() > x1))
					|| ((P4.getX() < x2) && (P4.getY() > y2) && (P4.getX() > x1))) // replace south
			{
				ent.getCoordinates().setY(d.getCoordinates(true).getY());
			}
			if (((P2.getX() > x2) && (P2.getY() > y1) && (P2.getY() < y2))
					|| ((P4.getX() > x2) && (P4.getY() > y1) && (P4.getY() < y2))) // replace east
			{
				ent.getCoordinates().setX(d.getCoordinates(true).getX());
				m_wallBlockedEntity.add(new SolvedCollisionProperty(ent, _static.getProperty()));
			}
		}

		if (_static.getProperty().isDangerous())
		{
			if (_static.getProperty().getKillZone().collide(_dynamic))
			{
				m_deadZoneEntity.add(new SolvedCollisionProperty((Body) _dynamic.getProperty()
						.getEntity(), _static.getProperty()));
			}
		}

		if (_static.getProperty().isActivable())
		{
			if (_static.getProperty().getActivationZone().collide(_dynamic))
			{
				m_activeEntity.add(new SolvedCollisionProperty((Body) _dynamic.getProperty()
						.getEntity(), _static.getProperty()));
			}
		}
	}

	/* --------------------------------------------------- */

	public void handleEndLevelCollision(
			final WorldEntity _ent)
	{

	}

	/* --------------------------------------------------- */

	public void computeMovements(
			final long _dt)
	{
		// TODO Auto-generated method stub
		for (CollisionShape shape : m_dynamicObjects)
		{
			DynamicEntity ent = (DynamicEntity) shape.getProperty().getEntity();
			ent.savePosition();
			ent.getCoordinates().add(ent.getSpeed().getX() * _dt, ent.getSpeed().getY() * _dt);
			ent.updateExterns();
			// s_LOGGER.debug("Speed: {}\t{}", ent.getSpeed().getX(),
			// ent.getSpeed().getY());
		}
	}

	/* --------------------------------------------------- */

	public boolean isGrounded(
			final DynamicEntity _ent)
	{
		for (SolvedCollisionProperty prop : m_groundedEntity)
		{
			if (prop.m_entity == _ent)
				return true;
		}
		return false;
	}

	/* --------------------------------------------------- */

	public boolean isInDeadZone(
			final DynamicEntity _ent)
	{
		for (SolvedCollisionProperty prop : m_deadZoneEntity)
		{
			if (prop.m_entity == _ent)
				return true;
		}
		return false;
	}

	/* --------------------------------------------------- */

	public boolean isInActiveZone(
			final DynamicEntity _ent)
	{
		for (SolvedCollisionProperty prop : m_activeEntity)
		{
			if (prop.m_entity == _ent)
				return true;
		}
		return false;
	}

	/* --------------------------------------------------- */

	public boolean isBlocked(
			final DynamicEntity _ent)
	{
		for (SolvedCollisionProperty prop : m_wallBlockedEntity)
		{
			if (prop.m_entity == _ent)
				return true;
		}
		return false;
	}
}

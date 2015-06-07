package fr.utbm.vi51.group11.lemmings.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.gui.GraphicsEngine;
import fr.utbm.vi51.group11.lemmings.gui.MainFrame;
import fr.utbm.vi51.group11.lemmings.model.agent.Agent;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.Body;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.BodyState;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.BodyStateProperty;
import fr.utbm.vi51.group11.lemmings.model.map.Map;
import fr.utbm.vi51.group11.lemmings.model.physics.PhysicEngine;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape.PhysicType;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelProperties;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.WorldEntityConfiguration;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;
import fr.utbm.vi51.group11.lemmings.utils.factory.EntityFactory;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;

/**
 * 
 * Class representing the environment of the simulation. It contains a grid
 * which is a subdivision of the map in cells, its own environment time.
 * 
 * @author jnovak
 *
 */
public class Environment
{
	/** Logger of the class */
	private final static Logger							s_LOGGER					= LoggerFactory
																							.getLogger(Environment.class);

	/** Time of the environment in milliseconds */
	private final long									m_environmentTime;

	/** The Map */
	private final Map									m_map;

	/** Graphical User Interface of the simulation */
	private final MainFrame								m_gui;

	/**
	 * PhyicsEngine of the environment that handles all matters related to
	 * collisions, physics, etc ...
	 */
	private final PhysicEngine							m_physicEngine;

	/** List containing all of the world entities of the simulation */
	public ArrayList<WorldEntity>						m_worldEntities;

	/** Influence solver */
	private final InfluenceSolver						m_influenceSolver;

	private final LinkedList<IEntityDestroyedListener>	m_entityDestroyedListener	= new LinkedList<IEntityDestroyedListener>();

	/*----------------------------------------------*/

	public Environment(final LevelProperties _currentLevelProperties, final List<Agent> _agents,
			final Simulation _simulator)
	{
		s_LOGGER.debug("Creation of the environment...");

		int rowNb = _currentLevelProperties.getNbRow();
		int colNb = _currentLevelProperties.getNbCol();

		/* Instantiates attributes */
		m_environmentTime = 0;
		m_map = new Map(_currentLevelProperties);
		m_physicEngine = new PhysicEngine(rowNb, colNb);
		m_gui = new MainFrame(_simulator, this, rowNb, colNb);

		m_worldEntities = new ArrayList<WorldEntity>();

		WorldEntity worldEntity = null;
		/* Creates the world entities from the configuration file. */
		for (String key : _currentLevelProperties.getWorldEntitiesConfiguration().keySet())
			for (WorldEntityConfiguration c : _currentLevelProperties
					.getWorldEntitiesConfiguration().get(key))
			{
				if (WorldEntityEnum.LEMMING_BODY.equals(key))
				{
					worldEntity = EntityFactory.getInstance().createLemmingBody(c, this);
					// _agents.add(new LemmingAgent((LemmingBody) worldEntity));
				} else if (WorldEntityEnum.LEVEL_START.equals(key))
				{
					worldEntity = EntityFactory.getInstance().createLevelStart(c);
				} else if (WorldEntityEnum.LEVEL_END.equals(key))
				{
					worldEntity = EntityFactory.getInstance().createLevelEnd(c);
				}

				addWorldEntity(worldEntity);
			}

		m_map.getCollisionMask().updateShape();
		for (CollisionShape _shape : m_map.getCollisionMask().getChilds())
		{
			m_physicEngine.addShape(_shape, PhysicType.STATIC);
		}

		m_influenceSolver = new InfluenceSolver();

		s_LOGGER.debug("Environment created.");
	}

	/*----------------------------------------------*/
	public void sendEntityDetroyedEvent(
			final WorldEntity _ent)
	{
		for (IEntityDestroyedListener listener : m_entityDestroyedListener)
			listener.onEntityDestroyed(_ent);
	}

	/*----------------------------------------------*/
	public void addEntityDestroyedListener(
			final IEntityDestroyedListener _listener)
	{
		if (!m_entityDestroyedListener.contains(_listener))
			m_entityDestroyedListener.add(_listener);
	}

	/*----------------------------------------------*/

	/**
	 * Method used to add a WorldEntity to the environment.
	 * 
	 * @param _worldEntity
	 *            WorldEntity to add.
	 */
	public void addWorldEntity(
			final WorldEntity _worldEntity)
	{
		_worldEntity.updateExterns();

		m_worldEntities.add(_worldEntity);
		if (_worldEntity.getType() == WorldEntityEnum.LEMMING_BODY)
			m_physicEngine.addShape(_worldEntity.getCollisionMask(), PhysicType.DYNAMIC);
		else
			m_physicEngine.addShape(_worldEntity.getCollisionMask(), PhysicType.STATIC);
	}

	/*----------------------------------------------*/

	/**
	 * Method used to add a WorldEntity to the environment.
	 * 
	 * @param _worldEntity
	 *            WorldEntity to add.
	 * @param _x
	 *            x coordinate of the WorldEntity.
	 * @param _y
	 *            y coordinate of the WorldEntity;
	 */
	public void addWorldEntity(
			final WorldEntity _worldEntity,
			final int _x,
			final int _y)
	{
		_worldEntity.setWorldCoords(_x, _y);
		_worldEntity.updateExterns();

		addWorldEntity(_worldEntity);
	}

	/*----------------------------------------------*/
	public void destroyEntity(
			final WorldEntity _worldEntity)
	{
		_worldEntity.kill();
		m_worldEntities.remove(_worldEntity);
		m_physicEngine.removeShape(_worldEntity.getCollisionMask());
		sendEntityDetroyedEvent(_worldEntity);
	}

	/*----------------------------------------------*/

	/**
	 * Method used to return the list of perceived object by the body passed as
	 * parameter. Use the body's frustrum's iterator to get instances of
	 * possible perceived objects.
	 * 
	 * @param _body
	 *            Body to use to get perceived objects from.
	 * @return List of perceived objects by the body passed as parameter.
	 */
	public List<IPerceivable> getPerceptions(
			final Body _body)
	{
		return null; // TODO
	}

	/*----------------------------------------------*/
	public void update(
			final long _dt)
	{
		// TODO
		LinkedList<Body> bodies = new LinkedList<Body>();
		for (WorldEntity ent : m_worldEntities)
		{
			ent.updateExterns();
		}

		m_physicEngine.applyExternForces(_dt);
		m_physicEngine.computeMovements(_dt);
		m_physicEngine.updateQuadTree();
		m_physicEngine.solveCollisions();

		updateBodyStates(bodies);

		updateAnimations(_dt);
	}

	/*----------------------------------------------*/
	public void updateSpeed(
			final LinkedList<Body> _bodies)
	{
		for (Body body : _bodies)
		{
			switch (body.getState())
			{
				case CLIMBING:
					break;
				case DEAD:
					body.getSpeed().setX(0);
					if (body.getSpeed().getY() < 0)
						body.getSpeed().setY(0);
					break;
				case FALLING:
					body.getSpeed().setX(0);
					break;
				case NORMAL:
					if (body.getSpeed().getY() < 0)
						body.getSpeed().setY(0);
					break;
				default:
					break;

			}
		}
	}

	/*----------------------------------------------*/
	public void updateBodyStates(
			final LinkedList<Body> _bodies)
	{
		for (Body body : _bodies)
		{
			if (m_physicEngine.isInDeadZone(body))
			{
				BodyStateProperty deadState = new BodyStateProperty();
				deadState.m_timeOfDeath = getEnvironmentTime();
				body.setState(BodyState.DEAD, deadState);
			}

			switch (body.getState())
			{
				case CLIMBING:
					if (m_physicEngine.isGrounded(body))
					{
						if (!m_physicEngine.isBlocked(body))
						{
							body.setState(BodyState.NORMAL, new BodyStateProperty());
							body.setMass(LemmingUtils.LEMMING_MASS);
						}
					} else
					{
						if (!m_physicEngine.isBlocked(body))
						{
							BodyStateProperty fallingState = new BodyStateProperty();
							fallingState.m_chuteOpen = false;
							fallingState.m_fallHeight = body.getCoordinates().getY();
							body.setState(BodyState.FALLING, fallingState);
							body.setMass(LemmingUtils.LEMMING_MASS);
						}
					}
					break;

				case FALLING:
					if (m_physicEngine.isGrounded(body))
					{
						if (body.getStateProperty().m_chuteOpen)
						{
							body.setState(BodyState.NORMAL, new BodyStateProperty());
						} else if ((body.getStateProperty().m_fallHeight - body.getCoordinates()
								.getY()) <= LemmingUtils.MAXIMUM_FALLING_HEIGHTS)
						{
							body.setState(BodyState.NORMAL, new BodyStateProperty());
						} else
						{
							BodyStateProperty deadState = new BodyStateProperty();
							deadState.m_timeOfDeath = getEnvironmentTime();
							body.setState(BodyState.DEAD, deadState);
						}
					}
					break;

				case NORMAL:
					if (m_physicEngine.isGrounded(body))
					{
						if (m_physicEngine.isBlocked(body))
						{
							body.setState(BodyState.CLIMBING, new BodyStateProperty());
							body.setMass(-5);
						}
					} else
					{
						BodyStateProperty fallingState = new BodyStateProperty();
						fallingState.m_chuteOpen = false;
						fallingState.m_fallHeight = body.getCoordinates().getY();
						body.setState(BodyState.FALLING, fallingState);
					}
					break;

				// can't escape death for now
				case DEAD:
					break;
				default:
					break;

			}
		}
	}

	/*----------------------------------------------*/

	/**
	 * Ask entities to update their animation states
	 */
	public void updateAnimations(
			final long _dt)
	{
		for (WorldEntity ent : m_worldEntities)
			ent.updateAnimation(_dt);

		/*
		 * TODO
		 * if (ent instanceof Body)
		 * {
		 * Body body = (Body) ent;
		 * if (body.getCurrentAnimationState() ==
		 * body.getPreviousAnimationState())
		 * if (!body.getAnimations().get(body.getCurrentAnimationState())
		 * .incrementTime(_dt))
		 * {
		 * Vector2i spritePos = body.getAnimations()
		 * .get(body.getCurrentAnimationState()).getCoords();
		 * body.getSprite().setTextureRect(spritePos.x(), spritePos.y(), 27,
		 * 26);
		 * }
		 * }
		 */
	}

	/*----------------------------------------------*/

	/**
	 * @return The environment Time since the beginning of the simulation in
	 *         milliseconds.
	 */
	public long getEnvironmentTime()
	{
		return m_environmentTime;
	}

	/*----------------------------------------------*/

	/**
	 * @return GUI of the environment.
	 */
	public GraphicsEngine getGraphicsEngine()
	{
		return m_gui.getGraphicsEngine();
	}

	/*----------------------------------------------*/

	/**
	 * @return Map of the level
	 */
	public Map getMap()
	{
		return m_map;
	}

	/*----------------------------------------------*/

	/**
	 * @return PhysicEngine
	 */
	public PhysicEngine getPhysicEngine()
	{
		return m_physicEngine;
	}
}
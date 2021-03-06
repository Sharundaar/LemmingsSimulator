package fr.utbm.vi51.group11.lemmings.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.gui.GraphicsEngine;
import fr.utbm.vi51.group11.lemmings.gui.MainFrame;
import fr.utbm.vi51.group11.lemmings.model.agent.Agent;
import fr.utbm.vi51.group11.lemmings.model.agent.qlearning.QLearningState;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.entity.immobile.LevelEnd;
import fr.utbm.vi51.group11.lemmings.model.entity.immobile.LevelStart;
import fr.utbm.vi51.group11.lemmings.model.entity.immobile.map.Map;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.Body;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.BodyState;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.BodyStateProperty;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.LemmingBody;
import fr.utbm.vi51.group11.lemmings.model.physics.PhysicEngine;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape.PhysicType;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.RectangleShape;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelProperties;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.WorldEntityConfiguration;
import fr.utbm.vi51.group11.lemmings.utils.enums.DigDirection;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;
import fr.utbm.vi51.group11.lemmings.utils.factory.EntityFactory;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IEntityCreatedListener;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IEntityDestroyedListener;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

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
	private long										m_environmentTime;

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
	private final LinkedList<IEntityCreatedListener> 	m_entityCreatedListener 	= new LinkedList<IEntityCreatedListener>();

	private final LinkedList<BodyStateChangeRequest>	m_changeBodyStateRequest	= new LinkedList<Environment.BodyStateChangeRequest>();
	
	private int m_levelSpawnedLemming = 0;
	
	private boolean m_gameOver = false;

	class BodyStateChangeRequest
	{
		public BodyStateChangeRequest(final Body _body, final BodyState _state)
		{
			m_body = _body;
			m_newState = _state;
		}

		public Body			m_body;
		public BodyState	m_newState;
	}

	/*----------------------------------------------*/
	public Environment(final LevelProperties _currentLevelProperties,
			final Simulation _simulator)
	{
		s_LOGGER.debug("Creation of the environment...");

		int rowNb = _currentLevelProperties.getNbRow();
		int colNb = _currentLevelProperties.getNbCol();

		int width = UtilsLemmings.s_tileWidth * colNb;
		int height = UtilsLemmings.s_tileHeight * rowNb;

		/* Instantiates attributes */
		m_environmentTime = 0;
		m_map = new Map(_currentLevelProperties, this);
		m_physicEngine = new PhysicEngine(width, height);
		m_gui = new MainFrame(_simulator, this, width, height);

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
					m_levelSpawnedLemming++;
				} else if (WorldEntityEnum.LEVEL_START.equals(key))
				{
					worldEntity = EntityFactory.getInstance().createLevelStart(c, this);
				} else if (WorldEntityEnum.LEVEL_END.equals(key))
				{
					worldEntity = EntityFactory.getInstance().createLevelEnd(c, this);
				}
				
				addWorldEntity(worldEntity);
			}

		m_map.getCollisionMask().updateShape();
		for (CollisionShape _shape : m_map.getCollisionMask().getChilds())
		{
			m_physicEngine.addShape(_shape, PhysicType.STATIC);
		}

		m_influenceSolver = new InfluenceSolver(this);

		s_LOGGER.debug("Environment created.");
	}

	/*----------------------------------------------*/
	public LevelEnd getFirstLevelEnd()
	{
		for (WorldEntity ent : m_worldEntities)
		{
			if (ent.getType() == WorldEntityEnum.LEVEL_END)
				return (LevelEnd) ent;
		}
		return null;
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
	public void addEntityCreatedListener(
			final IEntityCreatedListener _listener)
	{
		if (!m_entityCreatedListener.contains(_listener))
			m_entityCreatedListener.add(_listener);
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
		List<IPerceivable> result = new LinkedList<IPerceivable>();
		for(CollisionShape shape : m_physicEngine.getCollidingObjects(_body.getFrustrum().getShape()))
		{
			if(shape.getProperty() != null && shape.getProperty().getEntity() != null)
				result.add(shape.getProperty().getEntity());
		}
		return result; // TODO
	}

	/*----------------------------------------------*/
	public void update(
			final long _dt)
	{
		
		if(m_gameOver)
			return;
		
		m_environmentTime += _dt;

		LinkedList<Body> bodies = new LinkedList<Body>();
		for (WorldEntity ent : m_worldEntities)
		{
			if (ent.getType() == WorldEntityEnum.LEMMING_BODY)
				bodies.add((Body) ent);
		}
		m_influenceSolver.solveInfluence(bodies);

		m_physicEngine.applyExternForces(_dt);
		updateSpeed(bodies);
		m_physicEngine.computeMovements(_dt);
		m_physicEngine.updateQuadTree();
		m_physicEngine.solveCollisions();
		// m_physicEngine.updateSpeed(bodies);

		updateBodyStates(bodies);
		
		updateWorldEntities(_dt);

		checkEndGameCondition();
		
		updateAnimations(_dt);
	}
	
	/*----------------------------------------------*/
	public void checkEndGameCondition()
	{
		int endedLemmingCount = 0;
		int deadCount = 0;
		for(WorldEntity ent : m_worldEntities)
		{
			if(ent.getType() == WorldEntityEnum.LEVEL_END)
			{
				endedLemmingCount += ((LevelEnd)ent).getLemmingStashCount();
			}
			else if(ent.getType() == WorldEntityEnum.LEMMING_BODY)
			{
				if(((Body)ent).getState() == BodyState.DEAD)
					deadCount++;
			}
		}
		
		if(endedLemmingCount == LevelStart.s_LEMMINGS_AT_START + m_levelSpawnedLemming)
		{
			gameOver(endedLemmingCount, deadCount);
		}
	}
	
	public void gameOver(int _alive, int _dead)
	{
		m_gui.displayEndGameMessage(_alive, _dead);
		m_gameOver = true;
	}
	
	/*----------------------------------------------*/
	public void updateWorldEntities(long _dt)
	{
		LinkedList<WorldEntity> concurrentProtect = new LinkedList<WorldEntity>(m_worldEntities);
		for(WorldEntity ent : concurrentProtect)
		{
			if(ent.getType() == WorldEntityEnum.LEVEL_START)
				((LevelStart)ent).update(_dt);
			else if(ent.getType() == WorldEntityEnum.LEVEL_END)
				((LevelEnd)ent).update(_dt);
		}
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
							body.setMass(UtilsLemmings.s_lemmingMass);
						}
					} else
					{
						if (!m_physicEngine.isBlocked(body))
						{
							BodyStateProperty fallingState = new BodyStateProperty();
							fallingState.m_chuteOpen = false;
							fallingState.m_fallHeight = body.getCoordinates().getY();
							body.setState(BodyState.FALLING, fallingState);
							body.setMass(UtilsLemmings.s_lemmingMass);
						}
					}
					break;

				case FALLING:
					if (m_physicEngine.isGrounded(body))
					{
						if (body.getStateProperty().m_chuteOpen)
						{
							body.setState(BodyState.NORMAL, new BodyStateProperty());
						} else if ((Math.abs(body.getStateProperty().m_fallHeight
								- body.getCoordinates().getY())) <= UtilsLemmings.s_maximumFallingHeight)
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

				case DIGGING:
					if (!m_physicEngine.isGrounded(body))
					{
						BodyStateProperty fallingState = new BodyStateProperty();
						fallingState.m_chuteOpen = false;
						fallingState.m_fallHeight = body.getCoordinates().getY();
						body.setState(BodyState.FALLING, fallingState);
					} else
					{
						if (!canDigCell(body, body.getStateProperty().m_diggingDirection))
						{
							body.setState(BodyState.NORMAL, new BodyStateProperty());
						} else if ((getEnvironmentTime() - body.getStateProperty().m_diggingStart) >= UtilsLemmings.s_diggingSpeed)
						{
							digCell(body, body.getStateProperty().m_diggingDirection);
						}
					}
					break;

				// can't escape death for now
				case DEAD:
					break;
				default:
					break;

			}
		}

		for (BodyStateChangeRequest request : m_changeBodyStateRequest)
		{
			switch (request.m_newState)
			{
				case DIGGING:
					if ((request.m_body.getState() == BodyState.NORMAL)
							&& canDigCell(request.m_body, DigDirection.BOTTOM))
					{
						BodyStateProperty newProp = new BodyStateProperty();
						newProp.m_diggingDirection = DigDirection.BOTTOM;
						newProp.m_diggingStart = getEnvironmentTime();
						request.m_body.setState(BodyState.DIGGING, newProp);
					} else if ((request.m_body.getState() == BodyState.CLIMBING)
							&& (request.m_body.getSpeed().getY() >= 0))
					{
						if ((request.m_body.getSpeed().getX() > 0)
								&& canDigCell(request.m_body, DigDirection.RIGHT))
						{
							BodyStateProperty newProp = new BodyStateProperty();
							newProp.m_diggingDirection = DigDirection.RIGHT;
							newProp.m_diggingStart = getEnvironmentTime();
							request.m_body.setState(BodyState.DIGGING, newProp);
							request.m_body.setMass(UtilsLemmings.s_lemmingMass);
						} else if ((request.m_body.getSpeed().getX() < 0)
								&& canDigCell(request.m_body, DigDirection.LEFT))
						{
							BodyStateProperty newProp = new BodyStateProperty();
							newProp.m_diggingDirection = DigDirection.LEFT;
							newProp.m_diggingStart = getEnvironmentTime();
							request.m_body.setState(BodyState.DIGGING, newProp);
							request.m_body.setMass(UtilsLemmings.s_lemmingMass);
						}
					}
					break;

				// You can't go voluntarily to these states
				case CLIMBING:
					break;
				case DEAD:
					break;
				case FALLING:
					break;
				case NORMAL:
					break;
				default:
					break;
			}
		}
		m_changeBodyStateRequest.clear();
	}

	public Point2f computeDiggingPoint(
			final Body _body,
			final DigDirection _direction)
	{
		Point2f centerPoint = _body.getCenterCoordinates();
		switch (_direction)
		{
			case BOTTOM:
				centerPoint
						.addY((UtilsLemmings.s_lemmingEntityHeight + UtilsLemmings.s_tileHeight) / 2.0f);
				break;
			case LEFT:
				centerPoint
						.addX(-(UtilsLemmings.s_lemmingEntityWidth + UtilsLemmings.s_tileWidth) / 2.0f);
				break;
			case RIGHT:
				centerPoint
						.addX((UtilsLemmings.s_lemmingEntityWidth + UtilsLemmings.s_tileWidth) / 2.0f);
				break;
			default:
				return null;
		}
		return centerPoint;
	}

	public boolean canDigCell(
			final Body _body,
			final DigDirection _direction)
	{
		Point2f centerPoint = computeDiggingPoint(_body, _direction);
		if (centerPoint != null)
			return m_map.canDigCell(centerPoint.getX(), centerPoint.getY());
		else
			return false;
	}

	public void digCell(
			final Body _body,
			final DigDirection _direction)
	{
		Point2f digPoint = computeDiggingPoint(_body, _direction);
		m_map.digCell(digPoint.getX(), digPoint.getY());
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

	public void addRequestBodyStateChange(
			final Body _body,
			final BodyState _state)
	{
		m_changeBodyStateRequest.add(new BodyStateChangeRequest(_body, _state));
	}

	public LevelStart getLevelStart() {
		for(WorldEntity ent : m_worldEntities)
		{
			if(ent.getType() == WorldEntityEnum.LEVEL_START)
				return (LevelStart)ent;
		}
		return null;
	}

	public LevelEnd getLevelEnd() {
		for(WorldEntity ent : m_worldEntities)
		{
			if(ent.getType() == WorldEntityEnum.LEVEL_END)
				return (LevelEnd)ent;
		}
		return null;
	}

	public void createWorldEntity(WorldEntity _ent) {
		addWorldEntity(_ent);
		for(IEntityCreatedListener listener : m_entityCreatedListener)
			listener.onEntityCreated(_ent);
	}

	public WorldEntity getEntity(int x, int y) {
		RectangleShape rshape = new RectangleShape(x-1, y-1, 3, 3, null);
		rshape.updateShape();
		LinkedList<CollisionShape> shapes = m_physicEngine.getCollidingObjects(rshape);
		if(shapes.size() <= 0)
			return null;
		
		CollisionShape shape = shapes.getFirst();
		
		if(shape.getProperty() != null && shape.getProperty().getEntity() != null)
			return shape.getProperty().getEntity();
		return null;
	}
}
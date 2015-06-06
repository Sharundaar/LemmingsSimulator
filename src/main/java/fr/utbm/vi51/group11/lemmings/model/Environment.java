package fr.utbm.vi51.group11.lemmings.model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.gui.GraphicsEngine;
import fr.utbm.vi51.group11.lemmings.gui.MainFrame;
import fr.utbm.vi51.group11.lemmings.model.agent.Agent;
import fr.utbm.vi51.group11.lemmings.model.agent.LemmingAgent;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.DynamicEntity;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.Body;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.LemmingBody;
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
public class Environment implements KeyListener
{
	/** Logger of the class */
	private final static Logger		s_LOGGER	= LoggerFactory.getLogger(Environment.class);

	/** Time of the environment in milliseconds */
	private long			m_environmentTime;

	/** The Map */
	private final Map				m_map;

	/** Graphical User Interface of the simulation */
	private final MainFrame			m_gui;

	/**
	 * PhyicsEngine of the environment that handles all matters related to
	 * collisions, physics, etc ...
	 */
	private final PhysicEngine		m_physicEngine;

	/** List containing all of the world entities of the simulation */
	public ArrayList<WorldEntity>	m_worldEntities;
	
	/** Influence solver */
	private InfluenceSolver m_influenceSolver;

	/*----------------------------------------------*/

	public Environment(final LevelProperties _currentLevelProperties, final List<Agent> _agents)
	{
		s_LOGGER.debug("Creation of the environment...");

		/* Instantiates attributes */
		m_environmentTime = 0;
		m_map = new Map(_currentLevelProperties);
		m_physicEngine = new PhysicEngine();
		m_gui = new MainFrame(this);

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

	public void applyExternForces(
			final long _dt)
	{

	}

	/*----------------------------------------------*/
	private int	m_selectedEnt	= 3;

	public void update(
			final long _dt)
	{
		// TODO
		m_environmentTime += _dt;
		
		LinkedList<Body> bodies = new LinkedList<Body>();
		for(WorldEntity ent : m_worldEntities)
		{
			if(ent.getType() == WorldEntityEnum.LEMMING_BODY)
				bodies.add((Body)ent);
		}
		m_influenceSolver.solveInfluence(bodies);
		
		m_physicEngine.applyExternForces(_dt);
		m_physicEngine.computeMovements(_dt);
		m_physicEngine.updateQuadTree();
		m_physicEngine.solveCollisions();

		updateAnimations(_dt);
	}
	
	/*----------------------------------------------*/
	
	/**
	 * Ask entities to update their animation states
	 */
	public void updateAnimations(long _dt)
	{
		for(WorldEntity ent : m_worldEntities)
			ent.updateAnimation(_dt);
	}

	/*----------------------------------------------*/

	/**
	 * @return The environment Time since the beginning of the simulation in milliseconds.
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

	boolean	m_upPressed		= false;
	boolean	m_downPressed	= false;
	boolean	m_rightPressed	= false;
	boolean	m_leftPressed	= false;

	@Override
	public void keyPressed(
			final KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		if (arg0.getKeyCode() == KeyEvent.VK_UP)
			m_upPressed = true;
		if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
			m_rightPressed = true;
		if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
			m_leftPressed = true;
		if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
			m_downPressed = true;

		if (arg0.getKeyCode() == KeyEvent.VK_I)
			m_selectedEnt++;

		if (m_selectedEnt >= 4)
			m_selectedEnt = 1;
	}

	@Override
	public void keyReleased(
			final KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		if (arg0.getKeyCode() == KeyEvent.VK_UP)
			m_upPressed = false;
		if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
			m_rightPressed = false;
		if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
			m_leftPressed = false;
		if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
			m_downPressed = false;
	}

	@Override
	public void keyTyped(
			final KeyEvent arg0)
	{
		// TODO Auto-generated method stub

	}
}
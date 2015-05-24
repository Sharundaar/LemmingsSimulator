package fr.utbm.vi51.group11.lemmings.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.controller.KeyboardController;
import fr.utbm.vi51.group11.lemmings.gui.GraphicsEngine;
import fr.utbm.vi51.group11.lemmings.gui.MainFrame;
import fr.utbm.vi51.group11.lemmings.model.agent.Agent;
import fr.utbm.vi51.group11.lemmings.model.agent.LemmingAgent;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.Body;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.LemmingBody;
import fr.utbm.vi51.group11.lemmings.model.map.Map;
import fr.utbm.vi51.group11.lemmings.model.physics.PhysicEngine;
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
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(Environment.class);

	/** Time of the environment */
	private final long			m_environmentTime;

	/** The Map */
	private final Map			m_map;

	/** Graphical User Interface of the simulation */
	private final MainFrame		m_gui;

	/**
	 * PhyicsEngine of the environment that handles all matters related to
	 * collisions, physics, etc ...
	 */
	private final PhysicEngine	m_physicEngine;

	/** List containing all of the world entities of the simulation */
	public ArrayList<WorldEntity>	m_worldEntities;

	/*----------------------------------------------*/

	public Environment(final LevelProperties _currentLevelProperties, final List<Agent> _agents)
	{
		s_LOGGER.debug("Creation of the environment...");

		/* Instantiates attributes */
		m_environmentTime = System.currentTimeMillis();
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
					_agents.add(new LemmingAgent((LemmingBody) worldEntity));
				} else if (WorldEntityEnum.LEVEL_START.equals(key))
				{
					worldEntity = EntityFactory.getInstance().createLevelStart(c);
				} else if (WorldEntityEnum.LEVEL_END.equals(key))
				{
					worldEntity = EntityFactory.getInstance().createLevelEnd(c);
				}

				m_worldEntities.add(worldEntity);
				worldEntity.getCollisionMask().updateShape();
				// TODO add to quadtree
				m_physicEngine.addShape(worldEntity.getCollisionMask());
			}

		s_LOGGER.debug("Environment created.");
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
		// TODO
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

	public void update(long _dt)
	{
		// TODO
		for(WorldEntity ent : m_worldEntities)
			ent.getCollisionMask().updateShape();
		
		WorldEntity controlledEnt = m_worldEntities.get(m_worldEntities.size()-1);
		
		
	}

	/*----------------------------------------------*/

	/**
	 * @return The environment Time since the beginning of the simulation.
	 */
	public long getEnvironmentTime()
	{
		return System.currentTimeMillis() - m_environmentTime;
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
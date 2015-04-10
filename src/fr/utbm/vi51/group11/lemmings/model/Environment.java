package fr.utbm.vi51.group11.lemmings.model;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.gui.GraphicsEngine;
import fr.utbm.vi51.group11.lemmings.gui.MainFrame;
import fr.utbm.vi51.group11.lemmings.model.agent.Agent;
import fr.utbm.vi51.group11.lemmings.model.agent.LemmingAgent;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.Body;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.LemmingBody;
import fr.utbm.vi51.group11.lemmings.model.map.Map;
import fr.utbm.vi51.group11.lemmings.model.physics.PhysicsEngine;
import fr.utbm.vi51.group11.lemmings.utils.configuration.LevelProperties;
import fr.utbm.vi51.group11.lemmings.utils.configuration.WorldEntityConfiguration;
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

	/** Grid of the map */
	private final Map			m_map;

	/** Graphical User Interface of the simulation */
	private final MainFrame		m_gui;

	/**
	 * PhyicsEngine of the environment that handles all matters related to
	 * collisions, pĥysics, etc ...
	 */
	private final PhysicsEngine	m_physicsEngine;

	public List<WorldEntity>	list;

	/*----------------------------------------------*/

	public Environment(final LevelProperties _currentLevelProperties, final List<Agent> _agents)
	{
		s_LOGGER.debug("Creation of the environment...");

		/* Instantiates attributes */
		m_environmentTime = System.currentTimeMillis();
		m_map = new Map(_currentLevelProperties);
		m_physicsEngine = new PhysicsEngine();
		m_gui = new MainFrame(this);

		list = new ArrayList<WorldEntity>();

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

				list.add(worldEntity);
				// TODO add to quadtree
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
		return null;
	}

	/*----------------------------------------------*/

	/**
	 * Method used to move a Body with a certain direction. This movement is
	 * handled by the PhysicsEngine and it updates the data structure containing
	 * all entities.
	 * 
	 * @param _body
	 *            Body to move in the environment.
	 * @param _direction
	 *            Direction the body will move with.
	 */
	public void move(
			final Body _body,
			final Vector2f _direction)
	{
	}

	/*----------------------------------------------*/

	/**
	 * @return The environment Time since the beginning of the simulation.
	 */
	public long getEnvironmentTime()
	{
		return m_environmentTime - System.currentTimeMillis();
	}

	/*----------------------------------------------*/

	/**
	 * @return GUI of the environment.
	 */
	public GraphicsEngine getGraphicsEngine()
	{
		return m_gui.getGraphicsEngine();
	}
}
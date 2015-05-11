package fr.utbm.vi51.group11.lemmings.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.agent.Agent;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelProperties;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelPropertiesMap;

/**
 * Class designed to be the simulation of the project. Contains the list of
 * agents, the environment, and an inputController. The agents are separated
 * from the environment, they represent the "mind" of their associated
 * bodies.</br>
 * Creating the simulation instantiates all objects needed such as
 * WorldEntities, textures, etc...
 * 
 * @author jnovak
 *
 */
public class Simulation
{
	/** Logger of the class */
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(Simulation.class);

	/** List of agents contained in the environment */
	private final List<Agent>	m_agents;

	/** Reference to the environment of the simulation */
	private final Environment	m_environment;

	/*----------------------------------------------*/

	/**
	 * Default Constructor. Creates the list of agents, the environment and
	 * instantiates its inputController.
	 */
	public Simulation(final String _environmentID)
	{
		s_LOGGER.debug("Creation of the Simulation...");

		/* Creation of the attributes */
		LevelProperties currentLevelProperties = LevelPropertiesMap.getInstance().get(
				_environmentID);
		m_agents = new ArrayList<Agent>();
		m_environment = new Environment(currentLevelProperties, m_agents);

		s_LOGGER.debug("Simulation created.");
	}

	/*----------------------------------------------*/

	/**
	 * @return List of agents of the simulation.
	 */
	public List<Agent> getAgents()
	{
		return m_agents;
	}

	/*----------------------------------------------*/

	/**
	 * @return Environment of the simulation.
	 */
	public Environment getEnvironment()
	{
		return m_environment;
	}

	/*----------------------------------------------*/

	/**
	 * Method used to destroy the simulation and all of its content.
	 */
	public void destroy()
	{
		m_agents.clear();
	}
}
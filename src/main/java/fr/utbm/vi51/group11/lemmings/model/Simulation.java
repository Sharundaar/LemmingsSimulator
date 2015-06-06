package fr.utbm.vi51.group11.lemmings.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.agent.Agent;
import fr.utbm.vi51.group11.lemmings.model.agent.KeyboardAgent;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelProperties;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelPropertiesMap;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IControllable;

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
	public final static Logger	s_LOGGER	= LoggerFactory.getLogger(Simulation.class);

	/** List of agents contained in the environment */
	private final List<Agent>	m_agents;

	/** Reference to the environment of the simulation */
	private final Environment	m_environment;
	
	private boolean m_running = false;

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
		loop();
	}

	/*----------------------------------------------*/
	public void initialize()
	{
		KeyboardAgent ka = new KeyboardAgent();
		for(WorldEntity ent : m_environment.m_worldEntities)
		{
			if(ent.getType() == WorldEntityEnum.LEMMING_BODY)
			{
				ka.setBody((IControllable)ent);
				break;
			}
		}
		
		m_environment.getGraphicsEngine().addKeyListener(ka);
		m_agents.add(ka);
	}
	
	/*----------------------------------------------*/
	public void loop()
	{
		s_LOGGER.debug("Loop start.");
		
		initialize();
		
		m_running = true;
		
		long start = 0;
		long end = System.currentTimeMillis();
		long dt = 0;
		long fps_timer = 0;
		short fps_count = 0;
		while(m_running)
		{
			start = System.currentTimeMillis();
			updateAgents(dt);
			update(dt);
			draw();
			
			try {
				if(System.currentTimeMillis() - start < 17)
					Thread.sleep(17 - (System.currentTimeMillis() - start));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			end = System.currentTimeMillis();
			
			dt = end - start;
			
			fps_timer += dt;
			fps_count++;
			if(fps_timer >= 1000)
			{
				s_LOGGER.debug("FPS: {}.", fps_count);
				fps_timer = 0;
				fps_count = 0;
			}
		}
	}
	
	/*----------------------------------------------*/
	public void updateAgents(long _dt)
	{
		for(Agent ag : m_agents)
		{
			ag.live(_dt);
		}
	}
	
	/*----------------------------------------------*/
	public void update(long _dt)
	{
		m_environment.update(_dt);
	}
	
	/*----------------------------------------------*/
	public void draw()
	{
		m_environment.getGraphicsEngine().repaint();
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
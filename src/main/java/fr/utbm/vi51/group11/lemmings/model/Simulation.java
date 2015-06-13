package fr.utbm.vi51.group11.lemmings.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.agent.Agent;
import fr.utbm.vi51.group11.lemmings.model.agent.KeyboardAgent;
import fr.utbm.vi51.group11.lemmings.model.agent.LemmingAgent;
import fr.utbm.vi51.group11.lemmings.model.agent.qlearning.QLearning;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.LemmingBody;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelProperties;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IControllable;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IEntityCreatedListener;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IEntityDestroyedListener;

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
public class Simulation implements IEntityDestroyedListener, IEntityCreatedListener
{
	/** Logger of the class */
	public final static Logger	s_LOGGER				= LoggerFactory.getLogger(Simulation.class);

	public final static long	MAXIMUM_DELTA_TIME		= 60;

	/** List of agents contained in the environment */
	private final List<Agent>	m_agents;

	/** Reference to the environment of the simulation */
	private Environment			m_environment;

	private boolean				m_running				= false;

	private float				m_speedMultiplicator	= 1.0f;

	private HumanActor			m_humanActor;

	private QLearning			m_qlearning;

	private boolean				m_pause					= false;

	private KeyboardAgent		m_keyboardAgent;

	/*----------------------------------------------*/

	/**
	 * Default Constructor. Creates the list of agents, the environment and
	 * instantiates its inputController.
	 */
	public Simulation(final LevelProperties _levelProperties)
	{
		s_LOGGER.debug("Creation of the Simulation...");

		m_agents = new ArrayList<Agent>();
		m_environment = new Environment(_levelProperties, this);
		m_environment.addEntityCreatedListener(this);
		m_environment.addEntityDestroyedListener(this);

		m_qlearning = new QLearning(m_environment, 0.70, 0.50, 0.0);

		s_LOGGER.debug("Simulation created.");
	}

	/*----------------------------------------------*/
	public void setSpeedMultiplicator(
			final float _mul)
	{
		if (_mul >= 0)
			m_speedMultiplicator = _mul;
	}

	/*----------------------------------------------*/
	public void initialize()
	{
		for (WorldEntity ent : m_environment.m_worldEntities)
		{
			if (ent.getType() == WorldEntityEnum.LEMMING_BODY)
			{
				LemmingAgent agent = new LemmingAgent((LemmingBody) ent, m_qlearning);
				agent.enable(true);
				m_agents.add(agent);
			}
		}

		m_keyboardAgent = new KeyboardAgent();
		m_keyboardAgent.enable(true);
		m_environment.getGraphicsEngine().addKeyListener(m_keyboardAgent);
		m_agents.add(m_keyboardAgent);

		m_humanActor = new HumanActor(this);
		m_environment.getGraphicsEngine().addMouseListener(m_humanActor);
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
		while (m_running)
		{
			start = System.currentTimeMillis();
			if (!m_pause)
				update((long) (m_speedMultiplicator * dt));
			draw();

			try
			{
				if ((System.currentTimeMillis() - start) < 17)
					Thread.sleep(17 - (System.currentTimeMillis() - start));
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			end = System.currentTimeMillis();

			dt = end - start;

			fps_timer += dt;
			fps_count++;
			if (fps_timer >= 1000)
			{
				s_LOGGER.debug("FPS: {}.", fps_count);
				fps_timer = 0;
				fps_count = 0;
			}
		}
	}

	/*----------------------------------------------*/
	public void updateAgents(
			final long _dt)
	{
		for (WorldEntity ent : m_environment.m_worldEntities)
		{
			if (ent.getType() == WorldEntityEnum.LEMMING_BODY)
			{
				LemmingBody body = (LemmingBody) ent;
				body.getInfluences().clear();
			}
		}

		for (Agent ag : m_agents)
		{
			if (ag.isAlive())
				ag.live(_dt);
		}
	}

	/*----------------------------------------------*/
	public void update(
			long _dt)
	{
		m_humanActor.update();
		if (_dt > MAXIMUM_DELTA_TIME)
		{
			s_LOGGER.debug("Maximum delta reach (dt: {}) safety clamping delta.", _dt);
			_dt = MAXIMUM_DELTA_TIME;
		}
		updateAgents(_dt);
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
		m_environment.destroy();
	}

	/*----------------------------------------------*/

	public void stop()
	{
		m_running = false;
	}

	/*----------------------------------------------*/

	@Override
	public void onEntityDestroyed(
			final WorldEntity _ent)
	{
		Agent ag = null;
		for (Agent a : m_agents)
		{
			if (a.getBody() == _ent)
			{
				ag = a;
				break;
			}
		}

		if (ag != null)
		{
			ag.kill();
			m_agents.remove(ag);
		}
	}

	// Remove the agent that control the body
	public void removeAgent(
			final IControllable _body)
	{
		Agent toRemove = null;
		for (Agent ag : m_agents)
		{
			if (ag.getBody() == _body)
			{
				toRemove = ag;
			}
		}
		toRemove.setBody(null);
		toRemove.enable(false);
		m_agents.remove(toRemove);
	}

	@Override
	public void onEntityCreated(
			final WorldEntity _ent)
	{
		if (_ent.getType() == WorldEntityEnum.LEMMING_BODY)
		{
			LemmingAgent la = new LemmingAgent((LemmingBody) _ent, m_qlearning);
			la.enable(true);
			m_agents.add(la);
		}
	}

	public void setPause(
			final boolean b)
	{
		m_pause = b;
	}

	public void togglePause()
	{
		m_pause = !m_pause;
	}

	public boolean isPaused()
	{
		// TODO Auto-generated method stub
		return m_pause;
	}

	public KeyboardAgent getKeyboardAgent()
	{
		// TODO Auto-generated method stub
		return m_keyboardAgent;
	}

	public void addAgent(
			final Agent _agent)
	{
		// TODO Auto-generated method stub
		m_agents.add(_agent);
		_agent.enable(true);
	}

	public QLearning getLearningAPI()
	{
		// TODO Auto-generated method stub
		return m_qlearning;
	}

	public void restart(
			final LevelProperties _levelProperties)
	{
		m_environment = null;
		m_environment = new Environment(_levelProperties, this);
		m_qlearning = null;
		m_qlearning = new QLearning(m_environment, 0.70, 0.50, 0.0);

		s_LOGGER.debug("Simulation created.");
	}
}
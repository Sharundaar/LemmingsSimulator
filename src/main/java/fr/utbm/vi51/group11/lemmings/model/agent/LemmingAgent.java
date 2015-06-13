package fr.utbm.vi51.group11.lemmings.model.agent;

import java.util.List;

import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.agent.qlearning.QLearning;
import fr.utbm.vi51.group11.lemmings.model.agent.qlearning.QLearningState;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.LemmingBody;
import fr.utbm.vi51.group11.lemmings.utils.enums.InfluenceType;
import fr.utbm.vi51.group11.lemmings.utils.enums.ShortTermAgentOrder;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IControllable;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

/**
 * 
 * Class representing the "mind" of a Lemming.
 * 
 * @author jnovak
 *
 */
public class LemmingAgent extends Agent
{

	/** Logger of the class */
	private QLearning m_learningAPI;
	private ShortTermAgent m_shortTermAgent;
	
	private QLearningState m_currentState;
	
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(LemmingAgent.class);

	/*----------------------------------------------*/

	/**
	 * Default Constructor. Creates a LemmingAgent from a body.
	 * 
	 * @param ent
	 *            Body of the LemmingAgent.
	 */
	public LemmingAgent(final IControllable ent, QLearning _learningAPI)
	{
		s_LOGGER.debug("Creation of the Lemming Agent...");

		m_learningAPI = _learningAPI;
		m_body = ent;
		
		m_shortTermAgent = new ShortTermAgent();
		m_shortTermAgent.setBody(m_body);
		m_shortTermAgent.enable(true);

		s_LOGGER.debug("Lemming Agent created.");
	}

	/*----------------------------------------------*/

	/**
	 * Method used to tell the agent to start its cycle of life.
	 */
	@Override
	public void live(
			final long _dt)
	{
		int x = (int)(m_body.getCenterCoordinates().getX() / UtilsLemmings.s_tileWidth);
		int y = (int)(m_body.getCenterCoordinates().getY() / UtilsLemmings.s_tileHeight);
		QLearningState currentState = m_learningAPI.getState(x, y);
		
		if(m_shortTermAgent.isCurrentOrderComplete())
		{
			if(m_shortTermAgent.getCurrentOrder() != ShortTermAgentOrder.IDLE)
				m_learningAPI.update(m_currentState, m_shortTermAgent.getCurrentOrder(), x, y, m_body.getState());
			m_currentState = currentState;
			m_shortTermAgent.setOrder(currentState.getBestOrder());
		}
		
		m_shortTermAgent.checkForUmbrella();
		m_shortTermAgent.live(_dt);
	}

	/*----------------------------------------------*/

	/**
	 * Method used to decide what to do/decide according to the different
	 * perceived entities from its body's frustrum.
	 * 
	 * @param _surroundingEntities
	 *            List of perceived WorldEntities by the agent's body's
	 *            frustrum.
	 */
	@Override
	protected Influence decide(
			final List<IPerceivable> _surroundingEntities)
	{
		return new Influence(InfluenceType.SPEED, new Vector2f(-1, 0));
		// return null;// TODO TODO
	}
}

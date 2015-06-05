package fr.utbm.vi51.group11.lemmings.model.agent;

import java.util.List;

import fr.utbm.vi51.group11.lemmings.utils.interfaces.IControllable;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;

/**
 * Class designed to be the mind of a world entity. Possesses a physical body
 * (WorldEntity).
 * 
 * @author jnovak
 */
public abstract class Agent
{

	/** Physical body of the agent */
	protected IControllable	m_body;

	/*----------------------------------------------*/

	/**
	 * Method used to tell the agent to start its cycle of life.
	 * 
	 * @param _dt
	 *            time span since last call to live, in milliseconds.
	 */
	public abstract void live(
			long _dt);

	/*----------------------------------------------*/

	/**
	 * Method used to ask the environment (through the body) all the perceptions
	 * its body can perceive.
	 * 
	 * @return List of perceivable objects from the environment, from the
	 *         agent's body's frustrum.
	 */
	protected abstract List<IPerceivable> getPerceptions();

	/*----------------------------------------------*/

	/**
	 * Method used to decide what to do/decide according to the different
	 * perceived entities from its body's frustrum.
	 * 
	 * @param _surroundingEntities
	 *            List of perceived WorldEntities by the agent's body's
	 *            frustrum.
	 */
	protected abstract Influence decide(
			List<IPerceivable> _surroundingEntities);

	/*----------------------------------------------*/

	/**
	 * Method used to make the agent take an .
	 * 
	 * @param _action
	 *            Direction to take for the next move.
	 */
	protected abstract void influenceBody(
			Influence _influence);

	/*----------------------------------------------*/

	/**
	 * @return Return the Body of the agent.
	 */
	public IControllable getBody()
	{
		return m_body;
	}
}

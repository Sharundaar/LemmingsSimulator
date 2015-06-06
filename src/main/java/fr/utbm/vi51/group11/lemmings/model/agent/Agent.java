package fr.utbm.vi51.group11.lemmings.model.agent;

import java.util.List;

import org.arakhne.afc.math.continous.object2d.Vector2f;

import fr.utbm.vi51.group11.lemmings.utils.interfaces.IControllable;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;

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
	 * @param _dt
	 * 				time span since last call to live, in milliseconds.
	 */
	public abstract void live(long _dt);
	
	/*----------------------------------------------*/

	/**
	 * Method used to decide what to do/decide according to the different
	 * perceived entities from its body's frustrum.
	 * 
	 * @param _surroundingEntities
	 *            List of perceived WorldEntities by the agent's body's
	 *            frustrum.
	 */
	protected abstract Vector2f decide(
			List<IPerceivable> _surroundingEntities);

	/*----------------------------------------------*/

	/**
	 * Method used to move the agent's body with the direction passed as
	 * parameter.
	 * 
	 * @param _direction
	 *            Direction to take for the next move.
	 */
	protected abstract void move(
			Vector2f _direction);

	/*----------------------------------------------*/

	/**
	 * @return Return the Body of the agent.
	 */
	public IControllable getBody()
	{
		return m_body;
	}
	
	/*----------------------------------------------*/

	/**
	 * Set agent body
	 */
	public void setBody(IControllable _body)
	{
		m_body = _body;
	}
}

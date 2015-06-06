package fr.utbm.vi51.group11.lemmings.model.agent;

import java.util.List;

import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.LemmingBody;
import fr.utbm.vi51.group11.lemmings.utils.enums.InfluenceType;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;

/**
 * 
 * Class representing the "mind" of a Lemming.
 * 
 * @author jnovak
 *
 */
public class LemmingAgent extends Agent {

	/** Logger of the class */
	private final static Logger s_LOGGER = LoggerFactory
			.getLogger(LemmingAgent.class);

	/*----------------------------------------------*/

	/**
	 * Default Constructor. Creates a LemmingAgent from a body.
	 * 
	 * @param _lemmingBody
	 *            Body of the LemmingAgent.
	 */
	public LemmingAgent(final LemmingBody _lemmingBody) {
		s_LOGGER.debug("Creation of the Lemming Agent...");

		m_body = _lemmingBody;

		s_LOGGER.debug("Lemming Agent created.");
	}

	/*----------------------------------------------*/

	/**
	 * Method used to tell the agent to start its cycle of life.
	 */
	@Override
	public void live(long _dt) {
		List<IPerceivable> surroundingEntities = m_body.getPerception();

		Vector2f newDirection = decide(surroundingEntities);

		move(newDirection);

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
	protected Vector2f decide(final List<IPerceivable> _surroundingEntities) {
		return null;// TODO
	}

	/*----------------------------------------------*/

	/**
	 * Method used to move the agent's body with the direction passed as
	 * parameter.
	 * 
	 * @param _direction
	 *            Direction to take for the next move.
	 */
	@Override
	protected void move(final Vector2f _direction) {
		if(m_body != null)
			m_body.addInfluence(new Influence(InfluenceType.SPEED, _direction));
	}

}

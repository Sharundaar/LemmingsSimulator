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
public class LemmingAgent extends Agent
{

	/** Logger of the class */
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(LemmingAgent.class);

	/*----------------------------------------------*/

	/**
	 * Default Constructor. Creates a LemmingAgent from a body.
	 * 
	 * @param _lemmingBody
	 *            Body of the LemmingAgent.
	 */
	public LemmingAgent(final LemmingBody _lemmingBody)
	{
		s_LOGGER.debug("Creation of the Lemming Agent...");

		m_body = _lemmingBody;

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
		List<IPerceivable> surroundingEntities = m_body.getPerception();

		Influence influence = decide(surroundingEntities);

		influenceBody(influence);

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

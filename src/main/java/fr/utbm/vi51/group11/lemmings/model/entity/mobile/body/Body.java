package fr.utbm.vi51.group11.lemmings.model.entity.mobile.body;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.gui.texture.Animation;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.DynamicEntity;
import fr.utbm.vi51.group11.lemmings.utils.enums.AnimationState;
import fr.utbm.vi51.group11.lemmings.utils.enums.InfluenceType;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IControllable;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.misc.Action;
import fr.utbm.vi51.group11.lemmings.utils.misc.Frustrum;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;

/**
 * 
 * Class representing the body of an Entity, a physical object that obeys the
 * laws of the graphicsEngine. Associated with a "mind" (an Agent) it can ask
 * the environment to fill its percepton field (frustrum), filter its perception
 * with obstacles, ask the environment to filter its actions, and finally move
 * to a new destination. It also can be killed and revived.
 * 
 * @author jnovak
 *
 */
public abstract class Body extends DynamicEntity implements IControllable
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger					s_LOGGER	= LoggerFactory.getLogger(Body.class);

	/** Tells of the body is alive or not. */
	protected boolean							m_alive;

	/** Field of perception of the body */
	protected Frustrum							m_frustrum;

	/** Influences given by the agent to perform */
	protected LinkedList<Influence>				m_influences;

	/** State */
	protected BodyState							m_state;

	/** State property */
	protected BodyStateProperty					m_stateProperty;

	protected Map<AnimationState, Animation>	m_animations;

	protected AnimationState					m_currentAnimationState;
	protected AnimationState					m_previousAnimationState;

	/*----------------------------------------------*/

	/**
	 * @return True if the body is still alive.</br>False otherwise.
	 */
	public boolean isAlive()
	{
		return m_alive;
	}

	/*----------------------------------------------*/

	/**
	 * @return Frustrum of the body.
	 */
	public Frustrum getFrustrum()
	{
		return m_frustrum;
	}

	/*----------------------------------------------*/

	/**
	 * Method used to ask the environment a list of Perceivable objects the body
	 * can see through its body's frustrum.
	 * 
	 * @return List of Perceivable objects computed by the environment according
	 *         to the body's frustrum.
	 */
	@Override
	public List<IPerceivable> getPerception()
	{
		List<IPerceivable> perception = m_environment.getPerceptions(this);
		filterPerception(perception);
		return perception;
	}

	public Map<AnimationState, Animation> getAnimations()
	{
		return m_animations;
	}

	/*----------------------------------------------*/
	public LinkedList<Influence> getInfluences()
	{
		return m_influences;
	}

	public AnimationState getCurrentAnimationState()
	{
		return m_currentAnimationState;
	}

	public void setCurrentAnimationState(
			final AnimationState _animationState)
	{
		m_currentAnimationState = _animationState;
	}

	public AnimationState getPreviousAnimationState()
	{
		return m_previousAnimationState;
	}

	public void setPreviousAnimationState(
			final AnimationState _animationState)
	{
		m_previousAnimationState = _animationState;
	}

	/*----------------------------------------------*/

	/**
	 * Method used to filter the perception of the body. If Perceivable objects
	 * are hidden by some obstacle, the list of perceivable objects are to be
	 * updated.
	 * 
	 * @return True if the list has been totally filtered.</br>False otherwise.
	 */
	protected abstract boolean filterPerception(
			List<IPerceivable> _perception);

	@Override
	public boolean addInfluence(
			final Influence _influence)
	{
		return m_influences.add(_influence);
	}

	@Override
	public boolean removeInfluence(
			final Influence _influence)
	{
		return m_influences.remove(_influence);
	}
	
	/*----------------------------------------------*/

	/**
	 * Method used to filter the influence added to the body by the agent. If
	 * for example the body wants to move but is paralyzed, it will filter the
	 * speed influence.
	 * 
	 * @return True if the influence is filtered.</br>False otherwise.
	 */
	protected abstract boolean filterInfluence();

	/*----------------------------------------------*/

	/**
	 * Method used to kill the body.
	 */
	@Override
	public void kill()
	{
		m_alive = false;
	}

	/*----------------------------------------------*/

	/**
	 * Method used to revive the body.
	 */
	public void revive()
	{
		m_alive = true;
	}

	@Override
	public BodyState getState()
	{
		return m_state;
	}

	public BodyStateProperty getStateProperty()
	{
		return m_stateProperty;
	}

	public void setState(
			final BodyState _state,
			final BodyStateProperty _property)
	{
		if (_property == null)
			return;

		m_state = _state;
		switch (m_state)
		{
			case DEAD:
				m_stateProperty.m_timeOfDeath = _property.m_timeOfDeath;
				break;
			case FALLING:
				m_stateProperty.m_fallHeight = _property.m_fallHeight;
				m_stateProperty.m_chuteOpen = _property.m_chuteOpen;
				break;

			case DIGGING:
				m_stateProperty.m_diggingDirection = _property.m_diggingDirection;
				m_stateProperty.m_diggingStart = _property.m_diggingStart;
				break;
				
			case CLIMBING:
				break;
			case NORMAL:
				break;
			default:
				break;

		}
	}
}
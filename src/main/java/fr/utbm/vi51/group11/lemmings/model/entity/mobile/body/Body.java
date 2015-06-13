package fr.utbm.vi51.group11.lemmings.model.entity.mobile.body;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.arakhne.afc.math.discrete.object2d.Vector2i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.gui.texture.Animation;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.DynamicEntity;
import fr.utbm.vi51.group11.lemmings.utils.enums.AnimationState;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IControllable;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.misc.Frustrum;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

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
	private final static Logger						s_LOGGER	= LoggerFactory
																		.getLogger(Body.class);

	/** Tells of the body is alive or not. */
	protected boolean								m_alive;

	/** Field of perception of the body */
	protected Frustrum								m_frustrum;

	/** Influences given by the agent to perform */
	protected LinkedList<Influence>					m_influences;

	/** State */
	protected BodyState								m_state;

	/** State property */
	protected BodyStateProperty						m_stateProperty;

	protected MultivaluedMap<BodyState, Animation>	m_animations;

	protected Animation								m_currentAnimation;

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

	public MultivaluedMap<BodyState, Animation> getAnimations()
	{
		return m_animations;
	}

	/*----------------------------------------------*/
	public LinkedList<Influence> getInfluences()
	{
		return m_influences;
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

	@Override
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

	@Override
	public void updateAnimation(
			final long _dt)
	{
		Animation bodyStateAnimation = getCurrentBodyStateAnimation();

		if (bodyStateAnimation != null)
		{
			/* Same body state as before. */
			if (isSameBodyState() && isSameAnimationState(bodyStateAnimation))
			{
				/* Same animation state as before. */
				handleSameAnimation(_dt);

				/* Different State. */
			} else
			{
				handleNewStateAnimation(bodyStateAnimation);
			}

			updateAnimationPreviousBodyState();

		} else
		{
			s_LOGGER.error("Could not find associated animation for state '{}' and speed {}.",
					m_state, m_speed);
		}
	}

	private void handleSameAnimation(
			final long _dt)
	{
		/* Time exceeds for one Animation frame => change sprite. */
		if (m_currentAnimation.exceedsAnimationTime(_dt))
		{
			/* Resets the animation to a new frame and resets time. */
			m_currentAnimation.updateAnimationFrame();

			updateSprite();

			/* Still within frame animation time limit. */
		} else
		{
			m_currentAnimation.incrementTime(_dt);
		}
	}

	private void handleNewStateAnimation(
			final Animation _animation)
	{
		/* Resets the previous animation. */
		for (Animation a : m_animations.get(m_currentAnimation.getBodyState()))
			a.resetAnimationFrame();

		m_currentAnimation = _animation;

		updateSprite();
	}

	private Animation getCurrentBodyStateAnimation()
	{
		List<Animation> animationList = m_animations.get(m_state);

		int signX = (m_speed.getX() == 0.0f) ? 0 : (m_speed.getX() > 0.0f) ? 1 : -1;
		int signY = (m_speed.getY() == 0.0f) ? 0 : (m_speed.getY() > 0.0f) ? 1 : -1;

		switch (m_state)
		{
			case CLIMBING:
				if (signX == -1)
				{
					if (signY != 0)
						return getAnimationFromState(animationList, AnimationState.CLIMBING_LEFT);
					else
						return getAnimationFromState(animationList,
								AnimationState.CLIMBING_LEFT_IDLE);
				} else if (signX == 1)
				{
					if (signY != 0)
						return getAnimationFromState(animationList, AnimationState.CLIMBING_RIGHT);
					else
						return getAnimationFromState(animationList,
								AnimationState.CLIMBING_RIGHT_IDLE);
				}
			case DIGGING:
				if (signX == -1)
					return getAnimationFromState(animationList, AnimationState.DIGGING_LEFT);
				else if (signX == 0)
					return getAnimationFromState(animationList, AnimationState.DIGGING_DOWN);
				else
					return getAnimationFromState(animationList, AnimationState.DIGGING_RIGHT);
			case FALLING:
				if (m_stateProperty.m_chuteOpen)
					return getAnimationFromState(animationList, AnimationState.FALLING_UMBRELLA);
				else
					return getAnimationFromState(animationList, AnimationState.FALLING_FREE);
			case NORMAL:
				if (signX == -1)
					return getAnimationFromState(animationList, AnimationState.WALKING_LEFT);
				else if (signX == 0)
					return getAnimationFromState(animationList, AnimationState.IDLE);
				else
					return getAnimationFromState(animationList, AnimationState.WALKING_RIGHT);
			case DEAD:
				return animationList.get(0);
		}
		return null;
	}

	public Animation getAnimationFromState(
			final List<Animation> _list,
			final AnimationState _state)
	{
		for (int i = 0; i < _list.size(); ++i)
			if (_list.get(i).equals(_state))
				return _list.get(i);
		return null;
	}

	private void updateSprite()
	{
		/*
		 * Gets the new sprite coords in function of index and frame
		 * gap.
		 */
		Vector2i spritePos = m_currentAnimation.getCoords();

		/* Sets the new Sprite. */
		m_sprite.setTextureRect(spritePos.x(), spritePos.y(), UtilsLemmings.s_lemmingSpriteWidth,
				UtilsLemmings.s_lemmingSpriteHeight);
	}

	private void updateAnimationPreviousBodyState()
	{
		for (BodyState bodyState : m_animations.keySet())
			for (Animation anim : m_animations.get(bodyState))
			{
				anim.setBodyState(m_state);
				anim.setBodyStateProperty(m_stateProperty.newInstance());
			}
	}

	private boolean isSameBodyState()
	{
		return (m_state == m_currentAnimation.getBodyState());
	}

	private boolean isSameAnimationState(
			final Animation _bodyStateAnimation)
	{
		return m_currentAnimation.equals(_bodyStateAnimation);
	}
}
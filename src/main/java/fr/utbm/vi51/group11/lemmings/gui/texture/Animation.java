package fr.utbm.vi51.group11.lemmings.gui.texture;

import org.arakhne.afc.math.discrete.object2d.Vector2i;

import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.BodyState;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.BodyStateProperty;
import fr.utbm.vi51.group11.lemmings.utils.enums.AnimationState;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class Animation
{
	private final float[]			m_timeGaps;
	private float					m_currentTime;
	private int						m_index;
	private final AnimationState	m_animationState;
	private BodyState				m_bodyState;
	private BodyStateProperty		m_bodyStateProperty;

	public Animation(final AnimationState _state)
	{
		m_animationState = _state;
		m_currentTime = 0;
		m_index = 0;

		m_timeGaps = new float[_state.getNbSpriteSet()];
		for (int i = 0; i < _state.getNbSpriteSet(); ++i)
			m_timeGaps[i] = UtilsLemmings.s_lemmingAnimationTimeGap;
	}

	public AnimationState getAnimationState()
	{
		return m_animationState;
	}

	public void setBodyState(
			final BodyState _bodyState)
	{
		m_bodyState = _bodyState;
	}

	public BodyState getBodyState()
	{
		return m_bodyState;
	}

	public void setBodyStateProperty(
			final BodyStateProperty _bodyStateProperty)
	{
		m_bodyStateProperty = _bodyStateProperty;
	}

	public BodyStateProperty getBodyStateProperty()
	{
		return m_bodyStateProperty;
	}

	public void incrementTime(
			final long _dt)
	{
		m_currentTime += _dt;
	}

	public void updateAnimationFrame()
	{
		m_index = (m_index + 1) % m_timeGaps.length;
		m_currentTime = 0;
	}

	public void resetAnimationFrame()
	{
		m_index = 0;
		m_currentTime = 0;
	}

	public Vector2i getCoords()
	{
		return new Vector2i(m_animationState.getXTex()
				+ (m_index * UtilsLemmings.s_lemmingSpriteSheetPixelGap),
				m_animationState.getYTex());
	}

	public boolean exceedsAnimationTime(
			final long _dt)
	{
		if ((m_currentTime + _dt) > m_timeGaps[m_index])
			return true;
		else
			return false;
	}

	public boolean equals(
			final AnimationState _animationState)
	{
		return m_animationState.equals(_animationState);
	}

	public boolean equals(
			final Animation _animation)
	{
		return equals(_animation.getAnimationState());
	}
}
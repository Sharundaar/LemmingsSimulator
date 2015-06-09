package fr.utbm.vi51.group11.lemmings.utils.enums;

import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.BodyState;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public enum AnimationState
{
	WALKING_LEFT(BodyState.NORMAL, 2, 2, 2), // TODO
	IDLE(BodyState.NORMAL, 2, 0, 2),
	WALKING_RIGHT(BodyState.NORMAL, 2, 4, 2),
	CLIMBING_LEFT_IDLE(BodyState.CLIMBING, 1, 0, 4),
	CLIMBING_LEFT(BodyState.CLIMBING, 2, 1, 4),
	CLIMBING_RIGHT_IDLE(BodyState.CLIMBING, 1, 3, 4), // TODO
	CLIMBING_RIGHT(BodyState.CLIMBING, 2, 4, 4),
	FALLING_FREE(BodyState.FALLING, 2, 0, 3),
	FALLING_UMBRELLA(BodyState.FALLING, 2, 2, 3),
	DIGGING_LEFT(BodyState.DIGGING, 2, 0, 1),
	DIGGING_DOWN(BodyState.DIGGING, 2, 2, 1),
	DIGGING_RIGHT(BodyState.DIGGING, 2, 4, 1),
	DEAD(BodyState.DEAD, 1, 2, 0);

	private BodyState	m_bodyState;
	private int			m_nbSpriteSet;
	private int			m_xTex;
	private int			m_yTex;

	private AnimationState(final BodyState _state, final int _nbSprites, final int _xTex,
			final int _yTex)
	{
		m_bodyState = _state;
		m_nbSpriteSet = _nbSprites;
		m_xTex = _xTex * UtilsLemmings.s_lemmingSpriteSheetPixelGap;
		m_yTex = _yTex * UtilsLemmings.s_lemmingSpriteSheetPixelGap;
	}

	public int getNbSpriteSet()
	{
		return m_nbSpriteSet;
	}

	public BodyState getBodyState()
	{
		return m_bodyState;
	}

	public int getXTex()
	{
		return m_xTex;
	}

	public int getYTex()
	{
		return m_yTex;
	}
}

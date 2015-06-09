package fr.utbm.vi51.group11.lemmings.gui.texture;

import org.arakhne.afc.math.discrete.object2d.Vector2i;

import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.BodyState;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class Animation
{
	private final int		m_nbFrames;
	private final float		m_frameGap;
	private final float[]	m_timeGaps;
	private final Vector2i	m_spriteSheetCoord;
	private float			m_currentTime;
	private int				m_index;

	public Animation(final BodyState _state, final int _offset)
	{
		m_frameGap = UtilsLemmings.s_lemmingSpriteSheetPixelGap;
		m_currentTime = 0;
		m_index = 0;

		switch (_state)
		{
			case DEAD:
				m_nbFrames = 1;
				m_spriteSheetCoord = new Vector2i(m_frameGap * 2, m_frameGap * 0);
				break;
			// case BLOCK:
			// m_nbFrames = 2;
			// m_spriteSheetCoord = new Vector2i(m_frameGap * 0, m_frameGap *
			// 0);
			// break;
			case DIGGING:
				m_nbFrames = 2;
				m_spriteSheetCoord = new Vector2i(m_frameGap * _offset * m_nbFrames, m_frameGap * 1);
				break;
			case NORMAL:
				m_nbFrames = 2;
				m_spriteSheetCoord = new Vector2i(m_frameGap * _offset * m_nbFrames, m_frameGap * 2);
				break;
			case FALLING:
				m_nbFrames = 2;
				m_spriteSheetCoord = new Vector2i(m_frameGap * 0, m_frameGap * 3);
				break;
			case CLIMBING:
				m_nbFrames = 2;
				m_spriteSheetCoord = new Vector2i(m_frameGap * _offset * m_nbFrames, m_frameGap * 4);
				break;

			default:
				m_nbFrames = 0;
				m_spriteSheetCoord = null;
				break;
		}

		m_timeGaps = new float[m_nbFrames];
		for (int i = 0; i < m_nbFrames; ++i)
			m_timeGaps[i] = UtilsLemmings.s_lemmingAnimationTimeGap;
	}

	public void incrementTime(
			final long _dt)
	{
		m_currentTime += _dt;
	}

	public void updateAnimationFrame()
	{
		m_index = (m_index + 1) % m_nbFrames;
		m_currentTime = 0;
	}

	public void resetAnimationFrame()
	{
		m_index = 0;
		m_currentTime = 0;
	}

	public Vector2i getCoords()
	{
		return new Vector2i(m_spriteSheetCoord.getX() + (m_index * m_frameGap),
				m_spriteSheetCoord.getY());
	}

	public boolean exceedsAnimationTime(
			final long _dt)
	{
		if ((m_currentTime + _dt) > m_timeGaps[m_index])
			return true;
		else
			return false;
	}
}
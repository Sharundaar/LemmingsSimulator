package fr.utbm.vi51.group11.lemmings.model.agent.qlearning;

import java.util.LinkedList;
import java.util.Random;

import org.arakhne.afc.math.discrete.object2d.Point2i;

import fr.utbm.vi51.group11.lemmings.model.Environment;
import fr.utbm.vi51.group11.lemmings.model.entity.immobile.LevelEnd;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.BodyState;
import fr.utbm.vi51.group11.lemmings.utils.enums.ShortTermAgentOrder;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class QLearning
{
	public static Random		s_RND		= new Random();

	double						m_alpha;
	double						m_gamma;
	double						m_q0;

	Point2i						m_levelEnd;

	LinkedList<QLearningState>	m_states	= new LinkedList<QLearningState>();

	public QLearning(final Environment _environment, final double _alpha, final double _gamma,
			final double _q0)
	{
		m_alpha = _alpha;
		m_gamma = _gamma;
		m_q0 = _q0;

		LevelEnd end = _environment.getLevelEnd();
		m_levelEnd = new Point2i((int) (end.getEndPoint().getX() / UtilsLemmings.s_tileWidth),
				(int) (end.getEndPoint().getY() / UtilsLemmings.s_tileHeight));
	}

	public void update(
			final QLearningState _oldState,
			final ShortTermAgentOrder _order,
			final int _newX,
			final int _newY,
			final BodyState _newState)
	{
		int oldDist = computeManhattanDistance(_oldState.getCoordinates(), m_levelEnd);
		int newDist = computeManhattanDistance(new Point2i(_newX, _newY), m_levelEnd);

		float delta = oldDist - newDist;

		double oldReward = _oldState.getReward(_order);
		double reward = 0;

		if (_newState == BodyState.DEAD)
			reward = oldReward + (m_alpha * ((-500 + (m_gamma * 1)) - oldReward));
		else if ((_newX == m_levelEnd.x()) && (_newY == m_levelEnd.y()))
			reward = oldReward + (m_alpha * ((500 + (m_gamma * 1)) - oldReward));
		else
			reward = oldReward + (m_alpha * ((delta + (m_gamma * 1)) - oldReward));

		_oldState.setReward(_order, reward);
	}

	public QLearningState getState(
			final int _x,
			final int _y)
	{
		for (QLearningState state : m_states)
		{
			if ((state.getCoordinates().x() == _x) && (state.getCoordinates().y() == _y))
				return state;
		}

		QLearningState state = new QLearningState(_x, _y);
		m_states.add(state);
		return state;
	}

	public int computeManhattanDistance(
			final Point2i _p1,
			final Point2i _p2)
	{
		return Math.abs(_p1.x() - _p2.x()) + Math.abs(_p1.y() - _p2.y());
	}

}

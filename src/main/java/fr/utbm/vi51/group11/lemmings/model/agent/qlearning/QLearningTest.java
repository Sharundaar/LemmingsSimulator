package fr.utbm.vi51.group11.lemmings.model.agent.qlearning;

import java.util.HashMap;
import java.util.List;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.encog.ml.world.Action;
import org.encog.ml.world.ActionProbability;
import org.encog.ml.world.State;
import org.encog.ml.world.World;
import org.encog.ml.world.WorldAgent;
import org.encog.ml.world.learning.q.QLearning;

import fr.utbm.vi51.group11.lemmings.model.Simulation;
import fr.utbm.vi51.group11.lemmings.model.entity.immobile.LevelEnd;
import fr.utbm.vi51.group11.lemmings.model.entity.immobile.map.Map;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.BodyState;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.RectangleShape;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class QLearningTest implements World
{
	QLearning	m_framework;
	Simulation	m_simulation;

	class QLearningAction implements Action
	{
		String	m_label;

		public QLearningAction(final String _label)
		{
			m_label = _label;
		}

		@Override
		public String getLabel()
		{
			return m_label;
		}

	}

	class QLearningState implements State
	{
		HashMap<String, Object>	m_properties	= new HashMap<String, Object>();
		double					m_reward;
		double[]				m_policyValues	= null;
		int						m_visited		= 0;

		@Override
		public void setProperty(
				final String key,
				final Object value)
		{
			// TODO Auto-generated method stub
			if (m_properties.containsKey(key))
			{
				m_properties.replace(key, value);
			} else
			{
				m_properties.put(key, value);
			}
		}

		@Override
		public Object getProperty(
				final String key)
		{
			return m_properties.getOrDefault(key, null);
		}

		@Override
		public double getReward()
		{
			return m_reward;
		}

		@Override
		public void setReward(
				final double r)
		{
			m_reward = r;
		}

		@Override
		public double[] getPolicyValue()
		{
			// TODO Auto-generated method stub
			return m_policyValues;
		}

		@Override
		public void setAllPolicyValues(
				final double d)
		{
			for (int i = 0; i < m_policyValues.length; ++i)
				m_policyValues[i] = d;
		}

		@Override
		public void setPolicyValueSize(
				final int s)
		{
			if (s > 0)
				m_policyValues = new double[s];
		}

		@Override
		public boolean wasVisited()
		{
			return m_visited > 0;
		}

		@Override
		public void setVisited(
				final int i)
		{
			m_visited = i;
		}

		@Override
		public int getVisited()
		{
			return m_visited;
		}

		@Override
		public void increaseVisited()
		{
			m_visited++;
		}

	}

	public QLearningTest()
	{
		m_framework = new QLearning(this, 1, 0);

	}

	public void buildWorldFromMap(
			final Map _map)
	{
		QLearningState newState = null;
		Point2f position = null;
		for (int i = 0; i < _map.getGridHeight(); ++i)
		{
			for (int j = 0; j < _map.getGridWidth(); ++j)
			{
				switch (_map.getCellType(j, i, true))
				{

					case BACK_WALL:
						newState = new QLearningState();
						position = new Point2f((j * UtilsLemmings.s_tileWidth)
								+ (UtilsLemmings.s_tileWidth / 2.0f), i
								* UtilsLemmings.s_tileHeight);
						newState.setProperty("normalState", BodyState.NORMAL);
						newState.setProperty("position", position);
						newState.setReward(computeDistanceToEnd(position));
						addState(newState);
						break;
					case TOXIC:
						newState = new QLearningState();
						position = new Point2f((j * UtilsLemmings.s_tileWidth)
								+ (UtilsLemmings.s_tileWidth / 2.0f), i
								* UtilsLemmings.s_tileHeight);
						newState.setProperty("normalState", BodyState.DEAD);
						newState.setProperty("position", position);
						newState.setReward(-100);
						addState(newState);
						break;

					// Not reachable
					case DIRT:
						break;
					case GRASS:
						break;
					case PIT:
						break;
					case STONE:
						break;

					// Not supported
					case ATTRACTIVE_FIELD:
						break;
					case REPULSIVE_FIELD:
						break;

					default:
						break;

				}
			}
		}
	}

	public double computeDistanceToEnd(
			final Point2f _position)
	{
		LevelEnd levelEnd = m_simulation.getEnvironment().getFirstLevelEnd();
		RectangleShape endLevelRect = (RectangleShape) levelEnd.getCollisionMask().getProperty()
				.getActivationZone();
		return _position.distance(endLevelRect.getCenter());
	}

	@Override
	public void addAction(
			final Action arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void addAgent(
			final WorldAgent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void addGoal(
			final State arg0)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void addState(
			final State arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public List<Action> getActions()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorldAgent> getAgents()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<State> getGoals()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getPolicyValue(
			final State arg0,
			final Action arg1)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ActionProbability getProbability()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<State> getStates()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isGoalState(
			final State arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAgent(
			final WorldAgent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void removeGoal(
			final State arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void runToGoal(
			final WorldAgent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setAllRewards(
			final double arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setPolicyValue(
			final State arg0,
			final Action arg1,
			final double arg2)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setProbability(
			final ActionProbability arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void tick()
	{
		// TODO Auto-generated method stub

	}
}

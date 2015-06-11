package fr.utbm.vi51.group11.lemmings.model.agent.qlearning;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.arakhne.afc.math.discrete.object2d.Point2i;

import fr.utbm.vi51.group11.lemmings.utils.enums.ShortTermAgentOrder;

public class QLearningState {
	private Point2i m_coordinates;
	
	HashMap<ShortTermAgentOrder, Double> m_rewards = new HashMap<ShortTermAgentOrder, Double>();
	
	public QLearningState(int _x, int _y)
	{
		m_coordinates = new Point2i(_x, _y);
		
		m_rewards.put(ShortTermAgentOrder.GO_LEFT, 0.0);
		m_rewards.put(ShortTermAgentOrder.GO_RIGHT, 0.0);
		// m_rewards.put(ShortTermAgentOrder.DIG_LEFT, 0.0);
		// m_rewards.put(ShortTermAgentOrder.DIG_RIGHT, 0.0);
		// m_rewards.put(ShortTermAgentOrder.DIG_DOWN, 0.0);
	}
	
	public ShortTermAgentOrder getBestOrder()
	{
		LinkedList<ShortTermAgentOrder> sameRewardOrder = getBestRewardOrders();
		int index = QLearning.s_RND.nextInt(sameRewardOrder.size());
		return sameRewardOrder.get(index);
	}
	
	public LinkedList<ShortTermAgentOrder> getBestRewardOrders()
	{
		LinkedList<ShortTermAgentOrder> result = new LinkedList<ShortTermAgentOrder>();
		
		double maxValue = Double.NEGATIVE_INFINITY;
		for(ShortTermAgentOrder order : m_rewards.keySet())
		{
			if(m_rewards.get(order) > maxValue)
				maxValue = m_rewards.get(order);
		}
		
		for(ShortTermAgentOrder order : m_rewards.keySet())
		{
			if(m_rewards.get(order) == maxValue)
				result.add(order);
		}
		
		return result;
	}
	
	public void setAllRewards(double _reward)
	{
		for(ShortTermAgentOrder order : m_rewards.keySet())
		{
			m_rewards.replace(order, _reward);
		}
	}
	
	public void setReward(ShortTermAgentOrder _order, double _reward)
	{
		m_rewards.replace(_order, _reward);
	}
	
	public double getReward(ShortTermAgentOrder _order)
	{
		return m_rewards.get(_order);
	}

	public Point2i getCoordinates() {
		return m_coordinates;
	}	
}

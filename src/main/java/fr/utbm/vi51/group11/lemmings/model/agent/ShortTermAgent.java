package fr.utbm.vi51.group11.lemmings.model.agent;

import java.util.List;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;

import fr.utbm.vi51.group11.lemmings.utils.enums.InfluenceType;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class ShortTermAgent extends Agent {

	enum ShortTermAgentOrder
	{
		IDLE,
		GO_LEFT,
		GO_RIGHT,
		DIG_DOWN,
		DIG_RIGHT,
		DIG_LEFT
	}
	class ShortTermAgentOrderState
	{
		public boolean m_completed = false;
		public boolean m_initialized = false;
	}
	
	private Point2f m_startingPoint;
	private boolean m_terminated;
	private ShortTermAgentOrder m_order;
	private ShortTermAgentOrderState m_orderState;
	
	public ShortTermAgent()
	{
		m_order = ShortTermAgentOrder.IDLE;
		m_orderState = new ShortTermAgentOrderState();
		m_orderState.m_completed = true;
		m_orderState.m_initialized = true;
	}
	
	@Override
	public void live(long _dt) {
		// TODO Auto-generated method stub
		if(m_alive && m_body != null)
		{
			if(!m_orderState.m_completed)
			{
				if(!m_orderState.m_initialized)
				{
					initialize(_dt);
				}
				else
				{
					doOrder(_dt);
				}
			}
			
			
		}
	}
	
	public void doOrder(long _dt)
	{
		switch(m_order)
		{
		case DIG_DOWN:
			break;
		case DIG_LEFT:
			break;
		case DIG_RIGHT:
			break;
		case GO_LEFT:
			goLeft(_dt);
			break;
		case GO_RIGHT:
			goRight(_dt);
			break;
		case IDLE:
			m_orderState.m_completed = true;
			break;
		default:
			break;
		
		}
	}
	
	private void goRight(long _dt)
	{
		Point2f bodyCoords = m_startingPoint;
		Point2f closestCellCoords = new Point2f(bodyCoords);
		closestCellCoords.setX((int) (closestCellCoords.getX() / UtilsLemmings.s_tileWidth + 1) * UtilsLemmings.s_tileWidth + UtilsLemmings.s_tileWidth / 2.0f);
		closestCellCoords.setY((int) (closestCellCoords.getY() / UtilsLemmings.s_tileHeight + 1) * UtilsLemmings.s_tileHeight + UtilsLemmings.s_tileHeight / 2.0f);
		if(Math.abs(m_body.getCenterCoordinates().getX() - closestCellCoords.getX()) <= CENTER_EPSILON)
		{
			m_orderState.m_completed = true;
			m_body.addInfluence(new Influence(InfluenceType.SPEED, new Vector2f()));
		}
		else
		{
			Vector2f speed = new Vector2f();
			speed.setX((closestCellCoords.getX() - bodyCoords.getX()) / _dt);
			m_body.addInfluence(new Influence(InfluenceType.SPEED, speed));
		}
	}
	
	private void goLeft(long _dt)
	{
		Point2f bodyCoords = m_startingPoint;
		Point2f closestCellCoords = new Point2f(bodyCoords);
		closestCellCoords.setX((int) (closestCellCoords.getX() / UtilsLemmings.s_tileWidth - 1) * UtilsLemmings.s_tileWidth + UtilsLemmings.s_tileWidth / 2.0f);
		closestCellCoords.setY((int) (closestCellCoords.getY() / UtilsLemmings.s_tileHeight - 1) * UtilsLemmings.s_tileHeight + UtilsLemmings.s_tileHeight / 2.0f);
		if(Math.abs(m_body.getCenterCoordinates().getX() - closestCellCoords.getX()) <= CENTER_EPSILON)
		{
			m_orderState.m_completed = true;
			m_body.addInfluence(new Influence(InfluenceType.SPEED, new Vector2f()));
		}
		else
		{
			Vector2f speed = new Vector2f();
			speed.setX((closestCellCoords.getX() - bodyCoords.getX()) / _dt);
			m_body.addInfluence(new Influence(InfluenceType.SPEED, speed));
		}
	}
	
	private static final float CENTER_EPSILON = 0.5f;
	
	public void initialize(long _dt)
	{
		Point2f bodyCoords = m_body.getCenterCoordinates();
		Point2f closestCellCoords = new Point2f(bodyCoords);
		closestCellCoords.setX((int) (closestCellCoords.getX() / UtilsLemmings.s_tileWidth) * UtilsLemmings.s_tileWidth + UtilsLemmings.s_tileWidth / 2.0f);
		closestCellCoords.setY((int) (closestCellCoords.getY() / UtilsLemmings.s_tileHeight) * UtilsLemmings.s_tileHeight + UtilsLemmings.s_tileHeight / 2.0f);
		if(Math.abs(bodyCoords.getX() - closestCellCoords.getX()) <= CENTER_EPSILON)
		{
			m_orderState.m_initialized = true;
			m_startingPoint = bodyCoords;
			m_body.addInfluence(new Influence(InfluenceType.SPEED, new Vector2f()));
		}
		else
		{
			Vector2f speed = new Vector2f();
			speed.setX((closestCellCoords.getX() - bodyCoords.getX()) / _dt);
			m_body.addInfluence(new Influence(InfluenceType.SPEED, speed));
		}
	}
	
	public void setOrder(ShortTermAgentOrder _order)
	{
		m_order = _order;
		m_orderState.m_completed = false;
		m_orderState.m_initialized = false;
	}
	
	public boolean isCurrentOrderComplete()
	{
		return m_terminated;
	}

	@Override
	protected Influence decide(List<IPerceivable> _surroundingEntities) {
		// TODO Auto-generated method stub
		return null;
	}

}

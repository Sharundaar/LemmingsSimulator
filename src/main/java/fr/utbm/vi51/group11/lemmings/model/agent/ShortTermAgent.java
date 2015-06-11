package fr.utbm.vi51.group11.lemmings.model.agent;

import java.util.List;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.discrete.object2d.Rectangle2i;

import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.BodyState;
import fr.utbm.vi51.group11.lemmings.utils.enums.ActionType;
import fr.utbm.vi51.group11.lemmings.utils.enums.InfluenceType;
import fr.utbm.vi51.group11.lemmings.utils.enums.ShortTermAgentOrder;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.misc.Action;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class ShortTermAgent extends Agent {
	class ShortTermAgentOrderState
	{
		public boolean m_completed = false;
		public boolean m_initialized = false;
	}
	
	private Point2f m_startingPoint;
	private ShortTermAgentOrder m_order;
	private ShortTermAgentOrderState m_orderState;
	boolean m_digged = false;
	
	private Point2f m_savedPosition = new Point2f();
	
	private static final float CENTER_EPSILON = 0.5f;
	private static final float CLIMBING_EPSILON = 0.01f;
	
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
			else
			{
				m_body.addInfluence(new Influence(InfluenceType.SPEED, new Vector2f()));
			}
			
		}
	}
	
	public void abortOrder()
	{
		m_orderState.m_completed = true;
	}
	
	public void doOrder(long _dt)
	{
		if(m_body.getState() == BodyState.DEAD)
		{
			m_orderState.m_completed = true;
			return;
		}
		
		switch(m_order)
		{
		case DIG_DOWN:
			digDown(_dt);
			break;
		case DIG_LEFT:
			digLeft(_dt);
			break;
		case DIG_RIGHT:
			digRight(_dt);
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
	
	public void digDown(long _dt)
	{
		if(m_body.getState() == BodyState.DIGGING)
		{
			m_digged = true;
		}
		else if(m_body.getState() == BodyState.NORMAL)
		{
			if(!m_digged)
				m_body.addInfluence(new Influence(InfluenceType.ACTION, new Action(ActionType.DIG_VERTICAL, null)));
			else
				m_orderState.m_completed = true;
		}
	}
	
	public void digRight(long _dt)
	{
		if(m_body.getState() == BodyState.DIGGING)
		{
		}
		else
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
				if(m_body.getState() == BodyState.CLIMBING)
					m_body.addInfluence(new Influence(InfluenceType.ACTION, new Action(ActionType.DIG_HORIZONTAL, null)));
				m_body.addInfluence(new Influence(InfluenceType.SPEED, speed));
			}
		}
	}
	
	public void digLeft(long _dt)
	{
		if(m_body.getState() == BodyState.DIGGING)
		{
		}
		else
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
				if(m_body.getState() == BodyState.CLIMBING)
					m_body.addInfluence(new Influence(InfluenceType.ACTION, new Action(ActionType.DIG_HORIZONTAL, null)));
				m_body.addInfluence(new Influence(InfluenceType.SPEED, speed));
			}
		}
	}
	
	public boolean checkForUmbrella()
	{
		if(m_body.getState() == BodyState.FALLING && m_body.getStateProperty().m_chuteOpen == false)
		{
			if(Math.abs(m_body.getStateProperty().m_fallHeight - m_body.getWorldCoordinates()
					.getY()) <= UtilsLemmings.s_maximumFallingHeight)
			{
				m_body.addInfluence(new Influence(InfluenceType.ACTION, new Action(ActionType.UMBRELLA, null)));
				return true;
			}
		}
		
		return false;
	}
	
	private void goRight(long _dt)
	{
		Point2f bodyCoords = m_startingPoint;
		Point2f closestCellCoords = new Point2f(bodyCoords);
		closestCellCoords.setX((int) (closestCellCoords.getX() / UtilsLemmings.s_tileWidth + 1) * UtilsLemmings.s_tileWidth + UtilsLemmings.s_tileWidth / 2.0f);
		closestCellCoords.setY((int) (closestCellCoords.getY() / UtilsLemmings.s_tileHeight + 1) * UtilsLemmings.s_tileHeight + UtilsLemmings.s_tileHeight / 2.0f);
		if(Math.abs(m_body.getCenterCoordinates().getX() - closestCellCoords.getX()) <= CENTER_EPSILON && m_body.getState() == BodyState.NORMAL)
		{
			m_orderState.m_completed = true;
			m_body.addInfluence(new Influence(InfluenceType.SPEED, new Vector2f()));
		}
		else
		{
			Vector2f speed = new Vector2f();
			speed.setX((closestCellCoords.getX() - m_body.getCenterCoordinates().getX()) / _dt);
			if(m_body.getState() == BodyState.CLIMBING)
			{
				if(Math.abs(m_body.getCenterCoordinates().getY() - m_savedPosition.getY()) < CLIMBING_EPSILON)
				{
					abortOrder();
					m_body.addInfluence(new Influence(InfluenceType.SPEED, new Vector2f()));
					return;
				}
				m_savedPosition.set(m_body.getCenterCoordinates());
				speed.setY(UtilsLemmings.s_maximumClimbingSpeed);	
			}
			m_body.addInfluence(new Influence(InfluenceType.SPEED, speed));
		}
	}
	
	private void goLeft(long _dt)
	{
		Point2f bodyCoords = m_startingPoint;
		Point2f closestCellCoords = new Point2f(bodyCoords);
		closestCellCoords.setX((int) (closestCellCoords.getX() / UtilsLemmings.s_tileWidth - 1) * UtilsLemmings.s_tileWidth + UtilsLemmings.s_tileWidth / 2.0f);
		closestCellCoords.setY((int) (closestCellCoords.getY() / UtilsLemmings.s_tileHeight - 1) * UtilsLemmings.s_tileHeight + UtilsLemmings.s_tileHeight / 2.0f);
		if(Math.abs(m_body.getCenterCoordinates().getX() - closestCellCoords.getX()) <= CENTER_EPSILON && m_body.getState() == BodyState.NORMAL)
		{
			m_orderState.m_completed = true;
			m_body.addInfluence(new Influence(InfluenceType.SPEED, new Vector2f()));
		}
		else
		{
			Vector2f speed = new Vector2f();
			speed.setX((closestCellCoords.getX() - m_body.getCenterCoordinates().getX()) / _dt);
			if(m_body.getState() == BodyState.CLIMBING)
			{
				if(Math.abs(m_body.getCenterCoordinates().getY() - m_savedPosition.getY()) < CLIMBING_EPSILON)
				{
					abortOrder();
					m_body.addInfluence(new Influence(InfluenceType.SPEED, new Vector2f()));
					return;
				}
				m_savedPosition.set(m_body.getCenterCoordinates());
				speed.setY(UtilsLemmings.s_maximumClimbingSpeed);	
			}
			m_body.addInfluence(new Influence(InfluenceType.SPEED, speed));
		}
	}
	
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
			m_savedPosition.set(-1, -1);
			m_digged = false;
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
		return m_orderState.m_completed;
	}

	@Override
	protected Influence decide(List<IPerceivable> _surroundingEntities) {
		// TODO Auto-generated method stub
		return null;
	}

	public ShortTermAgentOrder getCurrentOrder() {
		// TODO Auto-generated method stub
		return m_order;
	}

}

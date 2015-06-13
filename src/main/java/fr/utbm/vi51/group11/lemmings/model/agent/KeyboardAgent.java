package fr.utbm.vi51.group11.lemmings.model.agent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import org.arakhne.afc.math.continous.object2d.Vector2f;

import fr.utbm.vi51.group11.lemmings.model.Simulation;
import fr.utbm.vi51.group11.lemmings.utils.enums.ShortTermAgentOrder;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class KeyboardAgent extends Agent implements KeyListener
{

	boolean	m_upPressed		= false;
	boolean	m_rightPressed	= false;
	boolean	m_leftPressed	= false;
	boolean	m_downPressed	= false;
	
	boolean	m_spacePressed	= false;
	
	boolean m_lPressed = false;
	boolean m_kPressed = false;
	
	ShortTermAgent m_shortAgent = new ShortTermAgent();
	
	@Override
	public void setBody(fr.utbm.vi51.group11.lemmings.utils.interfaces.IControllable _body) 
	{
		super.setBody(_body);
		m_shortAgent.setBody(_body);
		m_shortAgent.enable(true);
	}

	@Override
	public void live(
			final long _dt)
	{
		if (m_body == null)
			return;

		Vector2f speed = new Vector2f();

		if (m_rightPressed && m_shortAgent.isCurrentOrderComplete())
		{
			m_shortAgent.setOrder(ShortTermAgentOrder.GO_RIGHT);
		}
		else if (m_leftPressed && m_shortAgent.isCurrentOrderComplete())
		{
			m_shortAgent.setOrder(ShortTermAgentOrder.GO_LEFT);	
		}

		if (m_upPressed)
			speed.setY(UtilsLemmings.s_maximumClimbingSpeed);
		
		if (m_downPressed && m_shortAgent.isCurrentOrderComplete())
			m_shortAgent.setOrder(ShortTermAgentOrder.DIG_DOWN);
		
		if(m_lPressed)
		{
			m_shortAgent.setOrder(ShortTermAgentOrder.DIG_RIGHT);
			m_lPressed = false;
		}
		
		if(m_kPressed)
		{
			m_shortAgent.setOrder(ShortTermAgentOrder.DIG_LEFT);
			m_kPressed = false;
		}
		
		if(m_spacePressed)
		{
			m_shortAgent.abortOrder();
			m_spacePressed = false;
		}

		m_shortAgent.checkForUmbrella();
		m_shortAgent.live(_dt);
		// m_body.addInfluence(new Influence(InfluenceType.SPEED, speed));
		Simulation.s_LOGGER.debug("BodyState: {}.", m_body.getState());
	}

	@Override
	protected Influence decide(
			final List<IPerceivable> _surroundingEntities)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void keyPressed(
			final KeyEvent arg0)
	{
		if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
			m_rightPressed = true;
		if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
			m_leftPressed = true;
		if (arg0.getKeyCode() == KeyEvent.VK_UP)
			m_upPressed = true;
		if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
			m_downPressed = true;
	}

	@Override
	public void keyReleased(
			final KeyEvent arg0)
	{
		if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
			m_rightPressed = false;
		if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
			m_leftPressed = false;
		if (arg0.getKeyCode() == KeyEvent.VK_UP)
			m_upPressed = false;
		
		if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
			m_downPressed = false;
		
		if (arg0.getKeyCode() == KeyEvent.VK_L)
			m_lPressed = true;
		
		if (arg0.getKeyCode() == KeyEvent.VK_K)
			m_kPressed = true;
		
		if (arg0.getKeyCode() == KeyEvent.VK_SPACE)
			m_spacePressed = true;
		
	}

	@Override
	public void keyTyped(
			final KeyEvent arg0)
	{

	}

}

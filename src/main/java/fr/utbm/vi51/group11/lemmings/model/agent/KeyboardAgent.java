package fr.utbm.vi51.group11.lemmings.model.agent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import org.arakhne.afc.math.continous.object2d.Vector2f;

import fr.utbm.vi51.group11.lemmings.model.Simulation;
import fr.utbm.vi51.group11.lemmings.utils.enums.InfluenceType;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;
import fr.utbm.vi51.group11.lemmings.utils.statics.LemmingUtils;

public class KeyboardAgent extends Agent implements KeyListener {

	boolean m_upPressed = false;
	boolean m_rightPressed = false;
	boolean m_leftPressed = false;
	boolean m_downPressed = false;
	
	@Override
	public void live(long _dt) {
		if(m_body == null)
			return;
		
		Vector2f speed = new Vector2f();
		
		if(m_rightPressed)
			speed.setX(LemmingUtils.s_lemmingMaxVelocity);
		else if(m_leftPressed)
			speed.setX(-LemmingUtils.s_lemmingMaxVelocity);
		
		if(m_upPressed)
			speed.setY(-LemmingUtils.MAXIMUM_CLIMBING_SPEED);
		
		m_body.addInfluence(new Influence(InfluenceType.SPEED, speed));
		// Simulation.s_LOGGER.debug("BodyState: {}.", m_body.getState());
	}

	@Override
	protected Vector2f decide(List<IPerceivable> _surroundingEntities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void move(Vector2f _direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_RIGHT)
			m_rightPressed = true;
		if(arg0.getKeyCode() == KeyEvent.VK_LEFT)
			m_leftPressed = true;
		if(arg0.getKeyCode() == KeyEvent.VK_UP)
			m_upPressed = true;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_RIGHT)
			m_rightPressed = false;
		if(arg0.getKeyCode() == KeyEvent.VK_LEFT)
			m_leftPressed = false;
		if(arg0.getKeyCode() == KeyEvent.VK_UP)
			m_upPressed = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

}

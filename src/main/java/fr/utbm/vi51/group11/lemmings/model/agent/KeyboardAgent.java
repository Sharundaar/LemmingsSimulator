package fr.utbm.vi51.group11.lemmings.model.agent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import org.arakhne.afc.math.continous.object2d.Vector2f;

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
		boolean res = false;
		if(m_rightPressed)
			res = m_body.addInfluence(new Influence(InfluenceType.SPEED, new Vector2f(LemmingUtils.s_lemmingMaxVelocity, 0)));
		else if(m_leftPressed)
			res = m_body.addInfluence(new Influence(InfluenceType.SPEED, new Vector2f(-LemmingUtils.s_lemmingMaxVelocity, 0)));
		else
			res = m_body.addInfluence(new Influence(InfluenceType.SPEED, new Vector2f(0, 0)));
	}

	@Override
	protected List<IPerceivable> getPerceptions() {
		// TODO Auto-generated method stub
		return null;
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
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_RIGHT)
			m_rightPressed = false;
		if(arg0.getKeyCode() == KeyEvent.VK_LEFT)
			m_leftPressed = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

}

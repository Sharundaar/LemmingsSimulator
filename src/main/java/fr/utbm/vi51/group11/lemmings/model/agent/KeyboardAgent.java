package fr.utbm.vi51.group11.lemmings.model.agent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import org.arakhne.afc.math.continous.object2d.Vector2f;

import fr.utbm.vi51.group11.lemmings.model.Simulation;
import fr.utbm.vi51.group11.lemmings.utils.enums.InfluenceType;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class KeyboardAgent extends Agent implements KeyListener
{

	boolean	m_upPressed		= false;
	boolean	m_rightPressed	= false;
	boolean	m_leftPressed	= false;
	boolean	m_downPressed	= false;

	@Override
	public void live(
			final long _dt)
	{
		if (m_body == null)
			return;

		Vector2f speed = new Vector2f();

		if (m_rightPressed)
			speed.setX(UtilsLemmings.s_lemmingMaxVelocity);
		else if (m_leftPressed)
			speed.setX(-UtilsLemmings.s_lemmingMaxVelocity);

		if (m_upPressed)
			speed.setY(-UtilsLemmings.s_maximumClimbingSpeed);

		m_body.addInfluence(new Influence(InfluenceType.SPEED, speed));
		// Simulation.s_LOGGER.debug("BodyState: {}.", m_body.getState());
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
	}

	@Override
	public void keyTyped(
			final KeyEvent arg0)
	{

	}

}

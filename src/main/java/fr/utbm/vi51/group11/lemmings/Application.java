package fr.utbm.vi51.group11.lemmings;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.joran.spi.JoranException;
import fr.utbm.vi51.group11.lemmings.gui.texture.TextureBank;
import fr.utbm.vi51.group11.lemmings.model.Simulation;
import fr.utbm.vi51.group11.lemmings.model.agent.Agent;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelPropertiesMap;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsFile;

public class Application implements WindowListener
{

	/** Logger of the class */
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(Application.class);

	private Simulation			m_simulation;

	public Application() throws JoranException
	{
		/* Initializes the logger and the logfile */
		UtilsFile.initLogger();
	}

	public void go() throws Exception
	{
		s_LOGGER.info("Application launched.");

		float dt = 0;

		/* Creates the simulation */
		m_simulation = new Simulation("levelTest");

		/* Adds a window listener to the application */
		((JFrame) SwingUtilities.getWindowAncestor(m_simulation.getEnvironment()
				.getGraphicsEngine())).addWindowListener(this);

		float fps = 60;
		long targetTPF = (long) (1.0 / (fps * 1000));

		long start;
		long end;

		while (true)
		{
			start = System.currentTimeMillis();

			for (Agent a : m_simulation.getAgents())
				a.live(start);

			m_simulation.getEnvironment().getGraphicsEngine().repaint();

			end = System.currentTimeMillis() - start;

			if ((end / 1000) < targetTPF)
				Thread.sleep(targetTPF - (end / 1000));

			dt = System.currentTimeMillis() - start;
		}
	}

	private void destroyApplication()
	{
		m_simulation.destroy();
		TextureBank.getInstance().destroy();
		LevelPropertiesMap.getInstance().destroy();
		s_LOGGER.info("Application terminated");
	}

	@Override
	public void windowOpened(
			final WindowEvent e)
	{
	}

	@Override
	public void windowClosing(
			final WindowEvent e)
	{
		destroyApplication();
	}

	@Override
	public void windowClosed(
			final WindowEvent e)
	{
	}

	@Override
	public void windowIconified(
			final WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(
			final WindowEvent e)
	{
	}

	@Override
	public void windowActivated(
			final WindowEvent e)
	{
	}

	@Override
	public void windowDeactivated(
			final WindowEvent e)
	{
	}
}
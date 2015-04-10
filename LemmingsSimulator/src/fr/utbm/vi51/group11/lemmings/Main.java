package fr.utbm.vi51.group11.lemmings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.joran.spi.JoranException;
import fr.utbm.vi51.group11.lemmings.model.Simulation;
import fr.utbm.vi51.group11.lemmings.utils.statics.FileUtils;

public class Main
{
	/** Logger of the class */
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(Main.class);

	public static void main(
			final String[] args) throws JoranException
	{
		FileUtils.initLogger();

		Simulation simulation = null;
		try
		{
			s_LOGGER.info("Main launched.");

			simulation = new Simulation("levelTest");

			float FRAMERATE = 500;

			while (true)
			{
				long start = System.currentTimeMillis();

				// for (Agent a : s.getAgents())
				// a.live();

				while ((System.currentTimeMillis() - start) < FRAMERATE)
				{
				}

				simulation.getEnvironment().getGraphicsEngine().repaint();
			}
		} finally
		{
			if (simulation != null)
				simulation.destroy();
			s_LOGGER.info("Main terminated");
		}
	}
}
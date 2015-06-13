package fr.utbm.vi51.group11.lemmings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.joran.spi.JoranException;
import fr.utbm.vi51.group11.lemmings.gui.LoaderMenuItem.LevelObservable;
import fr.utbm.vi51.group11.lemmings.gui.MainFrame;
import fr.utbm.vi51.group11.lemmings.gui.texture.TextureBank;
import fr.utbm.vi51.group11.lemmings.model.Simulation;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelProperties;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelPropertiesMap;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsFile;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class Application implements Observer
{

	/** Logger of the class */
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(Application.class);

	private String				m_currentSimulationID;
	private Simulation			m_simulation;

	private final List<String>	m_simulationIDs;

	private boolean				m_simulating;

	public Application() throws JoranException, IOException
	{
		/* Initializes the logger and the logfile */
		UtilsFile.initLogger();

		m_simulationIDs = new ArrayList<String>();
		m_simulating = true;
	}

	public void go()
	{
		s_LOGGER.info("Application launched.");
		LevelProperties currentLevelProperties;

		m_simulationIDs.addAll(LevelPropertiesMap.getInstance().keySet());
		m_currentSimulationID = m_simulationIDs.get(0);

		MainFrame gui = new MainFrame("Lemmings Simulator");

		/* Make the Frame listen to the application */
		gui.setApplicationObserver(this);

		while (m_simulating)
		{
			/* Creation of the attributes */
			currentLevelProperties = LevelPropertiesMap.getInstance().get(m_currentSimulationID);

			/* Creates the simulation */
			m_simulation = new Simulation(currentLevelProperties);

			gui.initialize(m_simulation,
					UtilsLemmings.s_tileWidth * currentLevelProperties.getNbCol(),
					UtilsLemmings.s_tileHeight * currentLevelProperties.getNbRow());

			m_simulation.getEnvironment().setMainFrame(gui);

			m_simulation.loop();

			m_simulation.destroy();
		}
		gui.dispose();
		destroyApplication();
		s_LOGGER.info("Application destroyed.");
	}

	private void destroyApplication()
	{
		TextureBank.getInstance().destroy();
		LevelPropertiesMap.getInstance().destroy();
		s_LOGGER.info("Simulation destroyed.");
	}

	@Override
	public void update(
			final Observable _observable,
			final Object _arg)
	{
		if (_observable instanceof LevelObservable)
		{
			m_simulation.stop();
			m_currentSimulationID = (String) _arg;
		} else
		{
			m_simulating = false;
			m_simulation.stop();
		}
	}
}
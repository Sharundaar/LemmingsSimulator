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

	private Simulation			m_simulation;

	private final List<String>	m_simulationIDs;

	private MainFrame			m_gui;

	public Application() throws JoranException, IOException
	{
		/* Initializes the logger and the logfile */
		UtilsFile.initLogger();

		m_simulationIDs = new ArrayList<String>();
	}

	public void go()
	{
		s_LOGGER.info("Application launched.");

		m_simulationIDs.addAll(LevelPropertiesMap.getInstance().keySet());
		String currentSimulationID = m_simulationIDs.get(0);

		m_gui = new MainFrame("Lemmings Simulator");

		/* Make the Frame listen to the application */
		m_gui.setApplicationObserver(this);

		/* Creation of the attributes */
		LevelProperties currentLevelProperties = LevelPropertiesMap.getInstance().get(
				currentSimulationID);

		/* Creates the simulation */
		m_simulation = new Simulation(currentLevelProperties);

		m_gui.initialize(m_simulation,
				UtilsLemmings.s_tileWidth * currentLevelProperties.getNbCol(),
				UtilsLemmings.s_tileHeight * currentLevelProperties.getNbRow());

		m_simulation.getEnvironment().setMainFrame(m_gui);

		m_simulation.loop();

		m_simulation.destroy();

		m_gui.dispose();
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
		/* Changing the environment */
		if (_observable instanceof LevelObservable)
		{
			String currentSimulationID = (String) _arg;

			LevelProperties newLevelProperties = LevelPropertiesMap.getInstance().get(
					currentSimulationID);
			m_simulation.setLevelProperties(newLevelProperties);
			m_simulation.stopRunning();

			/* Stopping the simulation. */
		} else
		{
			m_simulation.stopRunning();
			m_simulation.stopSimulating();
		}
	}
}
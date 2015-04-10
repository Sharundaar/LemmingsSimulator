package fr.utbm.vi51.group11.lemmings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.joran.spi.JoranException;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelPropertiesMap;
import fr.utbm.vi51.group11.lemmings.utils.statics.FileUtils;

public class MainTest
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(MainTest.class);

	public static void main(
			final String[] args) throws JoranException
	{
		FileUtils.initLogger();

		LevelPropertiesMap.getInstance();

		System.out.println(LevelPropertiesMap.getInstance());

	}
}
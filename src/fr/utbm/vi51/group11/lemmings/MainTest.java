package fr.utbm.vi51.group11.lemmings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelPropertiesMap;
import fr.utbm.vi51.group11.lemmings.utils.statics.ConfigurationUtils;
import fr.utbm.vi51.group11.lemmings.utils.statics.FileUtils1;

public class MainTest
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(MainTest.class);

	public static void main(
			final String[] args) throws Exception
	{

		ConfigurationUtils.setupConfigurationDirectory();

		FileUtils1.initLogger();

		LevelPropertiesMap.getInstance();

		System.out.println(LevelPropertiesMap.getInstance());

	}
}
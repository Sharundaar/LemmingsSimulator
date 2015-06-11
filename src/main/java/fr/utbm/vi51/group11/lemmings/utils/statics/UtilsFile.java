package fr.utbm.vi51.group11.lemmings.utils.statics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class UtilsFile
{
	/** Logger of the class */
	private final static Logger	LOGGER						= LoggerFactory
																	.getLogger(UtilsFile.class);

	/** FilePath referencing the resource dir */
	public static final Path	RESOURCES_DIR				= Paths.get("resources");

	public static final String	TEXTURE_DIR_NAME			= "textures";

	/** FilePath referencing the texture dir */
	public static final Path	TEXTURE_DIR					= RESOURCES_DIR
																	.resolve(TEXTURE_DIR_NAME);

	/** File extension for sprite Sheets */
	public static final String	SPRITESHEET_FILE_EXTENSION	= ".png";

	/** User home directory */
	public static final Path	USER_HOME_DIR				= Paths.get(System
																	.getProperty("user.home"));

	/** Application configuration directory */
	public static final Path	USER_CONFIGURATION_DIR		= USER_HOME_DIR
																	.resolve("LemmingsSimulator");

	/** Application saves */
	public static final Path	CONFIGURATION_SAVE_DIR		= USER_CONFIGURATION_DIR
																	.resolve("saves");

	/** Application textures */
	public static final Path	CONFIGURATION_TEXTURE_DIR	= USER_CONFIGURATION_DIR
																	.resolve("textures");

	/** String name of the logback file */
	public static final String	LOGBACK_FILENAME			= "logback_lemmings.xml";

	/** Filepath of the logback file */
	public static final Path	LOGBACK_FILEPATH			= USER_CONFIGURATION_DIR
																	.resolve(LOGBACK_FILENAME);

	/** String name of the levels configuration file */
	public static final String	LEVEL_CONF_FILENAME			= "levels.xml";

	/**
	 * Path of the application file corresponding to the level configuration
	 * file
	 */
	public static final Path	LEVEL_CONF_FILEPATH			= Paths.get(LEVEL_CONF_FILENAME);

	/**
	 * Method used to initialize the log file.
	 * 
	 * @throws JoranException
	 * @throws IOException
	 */
	public static void initLogger() throws JoranException, IOException
	{
		System.setProperty("configuration.dir", Paths.get("./").toAbsolutePath().toString());
		JoranConfigurator configurator = new JoranConfigurator();
		LoggerContext c = (LoggerContext) LoggerFactory.getILoggerFactory();
		c.reset();
		configurator.setContext(c);
		// configurator.doConfigure(FileUtils1.LOGBACK_FILEPATH.toFile());
		// configurator.doConfigure(FileUtils1.RESOURCES_DIR.resolve(LOGBACK_FILENAME).toFile());
		System.out.println((new File(".").getAbsolutePath()));
		if (Files.notExists(Paths.get("log")))
			Files.createDirectories(Paths.get("log"));
		if (Files.exists(Paths.get("src/main/resources/logback_lemmings.xml")))
			configurator.doConfigure(configurator.getClass().getClassLoader()
					.getResourceAsStream("logback_lemmings.xml"));
		else
			configurator.doConfigure(configurator.getClass().getClassLoader()
					.getResourceAsStream("resources/logback_lemmings.xml"));

		LOGGER.debug("Log file initialized.");
	}

	/**
	 * Method used to read the bytes from a file.
	 * 
	 * @param _file
	 *            File to read.
	 * @return Bytes contained in the file.
	 * @throws IOException
	 */
	public static byte[] readBytesFromFile(
			final File _file) throws IOException
	{
		return Files.readAllBytes(_file.toPath());
	}
}

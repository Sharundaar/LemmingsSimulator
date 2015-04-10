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

public class FileUtils
{
	/** Logger of the class */
	private final static Logger	LOGGER					= LoggerFactory.getLogger(FileUtils.class);

	/** Filepath of the logback file */
	public static final Path	LOGBACK_FILEPATH		= Paths.get("resources/logback_lemmings.xml");

	/** FilePath referencing the resource dir */
	public static final Path	RESOURCES_DIR			= Paths.get("resources");

	/** FilePath referencing the texture dir */
	public static final Path	TEXTURES_DIR			= RESOURCES_DIR.resolve("textures");

	/** File extension for sprite Sheets */
	public static final String	SPRITESHEET_FILE_EXTENSION	= ".png";

	/**
	 * Method used to initialize the log file.
	 * 
	 * @throws JoranException
	 */
	public static void initLogger() throws JoranException
	{
		JoranConfigurator configurator = new JoranConfigurator();
		LoggerContext c = (LoggerContext) LoggerFactory.getILoggerFactory();
		c.reset();
		configurator.setContext(c);
		configurator.doConfigure(FileUtils.LOGBACK_FILEPATH.toFile());

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

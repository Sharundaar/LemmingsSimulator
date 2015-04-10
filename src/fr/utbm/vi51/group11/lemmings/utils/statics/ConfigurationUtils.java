package fr.utbm.vi51.group11.lemmings.utils.statics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationUtils
{

	/** Logger of the class */
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(ConfigurationUtils.class);

	public static void setupConfigurationDirectory() throws IOException
	{
		if (Files.notExists(FileUtils1.USER_CONFIGURATION_DIR))
		{
			/* Creates the LemmingsSimulator in the user directory */
			Files.createDirectories(FileUtils1.USER_CONFIGURATION_DIR);
		}

		/* for all resources files and directories */
		for (File f : FileUtils1.RESOURCES_DIR.toFile().listFiles())
		{
			/* If the file doesn't exists */
			if (Files.notExists(FileUtils1.USER_CONFIGURATION_DIR.resolve(f.getName())))
			{
				if (f.isDirectory())
					FileUtils.copyDirectoryToDirectory(f,
							FileUtils1.USER_CONFIGURATION_DIR.toFile());
				else
					FileUtils.copyFileToDirectory(f, FileUtils1.USER_CONFIGURATION_DIR.toFile());
			}
		}

		/* Creates the save directory */
		Files.createDirectories(FileUtils1.CONFIGURATION_SAVE_DIR);
	}
}

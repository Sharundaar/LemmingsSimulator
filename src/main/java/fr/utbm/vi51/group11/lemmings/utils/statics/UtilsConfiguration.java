package fr.utbm.vi51.group11.lemmings.utils.statics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;

public class UtilsConfiguration
{
	public static void setupConfigurationDirectory() throws IOException
	{
		if (Files.notExists(UtilsFile.USER_CONFIGURATION_DIR))
		{
			/* Creates the LemmingsSimulator in the user directory */
			Files.createDirectories(UtilsFile.USER_CONFIGURATION_DIR);
		}

		/* for all resources files and directories */
		for (File f : UtilsFile.RESOURCES_DIR.toFile().listFiles())
		{
			/* If the file doesn't exists */
			if (Files.notExists(UtilsFile.USER_CONFIGURATION_DIR.resolve(f.getName())))
			{
				if (f.isDirectory())
					FileUtils.copyDirectoryToDirectory(f,
							UtilsFile.USER_CONFIGURATION_DIR.toFile());
				else
					FileUtils.copyFileToDirectory(f, UtilsFile.USER_CONFIGURATION_DIR.toFile());
			}
		}

		/* Creates the save directory */
		Files.createDirectories(UtilsFile.CONFIGURATION_SAVE_DIR);
	}
}

package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.Body;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.LemmingBody;

public class MainTest
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(MainTest.class);

	public static void main(
			final String[] args) throws Exception
	{

		// UtilsConfiguration.setupConfigurationDirectory();
		//
		// UtilsFile.initLogger();
		//
		// // GlobalConfiguration.getInstance();
		//
		// System.out.println(ImageIO.read(new File(
		// "/home/jnovak/LemmingsSimulator/textures/entitySpriteSheet.png")));

		LemmingBody bdy = null;

		if (bdy instanceof Body)
			System.out.println("rue");
		else
			System.out.println("false");

	}
}
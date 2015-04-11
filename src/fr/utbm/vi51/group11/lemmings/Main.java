package fr.utbm.vi51.group11.lemmings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(Main.class);

	public static void main(
			final String[] args) throws Exception
	{
		Application app = new Application();

		app.go();
	}
}
package fr.utbm.vi51.group11.lemmings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.joran.spi.JoranException;

public class Main
{
	/** Logger of the class */
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(Main.class);

	public static void main(
			final String[] args) throws JoranException
	{
		Application app = new Application();

		app.go();
	}
}
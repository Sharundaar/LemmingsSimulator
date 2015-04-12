package fr.utbm.vi51.group11.lemmings.utils.statics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LemmingUtils
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER					= LoggerFactory.getLogger(LemmingUtils.class);
	
	/** Default size of a lemming */
	public final static float LEMMING_DEFAULT_WIDTH  = 50;
	public final static float LEMMING_DEFAULT_HEIGHT = 20;
	
	/** Default size of the entry */
	public final static float ENTRY_DEFAULT_WIDTH = 5;
	public final static float ENTRY_DEFAULT_HEIGHT = 5;
	
	/** Default size of the entry */
	public final static float EXIT_DEFAULT_WIDTH = 5;
	public final static float EXIT_DEFAULT_HEIGHT = 5;

	/** Default maximum velocity of the lemmings */
	public final static float	s_lemmingMaxVelocity		= 12;
	
	/** Default maximum acceleration of a lemming */
	public final static float 	s_lemmingMaxAcceleration	= 1;
}

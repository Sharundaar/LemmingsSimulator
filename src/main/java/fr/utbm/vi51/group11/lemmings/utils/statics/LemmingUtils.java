package fr.utbm.vi51.group11.lemmings.utils.statics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LemmingUtils
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER					= LoggerFactory.getLogger(LemmingUtils.class);
	
	/** Default size of a lemming */
	public final static float LEMMING_DEFAULT_WIDTH  = 20;
	public final static float LEMMING_DEFAULT_HEIGHT = 20;
	
	/** Default size of the entry */
	public final static float ENTRY_DEFAULT_WIDTH = 60;
	public final static float ENTRY_DEFAULT_HEIGHT = 22;
	
	/** Default size of the entry */
	public final static float EXIT_DEFAULT_WIDTH = 60;
	public final static float EXIT_DEFAULT_HEIGHT = 22;

	/** Default maximum velocity of the lemmings */
	public final static float	s_lemmingMaxVelocity		= 35.0f / 1000.0f;
	
	/** Default maximum fall tolerence of the lemmings */
	public final static float MAXIMUM_FALLING_HEIGHTS = 65.0f;
	
	/** Default maximum acceleration of a lemming */
	public final static float 	s_lemmingMaxAcceleration	= 1;
	
	public final static float MAXIMUM_FALLING_SPEED = 100.0f / 1000.0f;
}

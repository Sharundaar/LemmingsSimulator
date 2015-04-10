package fr.utbm.vi51.group11.lemmings.utils.statics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LemmingUtils
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER					= LoggerFactory.getLogger(LemmingUtils.class);

	/** Default maximum velocity of the lemmings */
	public final static float	s_lemmingMaxVelocity		= 12;
	
	/** Default maximum acceleration of a lemming */
	public final static float 	s_lemmingMaxAcceleration	= 1;
}

package fr.utbm.vi51.group11.lemmings.utils.statics;

public class UtilsLemmings
{
	/** Default size of a lemming */
	public final static int		s_lemmingEntityWidth			= 30;
	public final static int		s_lemmingEntityHeight			= 30;

	public final static int		s_lemmingSpriteWidth			= 28;
	public final static int		s_lemmingSpriteHeight			= 28;

	/** Default size of the entry */
	public final static int		s_entryDefaultWidth				= 64;
	public final static int		s_entryDefaultHeight			= 32;

	/** Default size of the entry */
	public final static int		s_exitDefaultWidth				= 64;
	public final static int		s_exitDefaultHeight				= 32;

	/** Default width and height of tile */
	public final static int		s_tileWidth						= 32;
	public final static int		s_tileHeight					= 32;

	/** Default maximum velocity of the lemmings */
	public final static float	s_lemmingMaxVelocity			= 35.0f / 1000.0f;

	/** Default maximum acceleration of a lemming */
	public final static float	s_lemmingMaxAcceleration		= 1;

	public final static float	s_lemmingMass					= 1;

	/** Default maximum fall tolerence of the lemmings */
	public final static float	s_maximumFallingHeight			= 65.0f;

	public final static float	s_maximumFallingSpeed			= 100.0f / 1000.0f;
	public final static float	s_maximumClimbingSpeed			= -35.0f / 1000.0f;

	public final static float	s_lemmingAnimationTimeGap		= 500f;

	public final static int		s_lemmingSpriteSheetPixelGap	= 28;
	
	public final static long 	s_diggingSpeed 					= 1000;
}

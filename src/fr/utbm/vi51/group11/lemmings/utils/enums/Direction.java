package fr.utbm.vi51.group11.lemmings.utils.enums;

import java.util.Random;

/**
 * Enumeration designed to translate directions into a vector of coordinate
 * (dx,dy).
 * 
 * @author jnovak
 *
 */
public enum Direction
{
	/** North. */
	NORTH(0, -1)
	{
		@Override
		public Direction opposite()
		{
			return SOUTH;
		}
	},
	/** North-West. */
	NORTH_WEST(-1, -1)
	{
		@Override
		public Direction opposite()
		{
			return SOUTH_EAST;
		}
	},
	/** West. */
	WEST(-1, 0)
	{
		@Override
		public Direction opposite()
		{
			return EAST;
		}
	},
	/** South-West. */
	SOUTH_WEST(-1, +1)
	{
		@Override
		public Direction opposite()
		{
			return NORTH_EAST;
		}
	},
	/** South. */
	SOUTH(0, +1)
	{
		@Override
		public Direction opposite()
		{
			return NORTH;
		}
	},
	/** South-East. */
	SOUTH_EAST(+1, +1)
	{
		@Override
		public Direction opposite()
		{
			return NORTH_WEST;
		}
	},
	/** East. */
	EAST(+1, 0)
	{
		@Override
		public Direction opposite()
		{
			return WEST;
		}
	},
	/** North-West. */
	NORTH_EAST(+1, -1)
	{
		@Override
		public Direction opposite()
		{
			return SOUTH_WEST;
		}
	},
	/** None. */
	NONE(0, 0)
	{
		@Override
		public Direction opposite()
		{
			return NONE;
		}
	};

	/*----------------------------------------------*/

	/** Relative coordinate of the direction. */
	public final int	m_Dx;

	/** Relative coordinate of the direction. */
	public final int	m_Dy;

	/*----------------------------------------------*/

	/**
	 * Default Constructor instantiating the coordinate of the vector of the
	 * direction to create.
	 * 
	 * @param _X
	 *            x coordinate of the direction.
	 * @param _Y
	 *            y coordinate of the direction.
	 */
	Direction(final int _X, final int _Y)
	{
		this.m_Dx = _X;
		this.m_Dy = _Y;
	}

	/*----------------------------------------------*/

	/**
	 * @return A random direction excluding the "NONE" one.
	 */
	public static Direction random()
	{
		Random rnd = new Random();
		return values()[rnd.nextInt(values().length - 1)];
	}

	/*----------------------------------------------*/

	/**
	 * @return A direction along the x-axis (EAST or WEST)
	 */
	public static Direction randomXDirection()
	{
		Random rnd = new Random();
		return (rnd.nextBoolean() == true) ? EAST : WEST;
	}

	/*----------------------------------------------*/

	/**
	 * @return A direction along the y-axis (EAST or WEST)
	 */
	public static Direction randomYDirection()
	{
		Random rnd = new Random();
		return (rnd.nextBoolean() == true) ? NORTH : SOUTH;
	}

	/*----------------------------------------------*/

	/**
	 * @return The opposite direction of the direction (for "NONE", its opposite
	 *         is "NONE").
	 */
	public abstract Direction opposite();

}

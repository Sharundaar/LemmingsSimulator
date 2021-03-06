package fr.utbm.vi51.group11.lemmings.utils.enums;

/**
 * Enumeration designed to specify each type of cell that exists. Cells are
 * crossable (or not) and dangerous (or not).
 * 
 * @author jnovak
 *
 */
public enum CellType
{
	BACK_WALL_BRIGHT(0, true, false),
	TOXIC_BACK_WALL_BRIGHT(1, true, true),
	PIT_BACK_WALL_BRIGHT(2, true, false),
	BACK_WALL_GRANIT(3, true, false),
	TOXIC_BACK_WALL_GRANIT(4, true, true),
	PIT_BACK_WALL_GRANIT(5, true, false),
	BACK_WALL_CONCRETE(6, true, false),
	TOXIC_BACK_WALL_CONCRETE(7, true, true),
	PIT_BACK_WALL_CONCRETE(8, true, false),
	BRICK_STONE(9, false, false),
	DIRT(10, false, false),
	GRASS(11, false, false),
	ATTRACTIVE_FIELD(12, false, false),
	REPULSIVE_FIELD(13, false, false);

	/*----------------------------------------------*/

	/** ID of the cell type */
	private final int		m_id;

	/** Tells if the cell is crossable or not */
	private final boolean	m_crossable;

	/** Tells if the cell is dangerous or not */
	private final boolean	m_dangerous;

	/*----------------------------------------------*/

	/**
	 * Constructor initializing attributes.
	 * 
	 * @param _crossable
	 *            Tells if the cell is crossable or not.
	 * @param _dangerous
	 *            Tells if the cell is dangerous or not.
	 */
	private CellType(final int _id, final boolean _crossable, final boolean _dangerous)
	{
		m_id = _id;
		m_crossable = _crossable;
		m_dangerous = _dangerous;
	}

	/*----------------------------------------------*/

	public static CellType valueOf(
			final int _id)
	{
		CellType[] cellTypes = CellType.values();
		boolean stop = false;
		int i = 0;
		while (!stop && (i < cellTypes.length))
		{
			if (_id == cellTypes[i].getID())
			{
				stop = true;
				--i;
			}
			++i;
		}
		if (stop == false)
			return null;
		return cellTypes[i];
	}

	/*----------------------------------------------*/

	public int getID()
	{
		return m_id;
	}

	/*----------------------------------------------*/

	/**
	 * @return True if the cell can be crossed.</br>False otherwise.
	 */
	public boolean isCrossable()
	{
		return m_crossable;
	}

	/*----------------------------------------------*/

	/**
	 * @return True if the cell can kill a lemming.</br>False otherwise.
	 */
	public boolean isDangerous()
	{
		return m_dangerous;
	}
}

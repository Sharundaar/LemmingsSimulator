package fr.utbm.vi51.group11.lemmings.model.map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.utils.enums.CellType;

/**
 * 
 * Class designed to divide the map in a matrix of Cells. The number of lines
 * and columns are determined from the constructor called by the Environment.
 * 
 * @author jnovak
 *
 */
public class Grid
{
	/** Logger of the class */
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(Grid.class);

	/** Matrix of Cells */
	private final Cell[][]		m_cells;

	/*----------------------------------------------*/

	/**
	 * Default Constructor of a GameGrid. Creates the matrix in function of the
	 * parameters.
	 * 
	 * @param _rowNb
	 *            Number of rows of the grid.
	 * @param _colNb
	 *            Number of columns of the grid.
	 */
	public Grid(final int _rowNb, final int _colNb, final int[][] _tileGrid)
	{
		s_LOGGER.debug("Creation of the Grid with '{}' rows and '{}' columns...", _rowNb, _colNb);

		m_cells = new Cell[_rowNb][_colNb];
		for (int i = 0; i < _rowNb; ++i)
			for (int j = 0; j < _colNb; ++j)
			{
				m_cells[i][j] = new Cell(i, j);
				m_cells[i][j].setCellType(CellType.valueOf(_tileGrid[i][j]));
			}

		s_LOGGER.debug("Grid created.");
	}

	/*----------------------------------------------*/

	/**
	 * @return Returns the matrix containing the cells.
	 */
	public Cell[][] getCells()
	{
		return m_cells;
	}

	/*----------------------------------------------*/

	/**
	 * @return The number of lines of the GameGrid.
	 */
	public int getWidth()
	{
		return m_cells.length;
	}

	/*----------------------------------------------*/

	/**
	 * @return The number of columns of the GameGrid.
	 */
	public int getHeight()
	{
		return m_cells[0].length;
	}

	/*----------------------------------------------*/

	/**
	 * @param _x
	 *            Number of the line where is located the Cell.
	 * @param _y
	 *            Number of the column where is located the Cell.
	 * @return Cell corresponding to the specified indexes.
	 */
	public Cell getCell(
			final int _x,
			final int _y)
	{
		return m_cells[_x][_y];
	}
}
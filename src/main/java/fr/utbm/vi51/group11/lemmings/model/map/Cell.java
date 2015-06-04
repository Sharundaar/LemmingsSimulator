package fr.utbm.vi51.group11.lemmings.model.map;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;

import fr.utbm.vi51.group11.lemmings.model.physics.shapes.RectangleShape;
import fr.utbm.vi51.group11.lemmings.utils.enums.CellType;

/**
 * 
 * Class representing the Cell of a GameGrid, which is a subdivision of the map
 * in a matrix of Cells.</br>
 * The Cell can be crossable or not depending on the type of field.
 * 
 * @author jnovak
 *
 */
public class Cell
{
	/** x coordinate of the cell */
	private final int	m_x;

	/** y coordinate of the cell */
	private final int	m_y;

	/** Type defining the cell */
	private CellType	m_type;
	
	private RectangleShape m_rectangle;

	/*----------------------------------------------*/

	/**
	 * Default constructor defining a Cell with a row number and a column
	 * number.
	 * 
	 * @param _x
	 *            Row number of the Cell.
	 * @param _y
	 *            Column number of the Cell.
	 */
	public Cell(final int _x, final int _y)
	{
		this.m_x = _x;
		this.m_y = _y;
		
		m_rectangle = new RectangleShape(_x*Map.CELL_SIZE, _y*Map.CELL_SIZE, Map.CELL_SIZE, Map.CELL_SIZE, null);
		m_rectangle.updateShape();
	}

	/*----------------------------------------------*/

	/**
	 * @return Row Number of the Cell.
	 */
	public int getX()
	{
		return m_x;
	}

	/*----------------------------------------------*/

	/**
	 * @return Column number of the Cell.
	 */
	public int getY()
	{
		return m_y;
	}

	/*----------------------------------------------*/

	/**
	 * @return CellType of the cell.
	 */
	public CellType getCellType()
	{
		return this.m_type;
	}

	/*----------------------------------------------*/

	/**
	 * Method used to set the 'type' attribute to a CellType.
	 * 
	 * @param _type
	 *            CellType to assign to the attribute 'type'.
	 */
	public void setCellType(
			final CellType _type)
	{
		this.m_type = _type;
	}
	
	/*----------------------------------------------*/
	public RectangleShape getRectangle()
	{
		return m_rectangle;
	}
}
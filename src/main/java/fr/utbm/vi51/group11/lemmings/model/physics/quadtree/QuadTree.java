package fr.utbm.vi51.group11.lemmings.model.physics.quadtree;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.physics.collidingobjects.CollidingObjects;
import fr.utbm.vi51.group11.lemmings.model.physics.collidingobjects.CollidingObjectsSet;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.RectangleShape;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class QuadTree
{

	public class QuadTreeNode
	{
		public static final int						s_threasold		= 3;
		public static final float					s_minimalArea	= 32;

		private final RectangleShape				m_shape;

		private QuadTreeNode						m_nw;
		private QuadTreeNode						m_ne;
		private QuadTreeNode						m_sw;
		private QuadTreeNode						m_se;

		private final LinkedList<CollisionShape>	m_elements		= new LinkedList<CollisionShape>();

		public QuadTreeNode(final RectangleShape _shape)
		{
			m_shape = _shape;
			m_shape.updateShape();
			m_nw = m_ne = m_sw = m_se = null;
		}

		public RectangleShape getShape()
		{
			// TODO Auto-generated method stub
			return m_shape;
		}

		public boolean isLeaf()
		{
			return m_nw == null;
		}

		public boolean canExpand()
		{
			return (m_shape.getWidth() * m_shape.getHeight()) > s_minimalArea;
		}

		public boolean addElement(
				final CollisionShape _elem)
		{
			if (_elem.collide(m_shape))
			{
				if (isLeaf() && ((m_elements.size() < s_threasold) || !canExpand()))
				{
					m_elements.add(_elem);
					return true;
				} else if (isLeaf())
				{
					createChilds();
					LinkedList<CollisionShape> tmp = new LinkedList<CollisionShape>(m_elements);
					tmp.add(_elem);
					m_elements.clear();
					for (CollisionShape elem : tmp)
						addElement(elem);
					return true;
				} else
				{
					return m_nw.addElement(_elem) | m_ne.addElement(_elem) | m_sw.addElement(_elem)
							| m_se.addElement(_elem);
				}
			} else
			{
				return false;
			}
		}

		public boolean removeElement(
				final CollisionShape _elem)
		{
			if (isLeaf())
			{
				if (m_elements.contains(_elem))
				{
					m_elements.remove(_elem);
					return true;
				}
				return false;
			} else
			{
				boolean elemRemoved = m_nw.removeElement(_elem) | m_ne.removeElement(_elem)
						| m_sw.removeElement(_elem) | m_se.removeElement(_elem);

				if (canDecreaseDepth())
				{
					m_elements.addAll(getUniqueElements(null));

					m_nw = m_ne = m_sw = m_se = null;
				}

				return elemRemoved;
			}
		}

		public boolean areChildrenLeafs()
		{
			return m_nw.isLeaf() && m_ne.isLeaf() && m_sw.isLeaf() && m_se.isLeaf();
		}

		public boolean canDecreaseDepth()
		{
			return areChildrenLeafs() && (getUniqueElements(null).size() <= s_threasold);
		}

		private void createChilds()
		{
			m_nw = new QuadTreeNode(new RectangleShape(m_shape.getUpperLeft(), m_shape.getCenter(),
					null));
			m_ne = new QuadTreeNode(
					new RectangleShape(m_shape.getUpper(), m_shape.getRight(), null));
			m_sw = new QuadTreeNode(
					new RectangleShape(m_shape.getLeft(), m_shape.getBottom(), null));
			m_se = new QuadTreeNode(new RectangleShape(m_shape.getCenter(),
					m_shape.getBottomRight(), null));
		}

		public LinkedList<CollisionShape> getUniqueElements(
				LinkedList<CollisionShape> _out)
		{
			if (_out == null)
				_out = new LinkedList<CollisionShape>();

			if (isLeaf())
			{
				for (CollisionShape elem : m_elements)
				{
					if (!_out.contains(elem))
						_out.add(elem);
				}
			} else
			{
				m_nw.getUniqueElements(_out);
				m_ne.getUniqueElements(_out);
				m_sw.getUniqueElements(_out);
				m_se.getUniqueElements(_out);
			}

			return _out;
		}

		public QuadTreeNode getNW()
		{
			return m_nw;
		}

		public QuadTreeNode getNE()
		{
			return m_ne;
		}

		public QuadTreeNode getSW()
		{
			return m_sw;
		}

		public QuadTreeNode getSE()
		{
			return m_se;
		}

		public LinkedList<CollisionShape> getElements()
		{
			// TODO Auto-generated method stub
			return m_elements;
		}
	}

	/** TODO: make the tree handle collision mask and not just position */

	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(QuadTree.class);

	/** Root node of the Quadtree */
	private final QuadTreeNode	m_rootNode;

	/*----------------------------------------------*/
	public QuadTree(final int _width, final int _height)
	{
		m_rootNode = new QuadTreeNode(new RectangleShape(UtilsLemmings.s_tileWidth * _width, UtilsLemmings.s_tileHeight
				* _height, null));
	}

	/*----------------------------------------------*/
	public QuadTree(final RectangleShape _shape)
	{
		m_rootNode = new QuadTreeNode(_shape);
	}

	/*----------------------------------------------*/
	public void add(
			final CollisionShape _shape)
	{
		m_rootNode.addElement(_shape);
	}

	/*----------------------------------------------*/
	public void remove(
			final CollisionShape _shape)
	{
		if(_shape != null)
			m_rootNode.removeElement(_shape);
	}

	/*----------------------------------------------*/
	public void update()
	{

	}

	/*----------------------------------------------*/
	public void updateElement(
			final CollisionShape _elem)
	{
		m_rootNode.removeElement(_elem);
		m_rootNode.addElement(_elem);
	}

	/*----------------------------------------------*/
	public void updateElements(
			final LinkedList<CollisionShape> _elems)
	{
		for (CollisionShape elem : _elems)
			updateElement(elem);
	}

	public QuadTreeNode getRootNode()
	{
		// TODO Auto-generated method stub
		return m_rootNode;
	}
	
	public LinkedList<CollisionShape> getCollidingObjects(CollisionShape _shape, LinkedList<CollisionShape> _out)
	{
		if(_out == null)
			_out = new LinkedList<>();
		getCollidingObjects(_shape, getRootNode(), _out);
		return _out;
	}
	
	private void getCollidingObjects(CollisionShape _shape, QuadTreeNode _n, LinkedList<CollisionShape> _out)
	{
		if(!_n.getShape().collide(_shape))
			return;
		
		if(_n.isLeaf())
		{			
			for(CollisionShape shape : _n.getElements())
			{
				if(_shape.collide(shape))
					_out.add(shape);
			}
		}
		else
		{
			getCollidingObjects(_shape, _n.getNE(), _out);
			getCollidingObjects(_shape, _n.getNW(), _out);
			getCollidingObjects(_shape, _n.getSE(), _out);
			getCollidingObjects(_shape, _n.getSW(), _out);
		}
	}

	public CollidingObjectsSet getCollidingObjects(
			CollidingObjectsSet _out)
	{
		if (_out == null)
			_out = new CollidingObjectsSet();
		getCollidingObjects(m_rootNode, _out);
		return _out;
	}

	public CollidingObjectsSet getCollidingObjects(
			final QuadTreeNode _n,
			CollidingObjectsSet _out)
	{
		if (_out == null)
			_out = new CollidingObjectsSet();

		if (_n.isLeaf())
		{
			CollisionShape[] shapes = _n.getElements().toArray(new CollisionShape[0]);
			short elemCount = (short) shapes.length;
			for (short i = 0; i < elemCount; ++i)
			{
				for (short j = (short) (i + 1); j < elemCount; ++j)
				{
					if (shapes[i].collide(shapes[j]))
					{
						_out.add(new CollidingObjects(shapes[i], shapes[j]));
					}
				}
			}
		} else
		{
			getCollidingObjects(_n.getNW(), _out);
			getCollidingObjects(_n.getNE(), _out);
			getCollidingObjects(_n.getSW(), _out);
			getCollidingObjects(_n.getSE(), _out);
		}

		return _out;
	}
}

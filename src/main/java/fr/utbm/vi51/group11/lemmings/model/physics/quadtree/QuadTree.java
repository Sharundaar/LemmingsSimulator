package fr.utbm.vi51.group11.lemmings.model.physics.quadtree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuadTree
{
	/** TODO: make the tree handle collision mask and not just position */
	
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(QuadTree.class);

	/** Root node of the Quadtree */
	private final Node			m_rootNode;

	/*----------------------------------------------*/
	public QuadTree()
	{
		m_rootNode = new Node();
	}
	
	/*----------------------------------------------*/
	public void update()
	{
		m_rootNode.updateTree();
	}
	
}

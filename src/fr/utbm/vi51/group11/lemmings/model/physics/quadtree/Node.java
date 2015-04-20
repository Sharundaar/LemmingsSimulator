package fr.utbm.vi51.group11.lemmings.model.physics.quadtree;

import java.util.Collection;
import java.util.LinkedList;

import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;

public class Node
{	
	private enum NodeType
	{
		NONE,
		NORTH_EAST,
		NORTH_WEST,
		SOUTH_EAST,
		SOUTH_WEST,
	}
	
	
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(Node.class);

	private final static int ENTITY_THRESHOLD = 10;
	
	/** Possible children the node can have */
	private Node				m_northEast = null;
	private Node				m_northWest = null;
	private Node				m_southWest = null;
	private Node				m_southEast = null;
	
	Vector2f m_center = new Vector2f();
	Vector2f m_size = new Vector2f();

	LinkedList<WorldEntity> m_entities = new LinkedList<WorldEntity>();
	
	/*----------------------------------------------*/
	public Node()
	{
	}
	
	/*----------------------------------------------*/
	public Node(Vector2f _center, Vector2f _size)
	{
		m_center = _center;
		m_size = _size;
	}
	
	/*----------------------------------------------*/
	public void updateTree()
	{
		// two cases
		
		if(!isLeaf()) // node is not leaf
		{
			// first we update all childs
			updateChilds();
			
			// now if childs were not leaf and should be, they are !!
			LinkedList<WorldEntity> entityToRemove = new LinkedList<WorldEntity>();
			if(m_northEast.isLeaf())
			{
				for(WorldEntity ent : m_northEast.getEntities())
				{
					if(computeEntityPlace(ent) != NodeType.NORTH_EAST)
						entityToRemove.add(ent);
				}
			}
			if(m_northWest.isLeaf())
			{
				for(WorldEntity ent : m_northWest.getEntities())
				{
					if(computeEntityPlace(ent) != NodeType.NORTH_WEST)
						entityToRemove.add(ent);
				}
			}
			if(m_southEast.isLeaf())
			{
				for(WorldEntity ent : m_southEast.getEntities())
				{
					if(computeEntityPlace(ent) != NodeType.SOUTH_EAST)
						entityToRemove.add(ent);
				}
			}
			if(m_southWest.isLeaf())
			{
				for(WorldEntity ent : m_southWest.getEntities())
				{
					if(computeEntityPlace(ent) != NodeType.SOUTH_WEST)
						entityToRemove.add(ent);
				}
			}
			
			// effectively removing entities
			for(WorldEntity ent : entityToRemove)
			{
				removeEntity(ent); // should remove the entity at the wrong place
				addEntity(ent); // should put the entity at the right place
			}			
		}
		else // if the node is a leaf... well should be handled by his parent
		{
			
		}
	}
	
	/*----------------------------------------------*/
	public void updateChilds()
	{
		m_northWest.updateTree();
		m_northEast.updateTree();
		m_southWest.updateTree();
		m_southEast.updateTree();
	}
	
	/*----------------------------------------------*/
	public boolean isLeaf()
	{
		return m_northEast == null;
	}
	
	/*----------------------------------------------*/
	public NodeType computeEntityPlace(WorldEntity _entity)
	{
		int x = _entity.getCollisionMask().getCoordinates().x();
		int y = _entity.getCollisionMask().getCoordinates().y();
		if(x < m_center.getX() && y < m_center.getY()) // South-West
		{
			return NodeType.SOUTH_WEST;
		} 
		if(x > m_center.getX() && y < m_center.getY()) // South-East
		{
			return NodeType.SOUTH_EAST;
		} 
		if(x >= m_center.getX() && y >= m_center.getY()) // North-East
		{
			return NodeType.NORTH_EAST;
		} 
		if(x < m_center.getX() && y >= m_center.getY()) // North-West
		{
			return NodeType.NORTH_WEST;
		}
		
		// obviously a bug
		return NodeType.NONE;
	}
	
	/*----------------------------------------------*/
	public void addEntity(final WorldEntity _entity)
	{		
		if(m_northEast != null) // if not leaf gives object to child
		{
			int x = _entity.getCollisionMask().getCoordinates().x();
			int y = _entity.getCollisionMask().getCoordinates().y();
			if(x < m_center.getX() && y < m_center.getY()) // South-West
			{
				m_southWest.addEntity(_entity);
			} 
			else if(x > m_center.getX() && y < m_center.getY()) // South-East
			{
				m_southEast.addEntity(_entity);
			} 
			else if(x > m_center.getX() && y > m_center.getY()) // North-East
			{
				m_northEast.addEntity(_entity);
			} 
			else if(x < m_center.getX() && y > m_center.getY()) // North-West
			{
				m_northWest.addEntity(_entity);
			}	
		}
		else
		{
			// add entity to list
			m_entities.add(_entity);
			
			// Checking the threshold
			if(m_entities.size() > ENTITY_THRESHOLD)
			{
				// first lets cut the space
				m_northWest = new Node(new Vector2f(m_center.getX() - m_size.getX() / 2, m_center.getY() - m_size.getY() / 2), new Vector2f(m_size.getX() / 2, m_size.getY() / 2));
				m_northEast = new Node(new Vector2f(m_center.getX() + m_size.getX() / 2, m_center.getY() - m_size.getY() / 2), new Vector2f(m_size.getX() / 2, m_size.getY() / 2));
				m_southWest = new Node(new Vector2f(m_center.getX() - m_size.getX() / 2, m_center.getY() + m_size.getY() / 2), new Vector2f(m_size.getX() / 2, m_size.getY() / 2));
				m_southEast = new Node(new Vector2f(m_center.getX() + m_size.getX() / 2, m_center.getY() + m_size.getY() / 2), new Vector2f(m_size.getX() / 2, m_size.getY() / 2));
				
				// some recursivity here
				for(WorldEntity ent : m_entities)
					addEntity(ent);
				
				// then we clear the list
				m_entities.clear();
				
			} // If not everything is fine
		}
	}
	
	/*----------------------------------------------*/
	public void removeEntity(final WorldEntity _entity)
	{
		// lets find where the entity is ! code is copied from the add func, maybe we could refactor something here
		int x = _entity.getCollisionMask().getCoordinates().x();
		int y = _entity.getCollisionMask().getCoordinates().y();
		if(x < m_center.getX() && y < m_center.getY()) // South-West
		{
			m_southWest.removeEntity(_entity);
		} 
		else if(x > m_center.getX() && y < m_center.getY()) // South-East
		{
			m_southEast.removeEntity(_entity);
		} 
		else if(x > m_center.getX() && y > m_center.getY()) // North-East
		{
			m_northEast.removeEntity(_entity);
		} 
		else if(x < m_center.getX() && y > m_center.getY()) // North-West
		{
			m_northWest.removeEntity(_entity);
		}
		
		// now we should check if we can remove a level of the tree
		int entityCount = 0;
		entityCount += m_southWest.getEntityCount();
		entityCount += m_southEast.getEntityCount();
		entityCount += m_northWest.getEntityCount();
		entityCount += m_northEast.getEntityCount();
		
		if(entityCount <= ENTITY_THRESHOLD) // checking if the threshold is bumped
		{
			// let's cut the branch !
			m_entities.addAll(m_southWest.getEntities());
			m_entities.addAll(m_southEast.getEntities());
			m_entities.addAll(m_northWest.getEntities());
			m_entities.addAll(m_northEast.getEntities());
			
			clearBranch();
		}
	}
	
	/*----------------------------------------------*/
	private void clearBranch()
	{
		m_southWest.clear();
		m_southEast.clear();
		m_northWest.clear();
		m_northEast.clear();
		
		m_southWest = null;
		m_southEast = null;
		m_northWest = null;
		m_northEast = null;
		
	}
	
	/*----------------------------------------------*/
	private void clear()
	{
		m_entities.clear();
	}
	
	/*----------------------------------------------*/
	private Collection<WorldEntity> getEntities()
	{
		return m_entities;
	}
	
	/*----------------------------------------------*/
	private int getEntityCount()
	{
		return m_entities.size();
	}
	
	
}

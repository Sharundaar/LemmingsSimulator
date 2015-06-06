package fr.utbm.vi51.group11.lemmings.model.physics.properties;

import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape;

public class CollisionProperty {

	private boolean m_crossable;
	private boolean m_dangerous;
	private boolean m_activable;
	
	private CollisionShape m_killZone;
	private CollisionShape m_activationZone;
	
	private WorldEntity m_worldEntity;
	
	public CollisionProperty()
	{
		m_crossable = true;
		m_dangerous = false;
		m_activable = false;
		
		m_killZone = null;
		m_activationZone = null;
		
		m_worldEntity = null;
	}
	
	public void setCrossable(boolean _crossable)
	{
		m_crossable = _crossable;
	}
	
	public void setDangerous(boolean _dangerous, CollisionShape _killZone)
	{
		m_dangerous = _dangerous;
		m_killZone = _killZone;
	}
	
	public void setActivable(boolean _activable, CollisionShape _activationZone)
	{
		m_activable = _activable;
		m_activationZone = _activationZone;
	}
	
	public void setEntity(WorldEntity _ent)
	{
		m_worldEntity = _ent;
	}
	
	public boolean isCrossable()
	{
		return m_crossable;
	}
	
	public boolean isDangerous()
	{
		return m_dangerous;
	}
	
	public boolean isActiable()
	{
		return m_activable;
	}
	
	public CollisionShape getKillZone()
	{
		return m_killZone;
	}
	
	public CollisionShape getActivationZone()
	{
		return m_activationZone;
	}
	
	public WorldEntity getEntity()
	{
		return m_worldEntity;
	}
}

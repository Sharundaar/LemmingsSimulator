package fr.utbm.vi51.group11.lemmings.model.physics.collidingobjects;

import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape;

public class CollidingObjects {
	public CollisionShape m_s1;
	public CollisionShape m_s2;
	
	public CollidingObjects(CollisionShape _s1, CollisionShape _s2)
	{
		m_s1 = _s1;
		m_s2 = _s2;
	}
	
	public boolean equals(CollidingObjects _co)
	{
		return (_co.m_s1 == m_s1 && _co.m_s2 == m_s2) || (_co.m_s1 == m_s2 && _co.m_s2 == m_s1);
	}
}

package fr.utbm.vi51.group11.lemmings.model.entity.mobile.body;

import fr.utbm.vi51.group11.lemmings.utils.enums.DigDirection;

public class BodyStateProperty
{
	public float		m_fallHeight;
	public boolean		m_chuteOpen;
	public long			m_timeOfDeath;

	public long			m_diggingStart;
	public DigDirection	m_diggingDirection;

	public BodyStateProperty()
	{
		m_fallHeight = 0;
		m_chuteOpen = false;
		m_timeOfDeath = 0;
		m_diggingStart = 0;
		m_diggingDirection = DigDirection.NONE;
	}

	public BodyStateProperty newInstance()
	{
		BodyStateProperty p = new BodyStateProperty();

		p.m_chuteOpen = m_chuteOpen;
		p.m_diggingDirection = m_diggingDirection;
		p.m_diggingStart = m_diggingStart;
		p.m_fallHeight = m_fallHeight;
		p.m_timeOfDeath = m_timeOfDeath;

		return p;
	}
}

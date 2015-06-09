package fr.utbm.vi51.group11.lemmings.model.entity.mobile.body;

public class BodyStateProperty
{
	public float	m_fallHeight;
	public boolean	m_chuteOpen;
	public long		m_timeOfDeath;

	public BodyStateProperty()
	{
		m_fallHeight = 0;
		m_chuteOpen = false;
		m_timeOfDeath = 0;
	}

	public BodyStateProperty newInstance()
	{
		BodyStateProperty prop = new BodyStateProperty();
		prop.m_fallHeight = m_fallHeight;
		prop.m_chuteOpen = m_chuteOpen;
		prop.m_timeOfDeath = m_timeOfDeath;

		return prop;
	}

	public boolean equals(
			final BodyStateProperty _prop)
	{
		return ((m_fallHeight == _prop.m_fallHeight) && (m_chuteOpen == _prop.m_chuteOpen) && (m_timeOfDeath == _prop.m_timeOfDeath));
	}
}

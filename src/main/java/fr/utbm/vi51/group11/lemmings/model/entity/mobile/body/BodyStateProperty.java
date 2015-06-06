package fr.utbm.vi51.group11.lemmings.model.entity.mobile.body;

public class BodyStateProperty {	
	public float m_fallHeight;
	public boolean m_chuteOpen;
	public long m_timeOfDeath;
	
	public BodyStateProperty()
	{
		m_fallHeight = 0;
		m_chuteOpen = false;
		m_timeOfDeath = 0;
	}
}

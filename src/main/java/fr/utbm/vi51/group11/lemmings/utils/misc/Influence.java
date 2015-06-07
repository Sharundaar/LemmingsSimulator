package fr.utbm.vi51.group11.lemmings.utils.misc;

import org.arakhne.afc.math.continous.object2d.Vector2f;

import fr.utbm.vi51.group11.lemmings.utils.enums.InfluenceType;

public class Influence
{
	private final InfluenceType	m_type;

	private final Vector2f		m_speed;

	private final Vector2f		m_acceleration;

	private final Action		m_action;

	public Influence()
	{
		m_type = InfluenceType.NONE;
		m_speed = null;
		m_acceleration = null;
		m_action = null;
	}

	public Influence(final InfluenceType _type, final Vector2f _speedOrAcceleration)
	{
		m_type = _type;
		m_action = null;

		if (m_type.equals(InfluenceType.SPEED))
		{
			m_speed = _speedOrAcceleration;
			m_acceleration = null;
		} else if (m_type.equals(InfluenceType.ACCELERATION))
		{
			m_speed = null;
			m_acceleration = _speedOrAcceleration;
		} else
		{
			m_speed = null;
			m_acceleration = null;
		}
	}

	public Influence(final InfluenceType _type, final Action _action)
	{
		m_type = _type;
		m_speed = null;
		m_acceleration = null;
		m_action = _action;
	}

	public InfluenceType getType()
	{
		return m_type;
	}

	public Vector2f getSpeed()
	{
		return m_speed;
	}

	public Vector2f getAcceleration()
	{
		return m_acceleration;
	}

	public Action getAction()
	{
		return m_action;
	}
}

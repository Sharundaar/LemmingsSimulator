package fr.utbm.vi51.group11.lemmings.utils.misc;

import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.utils.enums.ActionType;

public class Action
{
	private final ActionType	m_type;

	private final WorldEntity	m_target;

	public Action(final ActionType _type, final WorldEntity _target)
	{
		m_type = _type;
		m_target = _target;
	}
	
	public ActionType getType()
	{
		return m_type;
	}
	
	public WorldEntity getTarget()
	{
		return m_target;
	}
}

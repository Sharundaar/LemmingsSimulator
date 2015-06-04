package fr.utbm.vi51.group11.lemmings.utils.enums;

import org.apache.commons.lang3.StringUtils;

public enum WorldEntityEnum
{
	NONE(false),
	LEVEL_START(false),
	LEVEL_END(false),
	LEMMING_BODY(true),
	MAP(false);

	public boolean	m_animated;

	private WorldEntityEnum(final boolean _animated)
	{
		m_animated = _animated;
	}

	public boolean equals(
			final String _id)
	{
		return (StringUtils.upperCase(this.toString()).equals(StringUtils.upperCase(_id)));
	}
}
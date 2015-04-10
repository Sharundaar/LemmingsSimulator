package fr.utbm.vi51.group11.lemmings.utils.enums;

import org.apache.commons.lang3.StringUtils;

public enum WorldEntityEnum
{
	NONE,
	LEVEL_START,
	LEVEL_END,
	LEMMING_BODY,
	MAP;

	public boolean equals(
			final String _id)
	{
		return (StringUtils.upperCase(this.toString()).equals(StringUtils.upperCase(_id)));
	}
}
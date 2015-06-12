package fr.utbm.vi51.group11.lemmings.model.entity.mobile.body;

public enum BodyState
{
	NORMAL,
	CLIMBING,
	FALLING,
	DIGGING,
	DEAD;

	public BodyState newInstance()
	{
		switch (this)
		{
			case NORMAL:
				return NORMAL;
			case CLIMBING:
				return CLIMBING;
			case DEAD:
				return DEAD;
			case DIGGING:
				return DIGGING;
			case FALLING:
				return FALLING;
			default:
				return null;
		}
	}
}
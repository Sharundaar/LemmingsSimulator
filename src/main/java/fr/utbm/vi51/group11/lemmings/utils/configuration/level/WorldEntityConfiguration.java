package fr.utbm.vi51.group11.lemmings.utils.configuration.level;

import org.arakhne.afc.math.continous.object2d.Point2f;

public class WorldEntityConfiguration
{
	private final Point2f	m_worldCoords;

	private final String	m_textureID;

	public WorldEntityConfiguration(final Point2f _levelCoords, final String _textureID)
	{
		m_worldCoords = _levelCoords;
		m_textureID = _textureID;
	}

	public Point2f getWorldCoords()
	{
		return m_worldCoords;
	}

	public String getTextureID()
	{
		return m_textureID;
	}

	@Override
	public String toString()
	{
		String disp = "";

		disp += "TextureID='" + m_textureID + "', WorldCoords=(" + m_worldCoords.x() + ","
				+ m_worldCoords.y() + ")";

		return disp;
	}
}

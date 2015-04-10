package fr.utbm.vi51.group11.lemmings.utils.configuration;

import org.arakhne.afc.math.continous.object2d.Point2f;

public class WorldEntityConfiguration
{
	private final Point2f	m_levelCoords;

	private final String	m_textureID;

	public WorldEntityConfiguration(final Point2f _levelCoords, final String _textureID)
	{
		m_levelCoords = _levelCoords;
		m_textureID = _textureID;
	}

	public Point2f getLevelCoords()
	{
		return m_levelCoords;
	}

	public String getTextureID()
	{
		return m_textureID;
	}

	@Override
	public String toString()
	{
		String disp = "";

		disp += "TextureID='" + m_textureID + "', LevelCoords=(" + m_levelCoords.x() + ","
				+ m_levelCoords.y() + ")";

		return disp;
	}
}

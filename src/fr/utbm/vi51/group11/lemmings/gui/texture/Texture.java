package fr.utbm.vi51.group11.lemmings.gui.texture;

import java.awt.image.BufferedImage;

public class Texture
{
	private final String		m_id;

	private final BufferedImage	m_image;

	public Texture(final String _id, final BufferedImage _image)
	{
		m_id = _id;
		m_image = _image;
	}

	public BufferedImage getImage()
	{
		return m_image;
	}
}

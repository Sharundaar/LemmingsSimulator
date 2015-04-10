package fr.utbm.vi51.group11.lemmings.gui.texture;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.discrete.object2d.Point2i;
import org.arakhne.afc.math.discrete.object2d.Rectangle2i;

import fr.utbm.vi51.group11.lemmings.utils.interfaces.ITextureHandler;

public class Sprite implements ITextureHandler
{
	private Texture				m_texture;

	private final Rectangle2i	m_spriteRect;

	private Point2f				m_worldCoords;

	public Sprite(final float _worldX, final float _worldY, final int _textureX,
			final int _textureY, final int _width, final int _height, final String _textureID)
	{
		m_spriteRect = new Rectangle2i(_textureX, _textureY, _width, _height);
		m_worldCoords = new Point2f(_worldX, _worldY); // TODO
		TextureBank.getInstance().getTexture(_textureID, this);
	}

	public Rectangle2i getSpriteRect()
	{
		return m_spriteRect;
	}

	public Point2i getTopLeft()
	{
		return new Point2i(m_spriteRect.getMinX(), m_spriteRect.getMinY());
	}

	public Point2i getTopRight()
	{
		return new Point2i(m_spriteRect.getMaxX(), m_spriteRect.getMinY());
	}

	public Point2i getBottomLeft()
	{
		return new Point2i(m_spriteRect.getMinX(), m_spriteRect.getMaxY());
	}

	public Point2i getBottomRight()
	{
		return new Point2i(m_spriteRect.getMaxX(), m_spriteRect.getMaxY());
	}

	public Point2i getCenter()
	{
		return new Point2i(m_spriteRect.getCenterX(), m_spriteRect.getCenterY());
	}

	public int getWidth()
	{
		return m_spriteRect.getWidth();
	}

	public int getHeight()
	{
		return m_spriteRect.getHeight();
	}

	public Point2f getWorldCoords()
	{
		return m_worldCoords;
	}

	public void setWorldCoords(
			final Point2f _coords)
	{
		m_worldCoords = _coords;
	}

	public Texture getTexture()
	{
		return m_texture;
	}

	@Override
	public void receiveTexture(
			final Texture _texture)
	{
		m_texture = _texture;
	}
}

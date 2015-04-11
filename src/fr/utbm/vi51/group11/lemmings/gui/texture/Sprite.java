package fr.utbm.vi51.group11.lemmings.gui.texture;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.discrete.object2d.Point2i;
import org.arakhne.afc.math.discrete.object2d.Rectangle2i;

import fr.utbm.vi51.group11.lemmings.utils.interfaces.ITextureHandler;

public class Sprite implements ITextureHandler
{
	private Texture				m_texture;

	private final Rectangle2i	m_spriteRect;

	private Rectangle2f			m_worldRect;

	public Sprite(final float _worldX, final float _worldY, final float _worldWidth, final float _worldHeight, 
			final int _textureX, final int _textureY, final int _textureWidth, final int _textureHeight, 
			final String _textureID)
	{
		m_spriteRect = new Rectangle2i(_textureX, _textureY, _textureWidth, _textureHeight);
		m_worldRect = new Rectangle2f(_worldX, _worldY, _worldWidth, _worldHeight); // TODO
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
		return new Point2f(m_worldRect.getMinX(), m_worldRect.getMinY());
	}

	public void setWorldCoords(
			final Point2f _coords)
	{
		m_worldRect.set(_coords.x(), _coords.y(), m_worldRect.getWidth(), m_worldRect.getHeight());
	}
	
	public Rectangle2f getWorldRect()
	{
		return m_worldRect;
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

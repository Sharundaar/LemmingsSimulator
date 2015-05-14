package fr.utbm.vi51.group11.lemmings.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.discrete.object2d.Rectangle2i;

import fr.utbm.vi51.group11.lemmings.gui.texture.Sprite;
import fr.utbm.vi51.group11.lemmings.model.Environment;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;

public class GraphicsEngine extends JPanel
{

	/**
	 * Generated serial ID
	 */
	private static final long		serialVersionUID	= 1756239984776600738L;

	private final Environment		m_environnement;

	private final AffineTransform	m_affineTransform;

	public GraphicsEngine(final Environment _environnement)
	{
		super();
		m_environnement = _environnement;
		m_affineTransform = new AffineTransform();
	}

	@Override
	public void paint(
			final Graphics _g)
	{

		_g.clearRect(0, 0, getWidth(), getHeight());

		drawMap(_g);

		drawEntities(_g);
	}

	private void drawMap(
			final Graphics _g)
	{
		m_environnement.getMap().redrawMap();
		drawSprite(m_environnement.getMap().getSprite(), _g);

	}

	private void drawEntities(
			final Graphics _g)
	{
		for (WorldEntity e : m_environnement.m_worldEntities)
			drawSprite(e.getSprite(), _g);
	}

	public void drawSprite(
			final Sprite _sprite,
			final Graphics _g)
	{
		Graphics2D g2d = (Graphics2D) _g;
		m_affineTransform.setToIdentity();

		if (_sprite.getTexture() != null) // Image has been correctly set
		{
			Rectangle2i blitRect = _sprite.getSpriteRect();
			Rectangle2f drawRect = _sprite.getWorldRect();

			Image img = _sprite
					.getTexture()
					.getImage()
					.getSubimage(blitRect.getMinX(), blitRect.getMinY(), blitRect.getWidth(),
							blitRect.getHeight());

			double sx = drawRect.getWidth() / blitRect.getWidth();
			double sy = drawRect.getHeight() / blitRect.getHeight();

			m_affineTransform.scale(sx, sy);
			m_affineTransform.translate(drawRect.getMinX(), drawRect.getMinY());

			g2d.drawImage(img, m_affineTransform, null);
		} else
		// Let's draw a simple placeholder for now
		{
			Rectangle2f drawRect = _sprite.getWorldRect();

			g2d.setColor(Color.CYAN);
			g2d.fillRect((int) drawRect.getMinX(), (int) drawRect.getMinY(),
					(int) drawRect.getWidth(), (int) drawRect.getHeight());
			g2d.setColor(Color.WHITE);
		}
	}
}

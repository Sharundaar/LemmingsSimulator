package fr.utbm.vi51.group11.lemmings.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import fr.utbm.vi51.group11.lemmings.gui.texture.Sprite;
import fr.utbm.vi51.group11.lemmings.model.Environment;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;

public class GraphicsEngine extends JPanel
{

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

		_g.clearRect(0, 0, this.WIDTH, this.HEIGHT);

		drawMap(_g);

		drawEntities(_g);
	}

	private void drawMap(
			final Graphics _g)
	{
		// int[][] tileGrid =
		// m_environnement.getCurrentLevelProperties().getTileGrid();
		// Texture texture;
		// Sprite sprite;
		// AffineTransform affineTransform = new AffineTransform();
		//
		// for (int i = 0; i < tileGrid.length; ++i)
		// for (int j = 0; j < tileGrid[0].length; ++j)
		// {
		// texture =
		// TextureBank.getInstance().getTexture(CellType.valueOf(tileGrid[i][j]));
		// sprite = texture.getSprite(0);
		// Image image =
		// texture.getImage().getSubimage(sprite.getTextureRect().getMinX(),
		// sprite.getTextureRect().getMinY(),
		// sprite.getTextureRect().getWidth(),
		// sprite.getTextureRect().getHeight());
		//
		// affineTransform.translate(sprite.getWorldCoords().getX(),
		// sprite.getWorldCoords()
		// .getY());
		// m_g2d.drawImage(image, affineTransform, null);
		//
		// affineTransform.translate(-sprite.getWorldCoords().getX(),
		// -sprite.getWorldCoords()
		// .getY());
		// }

	}

	private void drawEntities(
			final Graphics _g)
	{
		for (WorldEntity e : m_environnement.list)
			drawSprite(e.getSprite(), _g);
	}

	public void drawSprite(
			final Sprite _sprite,
			final Graphics _g)
	{
		Graphics2D g2d = (Graphics2D) _g;
		m_affineTransform.setToIdentity();

		Image image = _sprite
				.getTexture()
				.getImage()
				.getSubimage(_sprite.getTopLeft().x(), _sprite.getTopLeft().y(),
						_sprite.getWidth(), _sprite.getHeight());
		m_affineTransform.translate(_sprite.getWorldCoords().x(), _sprite.getWorldCoords().y());
		g2d.drawImage(image, m_affineTransform, null);
	}
}

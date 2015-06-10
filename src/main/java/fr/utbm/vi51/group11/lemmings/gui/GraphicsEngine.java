package fr.utbm.vi51.group11.lemmings.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.EnumSet;

import javax.swing.JPanel;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.discrete.object2d.Rectangle2i;

import fr.utbm.vi51.group11.lemmings.gui.texture.Sprite;
import fr.utbm.vi51.group11.lemmings.model.Environment;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.physics.collidingobjects.CollidingObjects;
import fr.utbm.vi51.group11.lemmings.model.physics.quadtree.QuadTree;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CircleShape;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionShape;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.RectangleShape;

public class GraphicsEngine extends JPanel
{

	enum DebugOption
	{
		SHOW_QUAD_TREE,
		SHOW_COLLISION_BOX;

		public static final EnumSet<DebugOption>	ALL	= EnumSet.allOf(DebugOption.class);
	}

	/**
	 * Generated serial ID
	 */
	private static final long			serialVersionUID	= 1756239984776600738L;

	private final Environment			m_environnement;

	private final AffineTransform		m_affineTransform;

	private final EnumSet<DebugOption>	m_debugOptions;

	public GraphicsEngine(final Environment _environnement, final int _width, final int _height)
	{
		super();

		setPreferredSize(new Dimension(_width, _height));

		m_environnement = _environnement;
		m_affineTransform = new AffineTransform();

		m_debugOptions = EnumSet.noneOf(DebugOption.class);
	}

	@Override
	public void paint(
			final Graphics _g)
	{

		_g.clearRect(0, 0, getWidth(), getHeight());

		drawMap(_g);

		drawEntities(_g);

		if (m_debugOptions.contains(DebugOption.SHOW_QUAD_TREE))
			drawQuadTree(_g);
		if (m_debugOptions.contains(DebugOption.SHOW_COLLISION_BOX))
			drawCollisionBoxes(_g);
	}

	private void drawQuadTree(
			final Graphics _g)
	{
		QuadTree quad = m_environnement.getPhysicEngine().getQuadTree();
		_g.setColor(Color.red);
		drawQuadTreeNode(_g, quad.getRootNode());
	}

	private void drawQuadTreeNode(
			final Graphics _g,
			final QuadTree.QuadTreeNode _node)
	{
		if (_node == null)
			return;

		_g.drawRect((int) (_node.getShape().getRectangle().getMinX()), (int) (_node.getShape()
				.getRectangle().getMinY()), (int) (_node.getShape().getRectangle().getWidth()),
				(int) (_node.getShape().getRectangle().getHeight()));

		if (!_node.isLeaf())
		{
			drawQuadTreeNode(_g, _node.getNW());
			drawQuadTreeNode(_g, _node.getNE());
			drawQuadTreeNode(_g, _node.getSW());
			drawQuadTreeNode(_g, _node.getSE());
		}
	}

	private void drawCollisionShape(
			final Graphics _g,
			final CollisionShape _shape)
	{
		if (_shape == null)
			return;

		switch (_shape.getType())
		{
			case CIRCLE:
				CircleShape circle = (CircleShape) _shape;
				_g.drawArc(circle.getCoordinates(true).x(), circle.getCoordinates(true).y(),
						(int) circle.getRadius(), (int) circle.getRadius(), 0, (int) (2 * Math.PI));
				break;
			case MASK:
				for (CollisionShape s : _shape.getChilds())
				{
					drawCollisionShape(_g, s, true);
				}
				break;
			case RECTANGLE:
				RectangleShape rectangle = (RectangleShape) _shape;
				_g.drawRect(rectangle.getCoordinates(true).x(), rectangle.getCoordinates(true).y(),
						(int) rectangle.getWidth(), (int) rectangle.getHeight());
				break;
			default:
				break;
		}
	}

	private void drawCollisionShape(
			final Graphics _g,
			final CollisionShape _shape,
			final boolean _drawChilds)
	{
		drawCollisionShape(_g, _shape);

		if (_drawChilds)
		{
			for (CollisionShape s : _shape.getChilds())
			{
				drawCollisionShape(_g, s, true);
			}
		}
	}

	private void drawCollisionBoxes(
			final Graphics _g)
	{
		_g.setColor(Color.cyan);
		for (WorldEntity entity : m_environnement.m_worldEntities)
		{
			drawCollisionShape(_g, entity.getCollisionMask());
		}

		for (CollisionShape shape : m_environnement.getMap().getCollisionMask().getChilds())
		{
			drawCollisionShape(_g, shape);
		}

		_g.setColor(Color.red);
		for (CollidingObjects co : m_environnement.getPhysicEngine().getQuadTree()
				.getCollidingObjects(null))
		{
			RectangleShape rs1, rs2;

			drawCollisionShape(_g, co.m_s1);
			drawCollisionShape(_g, co.m_s2);
		}
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
		{
			drawSprite(e.getSprite(), _g);
		}
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

			double sx = drawRect.getWidth() / blitRect.getWidth();
			double sy = drawRect.getHeight() / blitRect.getHeight();

			m_affineTransform.translate(drawRect.getMinX(), drawRect.getMinY());
			m_affineTransform.scale(sx, sy);

			Image img = _sprite
					.getTexture()
					.getImage()
					.getSubimage(blitRect.getMinX(), blitRect.getMinY(), blitRect.getWidth(),
							blitRect.getHeight());

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

	public void enableDebugOption(
			final DebugOption _option,
			final boolean _enable)
	{
		if (_enable)
			m_debugOptions.add(_option);
		else
			m_debugOptions.remove(_option);
	}

	public boolean isDebugOptionEnabled(
			final DebugOption _option)
	{
		return m_debugOptions.contains(_option);
	}
}

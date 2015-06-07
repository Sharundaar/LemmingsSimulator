package fr.utbm.vi51.group11.lemmings.gui;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.utbm.vi51.group11.lemmings.controller.KeyboardController;
import fr.utbm.vi51.group11.lemmings.gui.GraphicsEngine.DebugOption;
import fr.utbm.vi51.group11.lemmings.model.Environment;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class MainFrame extends GUI
{
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	private final GraphicsEngine	m_graphicsEngine;

	private JMenuBar				m_menuBar;

	public MainFrame(final Environment _environnement, final int _rowNb, final int _colNb)
	{
		super();

		createMenuBar();
		this.setJMenuBar(m_menuBar);

		int width = (UtilsLemmings.s_tileWidth * _colNb) + (getInsets().bottom + getInsets().top);
		int height = (UtilsLemmings.s_tileHeight * _rowNb) + (getInsets().right + getInsets().left)
				+ (m_menuBar.getHeight());

		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		m_graphicsEngine = new GraphicsEngine(_environnement, width, height);

		KeyboardController.getInstance().updateJPanelKeyboardMaps(m_graphicsEngine);

		m_graphicsEngine.setFocusable(true);

		setContentPane(m_graphicsEngine);
		setLocationRelativeTo(null);

		pack();

		setVisible(true);
	}

	public void createMenuBar()
	{
		m_menuBar = new JMenuBar();

		JMenu debugMenu = new JMenu("Debug Options");
		final JCheckBoxMenuItem showQuadTree = new JCheckBoxMenuItem("Show QuadTree");
		showQuadTree.addChangeListener(new ChangeListener()
		{

			@Override
			public void stateChanged(
					final ChangeEvent arg0)
			{
				// TODO Auto-generated method stub
				m_graphicsEngine.enableDebugOption(DebugOption.SHOW_QUAD_TREE,
						showQuadTree.getState());
			}

		});
		final JCheckBoxMenuItem showCollisionBox = new JCheckBoxMenuItem("Show Collision Boxes");
		showCollisionBox.addChangeListener(new ChangeListener()
		{

			@Override
			public void stateChanged(
					final ChangeEvent arg0)
			{
				// TODO Auto-generated method stub
				m_graphicsEngine.enableDebugOption(DebugOption.SHOW_COLLISION_BOX,
						showCollisionBox.getState());
			}

		});

		debugMenu.add(showCollisionBox);
		debugMenu.add(showQuadTree);

		m_menuBar.add(debugMenu);
	}

	public GraphicsEngine getGraphicsEngine()
	{
		return m_graphicsEngine;
	}
}

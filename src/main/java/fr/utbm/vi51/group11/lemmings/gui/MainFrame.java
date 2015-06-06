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

public class MainFrame extends GUI
{
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	private final GraphicsEngine	m_graphicsEngine;
	
	private JMenuBar m_menuBar;

	public MainFrame(final Environment _environnement)
	{
		super();
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		m_graphicsEngine = new GraphicsEngine(_environnement);

		KeyboardController.getInstance().updateJPanelKeyboardMaps(m_graphicsEngine);

		m_graphicsEngine.setFocusable(true);

		setContentPane(m_graphicsEngine);
		setLocationRelativeTo(null);
		
		createMenuBar();
		this.setJMenuBar(m_menuBar);
		
		setVisible(true);
	}
	
	public void createMenuBar()
	{
		m_menuBar = new JMenuBar();
		
		JMenu debugMenu = new JMenu("Debug Options");
		final JCheckBoxMenuItem showQuadTree = new JCheckBoxMenuItem("Show QuadTree");
		showQuadTree.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				m_graphicsEngine.enableDebugOption(DebugOption.SHOW_QUAD_TREE, showQuadTree.getState());
			}
			
		
		});
		final JCheckBoxMenuItem showCollisionBox = new JCheckBoxMenuItem("Show Collision Boxes");
		showCollisionBox.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				m_graphicsEngine.enableDebugOption(DebugOption.SHOW_COLLISION_BOX, showCollisionBox.getState());
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

package fr.utbm.vi51.group11.lemmings.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.utbm.vi51.group11.lemmings.controller.KeyboardController;
import fr.utbm.vi51.group11.lemmings.gui.GraphicsEngine.DebugOption;
import fr.utbm.vi51.group11.lemmings.model.Simulation;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.LevelPropertiesMap;

public class MainFrame extends GUI implements ActionListener, WindowListener
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private Observer			m_applicationObserver;

	private JMenuBar			m_menuBar;

	private JPanel				m_PSpeedButtonPanel;
	private JButton				m_BHalfSpeed;
	private JButton				m_BNormalSpeed;
	private JButton				m_BDoubleSpeed;

	private final BorderLayout	m_mainLayout;

	public MainFrame(final String _title)
	{
		super(_title);

		createMenuBar();
		this.setJMenuBar(m_menuBar);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(this);

		m_mainLayout = new BorderLayout();
		setLayout(m_mainLayout);

		createSpeedButton();

		this.getContentPane().add(m_PSpeedButtonPanel, BorderLayout.SOUTH);
	}

	public void initialize(
			final Simulation _simulation,
			final int _width,
			final int _height)
	{
		int width = 0;
		int height = 0;
		if (!isVisible())
		{
			width += getInsets().bottom + getInsets().top;
			height += getInsets().right + getInsets().left + m_menuBar.getHeight();
		}
		width += _width;
		height += _height;
		setSize(width, height);

		m_simulator = _simulation;
		m_graphicsEngine = new GraphicsEngine(m_simulator.getEnvironment(), width, height);
		KeyboardController.getInstance().updateJPanelKeyboardMaps(m_graphicsEngine);
		m_graphicsEngine.setFocusable(true);

		this.getContentPane().add(m_graphicsEngine, BorderLayout.CENTER);

		System.out.println(getContentPane().getComponentCount());

		if (!isVisible())
			setLocationRelativeTo(null);
		else
			getContentPane().repaint();

		pack();
		setVisible(true);
	}

	public void createSpeedButton()
	{
		m_PSpeedButtonPanel = new JPanel();

		m_BHalfSpeed = new JButton("* 0.5");
		m_BNormalSpeed = new JButton("* 1.0");
		m_BDoubleSpeed = new JButton("* 2.0");

		m_BHalfSpeed.addActionListener(this);
		m_BNormalSpeed.addActionListener(this);
		m_BDoubleSpeed.addActionListener(this);

		m_PSpeedButtonPanel.add(m_BHalfSpeed);
		m_PSpeedButtonPanel.add(m_BNormalSpeed);
		m_PSpeedButtonPanel.add(m_BDoubleSpeed);
		((FlowLayout) m_PSpeedButtonPanel.getLayout()).setAlignment(FlowLayout.LEADING);
	}

	public void createMenuBar()
	{
		m_menuBar = new JMenuBar();

		createDebugMenu();

		JMenu space = new JMenu();
		space.setEnabled(false);
		m_menuBar.add(space);

		createLoadLevelMenu();
	}

	private void createDebugMenu()
	{
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

	private void createLoadLevelMenu()
	{
		JMenu levelMenu = new JMenu("Load new Level");
		JMenuItem item;
		for (String levelId : LevelPropertiesMap.getInstance().keySet())
		{
			item = new LoaderMenuItem(levelId);
			levelMenu.add(item);
		}

		m_menuBar.add(levelMenu);
	}

	public GraphicsEngine getGraphicsEngine()
	{
		return m_graphicsEngine;
	}

	public void clearFrame()
	{
		getContentPane().remove(m_graphicsEngine);
		m_graphicsEngine.destroy();
		m_simulator = null;
	}

	@Override
	public void actionPerformed(
			final ActionEvent arg0)
	{
		// TODO Auto-generated method stub
		if (arg0.getSource() == m_BHalfSpeed)
		{
			m_simulator.setSpeedMultiplicator(0.5f);
		}
		if (arg0.getSource() == m_BNormalSpeed)
		{
			m_simulator.setSpeedMultiplicator(1.0f);
		}
		if (arg0.getSource() == m_BDoubleSpeed)
		{
			m_simulator.setSpeedMultiplicator(2.0f);
		}

		m_graphicsEngine.requestFocus();
	}

	public void setApplicationObserver(
			final Observer _appObserver)
	{
		m_applicationObserver = _appObserver;
		for (int i = 0; i < m_menuBar.getMenuCount(); ++i)
			for (int j = 0; j < m_menuBar.getMenu(i).getItemCount(); ++j)
				if (m_menuBar.getMenu(i).getItem(j) instanceof LoaderMenuItem)
				{
					((LoaderMenuItem) m_menuBar.getMenu(i).getItem(j))
							.setObserver(m_applicationObserver);
				}
	}

	@Override
	public void windowOpened(
			final WindowEvent e)
	{
	}

	@Override
	public void windowClosing(
			final WindowEvent e)
	{
		m_applicationObserver.update(new Observable(), null);
	}

	@Override
	public void windowClosed(
			final WindowEvent e)
	{
	}

	@Override
	public void windowIconified(
			final WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(
			final WindowEvent e)
	{
	}

	@Override
	public void windowActivated(
			final WindowEvent e)
	{
	}

	@Override
	public void windowDeactivated(
			final WindowEvent e)
	{
	}
}

package fr.utbm.vi51.group11.lemmings.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.utbm.vi51.group11.lemmings.controller.KeyboardController;
import fr.utbm.vi51.group11.lemmings.gui.GraphicsEngine.DebugOption;
import fr.utbm.vi51.group11.lemmings.model.Environment;
import fr.utbm.vi51.group11.lemmings.model.Simulation;

public class MainFrame extends GUI implements ActionListener
{
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	private final GraphicsEngine	m_graphicsEngine;
	private final Environment		m_environment;
	private final Simulation		m_simulator;

	private JMenuBar				m_menuBar;

	private JPanel					m_PSpeedButtonPanel;
	private JButton					m_BHalfSpeed;
	private JButton					m_BNormalSpeed;
	private JButton					m_BDoubleSpeed;
	private JButton					m_BPauseButton;

	private final BorderLayout		m_mainLayout;

	public MainFrame(final Simulation _simulator, final Environment _environnement,
			final int _width, final int _height)
	{
		super();

		setTitle("Lemmings Simulation");
		createMenuBar();
		this.setJMenuBar(m_menuBar);

		int width = _width + (getInsets().bottom + getInsets().top);
		int height = _height + (getInsets().right + getInsets().left) + (m_menuBar.getHeight());

		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		m_graphicsEngine = new GraphicsEngine(_environnement, width, height);

		m_simulator = _simulator;
		m_environment = _environnement;

		KeyboardController.getInstance().updateJPanelKeyboardMaps(m_graphicsEngine);

		m_graphicsEngine.setFocusable(true);

		m_mainLayout = new BorderLayout();
		setLayout(m_mainLayout);
		setLocationRelativeTo(null);

		createSpeedButton();

		this.getContentPane().add(m_graphicsEngine, BorderLayout.CENTER);
		this.getContentPane().add(m_PSpeedButtonPanel, BorderLayout.SOUTH);

		pack();

		setVisible(true);
	}

	public void createSpeedButton()
	{
		m_PSpeedButtonPanel = new JPanel();

		m_BHalfSpeed = new JButton("* 0.5");
		m_BNormalSpeed = new JButton("* 1.0");
		m_BDoubleSpeed = new JButton("* 2.0");
		m_BPauseButton = new JButton("Pause");

		m_BHalfSpeed.addActionListener(this);
		m_BNormalSpeed.addActionListener(this);
		m_BDoubleSpeed.addActionListener(this);
		m_BPauseButton.addActionListener(this);

		m_PSpeedButtonPanel.add(m_BHalfSpeed);
		m_PSpeedButtonPanel.add(m_BNormalSpeed);
		m_PSpeedButtonPanel.add(m_BDoubleSpeed);
		m_PSpeedButtonPanel.add(m_BPauseButton);
		((FlowLayout) m_PSpeedButtonPanel.getLayout()).setAlignment(FlowLayout.LEADING);
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
		if (arg0.getSource() == m_BPauseButton)
		{
			m_simulator.togglePause();

			if (m_simulator.isPaused())
			{
				m_BPauseButton.setText("Play");
			} else
			{
				m_BPauseButton.setText("Pause");
			}
		}

		m_graphicsEngine.requestFocus();
	}

	public void displayEndGameMessage(
			final int _alive,
			final int _dead)
	{
		JOptionPane.showMessageDialog(this, "GAME OVER. You saved " + _alive + " lemmings and "
				+ _dead + " are dead !");
	}
}

package fr.utbm.vi51.group11.lemmings.gui;

import javax.swing.JFrame;

import fr.utbm.vi51.group11.lemmings.controller.KeyboardController;
import fr.utbm.vi51.group11.lemmings.model.Environment;

public class MainFrame extends GUI
{
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	private final GraphicsEngine	m_graphicsEngine;

	public MainFrame(final Environment _environnement)
	{
		super();
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		m_graphicsEngine = new GraphicsEngine(_environnement);

		this.setContentPane(m_graphicsEngine);
		KeyboardController.getInstance().updateJPanelKeyboardMaps(m_graphicsEngine);

		m_graphicsEngine.setFocusable(true);

		setVisible(true);
	}

	public GraphicsEngine getGraphicsEngine()
	{
		return m_graphicsEngine;
	}
}

package fr.utbm.vi51.group11.lemmings.gui;

import javax.swing.JFrame;

import fr.utbm.vi51.group11.lemmings.controller.MouseController;
import fr.utbm.vi51.group11.lemmings.model.Simulation;

public abstract class GUI extends JFrame
{

	/**
	 * Generated ID
	 */
	private static final long	serialVersionUID	= 7417478385067284278L;

	protected GraphicsEngine	m_graphicsEngine;
	protected Simulation		m_simulator;

	public GUI(final String _title)
	{
		super(_title);
		this.addMouseListener(MouseController.getInstance());
		this.addMouseWheelListener(MouseController.getInstance());
	}

}

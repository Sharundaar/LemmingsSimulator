package fr.utbm.vi51.group11.lemmings.gui;

import javax.swing.JFrame;

import fr.utbm.vi51.group11.lemmings.controller.MouseController;

public abstract class GUI extends JFrame
{

	/**
	 * Generated ID
	 */
	private static final long	serialVersionUID	= 7417478385067284278L;

	public GUI()
	{
		super();
		this.addMouseListener(MouseController.getInstance());
		this.addMouseWheelListener(MouseController.getInstance());
	}

}

package fr.utbm.vi51.group11.lemmings.controller.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MathKeyListener extends AbstractAction implements ActionListener
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/** Logger of the class */
	private final static Logger	s_LOGGER			= LoggerFactory
															.getLogger(MathKeyListener.class);

	public void actionPerformed(
			final ActionEvent _event)
	{
		s_LOGGER.debug("Key typed : '{}' !");
	}

}

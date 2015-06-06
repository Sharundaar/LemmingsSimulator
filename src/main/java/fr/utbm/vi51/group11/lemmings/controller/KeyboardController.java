package fr.utbm.vi51.group11.lemmings.controller;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.controller.listeners.ArrowKeyListener;
import fr.utbm.vi51.group11.lemmings.controller.listeners.HandleKeyListener;
import fr.utbm.vi51.group11.lemmings.controller.listeners.MathKeyListener;

public class KeyboardController
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger				s_LOGGER				= LoggerFactory
																			.getLogger(KeyboardController.class);
	/** Instance of the singleton */
	private final static KeyboardController	s_keyboardController	= new KeyboardController();

	/** Input map where are stored all keys that can be typed by the user */
	private final InputMap					m_inputMap;

	/** Map containing the Action to do according to the associated input index */
	private final ActionMap					m_actionMap;

	/*----------------------------------------------*/

	/** Default private constructor called only once */
	private KeyboardController()
	{
		m_inputMap = new InputMap();
		m_actionMap = new ActionMap();

		initializeMaps();
	}

	/*----------------------------------------------*/

	/**
	 * Method used to initialize the ActionMap and the InputMap of the keyboard
	 * Controller.
	 */
	private void initializeMaps()
	{
		/* Inputs Keystroke registered */
		m_inputMap.put(KeyStroke.getKeyStroke("SPACE"), "space");
		m_inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
		m_inputMap.put(KeyStroke.getKeyStroke("ENTER"), "enter");
		m_inputMap.put(KeyStroke.getKeyStroke("UP"), "up");
		m_inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down");
		m_inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		m_inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left");
		m_inputMap.put(KeyStroke.getKeyStroke("ADD"), "plus");
		m_inputMap.put(KeyStroke.getKeyStroke("SUBTRACT"), "minus");

		/* Actions related to inputs */
		m_actionMap.put("space", new HandleKeyListener());
		m_actionMap.put("escape", new HandleKeyListener());
		m_actionMap.put("enter", new HandleKeyListener());
		m_actionMap.put("up", new ArrowKeyListener());
		m_actionMap.put("down", new ArrowKeyListener());
		m_actionMap.put("right", new ArrowKeyListener());
		m_actionMap.put("left", new ArrowKeyListener());
		m_actionMap.put("plus", new MathKeyListener());
		m_actionMap.put("minus", new MathKeyListener());
	}

	/*----------------------------------------------*/

	/**
	 * @return Instance of the singleton.
	 */
	public static KeyboardController getInstance()
	{
		return s_keyboardController;
	}

	/*----------------------------------------------*/

	public void updateJPanelKeyboardMaps(
			final JPanel _panel)
	{
		_panel.getActionMap().setParent(m_actionMap);
		_panel.getInputMap().setParent(m_inputMap);
	}
}
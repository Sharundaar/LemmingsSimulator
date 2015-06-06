package fr.utbm.vi51.group11.lemmings.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class designed to receive all the inputs from the player and take the
 * appropriate action regarding the input. Inputs can be from keyboard or from
 * mouse.
 * 
 * @author jnovak
 *
 */
public class MouseController implements MouseListener, MouseMotionListener,
		MouseWheelListener {
	/** Logger of the class */
	private final static Logger s_LOGGER = LoggerFactory
			.getLogger(MouseController.class);

	/** Instance of the input controller */
	private static MouseController s_mouseController = new MouseController();

	/*----------------------------------------------*/

	/**
	 * @return The instance of the singleton of InputController.
	 */
	public static MouseController getInstance() {
		return s_mouseController;
	}

	/*----------------------------------------------*/

	public void mouseClicked(final MouseEvent _event) {
		s_LOGGER.debug("Mouse clicked with '{}'", _event.getButton());
	}

	/*----------------------------------------------*/

	public void mousePressed(final MouseEvent _event) {
		s_LOGGER.debug("Mouse button '{}' pressed.", _event.getButton());
	}

	/*----------------------------------------------*/

	public void mouseReleased(final MouseEvent _event) {
		s_LOGGER.debug("Mouse button '{}' released.", _event.getButton());
	}

	/*----------------------------------------------*/

	public void mouseEntered(final MouseEvent _event) {
		s_LOGGER.debug("Mouse entered in window.", _event.getButton());
	}

	/*----------------------------------------------*/

	public void mouseExited(final MouseEvent _event) {
		s_LOGGER.debug("Mouse exited window.");
	}

	/*----------------------------------------------*/

	public void mouseWheelMoved(final MouseWheelEvent _event) {
		s_LOGGER.debug("Mouse wheel moved '{}' '{}'.", _event
				.getPreciseWheelRotation(),
				_event.getWheelRotation() > 0 ? "up" : "down");
	}

	/*----------------------------------------------*/

	public void mouseDragged(final MouseEvent _event) {
		s_LOGGER.debug("Mouse dragged from '{}' to '{}'.");
	}

	/*----------------------------------------------*/

	public void mouseMoved(final MouseEvent _event) {
		s_LOGGER.debug("Mouse moved from '{}' to '{}'.");
	}
}

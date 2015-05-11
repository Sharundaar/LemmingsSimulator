package fr.utbm.vi51.group11.lemmings.gui;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Camera2D
{
	/** Logger of the class */
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(Camera2D.class);

	private Rectangle2f			m_clippingRect;

	private float				m_zoomRatio;

	public Camera2D()
	{

	}

}

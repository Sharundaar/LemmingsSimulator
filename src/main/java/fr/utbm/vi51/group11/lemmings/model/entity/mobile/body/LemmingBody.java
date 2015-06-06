package fr.utbm.vi51.group11.lemmings.model.entity.mobile.body;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.gui.texture.Sprite;
import fr.utbm.vi51.group11.lemmings.model.Environment;
import fr.utbm.vi51.group11.lemmings.model.physics.properties.CollisionProperty;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionMask;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.RectangleShape;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.ICollidable;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;
import fr.utbm.vi51.group11.lemmings.utils.statics.LemmingUtils;

public class LemmingBody extends Body implements ICollidable
{
	/** Logger of the class */
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(LemmingBody.class);

	private final float			m_maxSpeed;
	private float				m_maxAcceleration;

	/*----------------------------------------------*/

	public LemmingBody(final String _textureID, final Point2f _worldCoords,
			final Environment _environment)
	{
		s_LOGGER.debug("Creation of the Lemming Body...");

		m_type = WorldEntityEnum.LEMMING_BODY;

		m_alive = false;
		m_worldCoords = _worldCoords;
		
		m_collisionMask = new CollisionMask(m_worldCoords);
		m_collisionMask.addChild(new RectangleShape(LemmingUtils.LEMMING_DEFAULT_WIDTH, LemmingUtils.LEMMING_DEFAULT_HEIGHT, null));
		
		CollisionProperty cp = new CollisionProperty();
		cp.setEntity(this);
		m_collisionMask.setProperty(cp);
		
		// TODO lemming frustrum
		m_maxSpeed = LemmingUtils.s_lemmingMaxVelocity;
		m_environment = _environment;
		// TODO world entity shapes
		m_sprite = new Sprite(m_worldCoords.x(), m_worldCoords.y(),
				LemmingUtils.LEMMING_DEFAULT_WIDTH, LemmingUtils.LEMMING_DEFAULT_HEIGHT, 0, 0, 27,
				26, _textureID);
		
		m_state = BodyState.NORMAL;
		m_stateProperty = new BodyStateProperty();
		
		m_influences = new LinkedList<Influence>();
		
		m_mass = LemmingUtils.LEMMING_MASS;
		
		s_LOGGER.debug("Lemming Body created.");
	}

	/*----------------------------------------------*/

	/**
	 * Method used to filter the perception of the body. If Perceivable objects
	 * are hidden by some obstacle, the list of perceivable objects are to be
	 * updated.
	 * 
	 * @return True if the list has been totally filtered.</br>False otherwise.
	 */
	@Override
	public boolean filterPerception(
			final List<IPerceivable> _perception)
	{
		return false; // TODO
	}

	/*----------------------------------------------*/

	/**
	 * Method used to filter the action of the body. If for example the body
	 * wants to move but is paralyzed, it will filter the action of moving.
	 * 
	 * @return True if the action is filtered.</br>False otherwise.
	 */
	@Override
	public boolean filterInfluence()
	{

		return false; // TODO
	}
	
}
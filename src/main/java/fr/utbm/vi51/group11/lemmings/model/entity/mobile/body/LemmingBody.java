package fr.utbm.vi51.group11.lemmings.model.entity.mobile.body;

import java.util.LinkedList;
import java.util.List;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.gui.texture.Animation;
import fr.utbm.vi51.group11.lemmings.gui.texture.Sprite;
import fr.utbm.vi51.group11.lemmings.model.Environment;
import fr.utbm.vi51.group11.lemmings.model.physics.properties.CollisionProperty;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CircleShape;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionMask;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.RectangleShape;
import fr.utbm.vi51.group11.lemmings.utils.enums.AnimationState;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.ICollidable;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.misc.Frustrum;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;
import fr.utbm.vi51.group11.lemmings.utils.misc.LemmingFrustrum;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

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

		/* Fills the animation list. */
		m_animations = new MultivaluedMapImpl<BodyState, Animation>();
		fillAnimationMap();

		m_alive = false;
		m_worldCoords = _worldCoords;

		m_collisionMask = new CollisionMask(m_worldCoords);
		m_collisionMask.addChild(new RectangleShape(UtilsLemmings.s_lemmingEntityWidth,
				UtilsLemmings.s_lemmingEntityHeight, null));

		CollisionProperty cp = new CollisionProperty();
		cp.setEntity(this);
		m_collisionMask.setProperty(cp);

		// TODO lemming frustrum
		m_maxSpeed = UtilsLemmings.s_lemmingMaxVelocity;
		m_environment = _environment;
		// TODO world entity shapes
		m_sprite = new Sprite(m_worldCoords.x(), m_worldCoords.y(),
				UtilsLemmings.s_lemmingEntityWidth, UtilsLemmings.s_lemmingEntityHeight, _textureID);

		m_state = BodyState.NORMAL;
		m_stateProperty = new BodyStateProperty();
		m_currentAnimation = m_animations.get(m_state).get(1);
		m_currentAnimation.setBodyState(m_state);
		m_currentAnimation.setBodyStateProperty(m_stateProperty);
		updateAnimation(0);

		m_influences = new LinkedList<Influence>();

		m_mass = UtilsLemmings.s_lemmingMass;
		
		m_frustrum = new LemmingFrustrum(new CircleShape(UtilsLemmings.s_tileWidth*3, null));

		s_LOGGER.debug("Lemming Body created.");
	}

	private void fillAnimationMap()
	{
		for (AnimationState state : AnimationState.values())
			m_animations.add(state.getBodyState(), new Animation(state));
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

	@Override
	public Point2f getCenterCoordinates()
	{
		return new Point2f(m_worldCoords.getX() + (UtilsLemmings.s_lemmingEntityWidth / 2.0f),
				m_worldCoords.getY() + (UtilsLemmings.s_lemmingEntityHeight / 2.0f));
	}

	@Override
	public Point2f getWorldCoordinates()
	{
		return new Point2f(m_worldCoords);
	}
}
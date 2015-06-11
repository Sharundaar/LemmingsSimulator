package fr.utbm.vi51.group11.lemmings.model.entity.immobile;

import java.util.LinkedList;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.discrete.object2d.Tuple2i;
import org.arakhne.afc.math.generic.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.gui.texture.Sprite;
import fr.utbm.vi51.group11.lemmings.model.Environment;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.LemmingBody;
import fr.utbm.vi51.group11.lemmings.model.physics.properties.CollisionProperty;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionMask;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.RectangleShape;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.ICollidable;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.statics.UtilsLemmings;

public class LevelEnd extends WorldEntity implements ICollidable, IPerceivable
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(LevelEnd.class);
	private Environment m_environment;
	
	private int m_lemmingStashed = 0;

	public LevelEnd(final String _textureID, final Point2f _worldCoords, Environment _environment)
	{
		m_worldCoords = _worldCoords;
		m_type = WorldEntityEnum.LEVEL_END;
		m_sprite = new Sprite(_worldCoords.x(), _worldCoords.y(), UtilsLemmings.s_exitDefaultWidth,
				UtilsLemmings.s_exitDefaultHeight, 64, 0, 64, 32, _textureID); // TODO

		m_collisionMask = new CollisionMask(m_worldCoords);
		m_collisionMask.addChild(new RectangleShape(UtilsLemmings.s_entryDefaultWidth,
				UtilsLemmings.s_entryDefaultHeight, null));

		CollisionProperty prop = new CollisionProperty();
		prop.setCrossable(true);
		RectangleShape activableArea = new RectangleShape(25, 0, 10, 20, null);
		m_collisionMask.addChild(activableArea);
		prop.setActivable(true, activableArea);
		prop.setEntity(this);
		
		m_environment = _environment;

		m_collisionMask.setProperty(prop);
	}

	public Point2f getEndPoint() {
		// TODO Auto-generated method stub
		return ((RectangleShape) m_collisionMask.getProperty().getActivationZone()).getCenter();
	}

	public void update(long _dt) {
		LinkedList<WorldEntity> copy = new LinkedList<WorldEntity>(m_environment.m_worldEntities);
		for(WorldEntity ent : copy)
		{
			if(ent.getType() == WorldEntityEnum.LEMMING_BODY && m_environment.getPhysicEngine().isInActiveZone((LemmingBody)ent))
			{
				m_environment.destroyEntity(ent);
				m_lemmingStashed++;
			}
		}
	}

	public int getLemmingStashCount() {
		return m_lemmingStashed;
	}

}

package fr.utbm.vi51.group11.lemmings.model.entity.immobile;

import org.arakhne.afc.math.continous.object2d.Point2f;
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

public class LevelStart extends WorldEntity implements ICollidable, IPerceivable
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(LevelStart.class);

	public final static int s_LEMMINGS_AT_START = 10;
	
	private int m_stashedLemmingCount = s_LEMMINGS_AT_START;
	private long m_spawnLemmingTimer = 0;
	private long m_spawnRate = 5000;
	private Point2f m_spawnPoint;
	
	private Environment m_environment;
	
	public LevelStart(final String _textureID, final Point2f _worldCoords, Environment _environment)
	{
		m_worldCoords = _worldCoords;
		m_type = WorldEntityEnum.LEVEL_START;
		m_sprite = new Sprite(_worldCoords.x(), _worldCoords.y(),
				UtilsLemmings.s_entryDefaultWidth, UtilsLemmings.s_entryDefaultHeight, 0, 0, 64,
				32, _textureID); // TODO

		m_collisionMask = new CollisionMask(m_worldCoords);
		m_collisionMask.addChild(new RectangleShape(UtilsLemmings.s_entryDefaultWidth,
				UtilsLemmings.s_entryDefaultHeight, null));
		m_collisionMask.setProperty(new CollisionProperty());
		m_collisionMask.getProperty().setEntity(this);
		
		m_spawnPoint = new Point2f(_worldCoords.getX() + UtilsLemmings.s_entryDefaultWidth / 2.0f, _worldCoords.getY());
		
		m_stashedLemmingCount = 10;
		
		m_environment = _environment;
	}
	
	public void update(long _dt)
	{
		m_spawnLemmingTimer += _dt;
		if(m_spawnLemmingTimer >= m_spawnRate && m_stashedLemmingCount > 0)
		{
			m_spawnLemmingTimer = 0;
			spawnLemming();
		}
	}
	
	public void spawnLemming()
	{
		LemmingBody lemming = new LemmingBody("entitySpriteSheet", new Point2f(m_spawnPoint), m_environment);
		m_environment.createWorldEntity(lemming);
		m_stashedLemmingCount--;
	}

	public Point2f getSpawnPoint() {
		// TODO Auto-generated method stub
		return m_spawnPoint;
	}
}

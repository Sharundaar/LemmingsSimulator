package fr.utbm.vi51.group11.lemmings.model.entity.immobile;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.gui.texture.Sprite;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.model.physics.properties.CollisionProperty;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.CollisionMask;
import fr.utbm.vi51.group11.lemmings.model.physics.shapes.RectangleShape;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.ICollidable;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.statics.LemmingUtils;

public class LevelStart extends WorldEntity implements ICollidable, IPerceivable
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(LevelStart.class);

	public LevelStart(final String _textureID, final Point2f _worldCoords)
	{
		m_worldCoords = _worldCoords;
		m_type = WorldEntityEnum.LEVEL_START;
		m_sprite = new Sprite(_worldCoords.x(), _worldCoords.y(), LemmingUtils.ENTRY_DEFAULT_WIDTH, LemmingUtils.ENTRY_DEFAULT_HEIGHT, 
				0, 0, 5, 5, _textureID); // TODO
		
		m_collisionMask = new CollisionMask(m_worldCoords);
		m_collisionMask.addChild(new RectangleShape(LemmingUtils.ENTRY_DEFAULT_WIDTH, LemmingUtils.ENTRY_DEFAULT_HEIGHT, null));
		m_collisionMask.setProperty(new CollisionProperty());
		m_collisionMask.getProperty().setEntity(this);
	}
}

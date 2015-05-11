package fr.utbm.vi51.group11.lemmings.model.entity.immobile;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.gui.texture.Sprite;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.ICollidable;
import fr.utbm.vi51.group11.lemmings.utils.interfaces.IPerceivable;
import fr.utbm.vi51.group11.lemmings.utils.statics.LemmingUtils;

public class LevelEnd extends WorldEntity implements ICollidable, IPerceivable
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(LevelEnd.class);

	public LevelEnd(final String _textureID, final Point2f _worldCoords)
	{
		m_worldCoords = _worldCoords;
		m_type = WorldEntityEnum.LEVEL_END;
		m_sprite = new Sprite(_worldCoords.x(), _worldCoords.y(), LemmingUtils.EXIT_DEFAULT_WIDTH, LemmingUtils.EXIT_DEFAULT_HEIGHT, 
				0, 0, 5, 5, _textureID); // TODO
	}

}

package fr.utbm.vi51.group11.lemmings.utils.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.Environment;
import fr.utbm.vi51.group11.lemmings.model.entity.immobile.LevelEnd;
import fr.utbm.vi51.group11.lemmings.model.entity.immobile.LevelStart;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.LemmingBody;
import fr.utbm.vi51.group11.lemmings.utils.configuration.level.WorldEntityConfiguration;

/**
 * 
 * Class designed to create WorldEntities from specific IDs.
 * 
 * @author jnovak
 *
 */
public class EntityFactory
{
	/** Instance of the singleton. */
	private static final EntityFactory	m_entityFactory	= new EntityFactory();

	/** Logger of the class */
	private final static Logger			s_LOGGER		= LoggerFactory
																.getLogger(EntityFactory.class);

	/*----------------------------------------------*/

	/**
	 * Default constructor.
	 */
	private EntityFactory()
	{
	}

	/*----------------------------------------------*/

	/**
	 * @return Instance of the singleton of EntityFactory.
	 */
	public static EntityFactory getInstance()
	{
		return m_entityFactory;
	}

	/*----------------------------------------------*/

	public LemmingBody createLemmingBody(
			final WorldEntityConfiguration _worldEntityConfiguration,
			final Environment _environment)
	{
		s_LOGGER.debug("Creating LemmingBody in ({},{}).", _worldEntityConfiguration
				.getLevelCoords().x(), _worldEntityConfiguration.getLevelCoords().y());

		return new LemmingBody(_worldEntityConfiguration.getTextureID(),
				_worldEntityConfiguration.getLevelCoords(), _environment);
	}

	/*----------------------------------------------*/

	public LevelStart createLevelStart(
			final WorldEntityConfiguration _worldEntityConfiguration)
	{
		s_LOGGER.debug("Creating LevelStart in ({},{}).", _worldEntityConfiguration
				.getLevelCoords().x(), _worldEntityConfiguration.getLevelCoords().y());

		return new LevelStart(_worldEntityConfiguration.getTextureID(),
				_worldEntityConfiguration.getLevelCoords());
	}

	/*----------------------------------------------*/

	public LevelEnd createLevelEnd(
			final WorldEntityConfiguration _worldEntityConfiguration)
	{
		s_LOGGER.debug("Creating LevelEnd in ({},{}).", _worldEntityConfiguration.getLevelCoords()
				.x(), _worldEntityConfiguration.getLevelCoords().y());

		return new LevelEnd(_worldEntityConfiguration.getTextureID(),
				_worldEntityConfiguration.getLevelCoords());
	}
}
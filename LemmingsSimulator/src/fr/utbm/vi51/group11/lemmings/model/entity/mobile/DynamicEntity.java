package fr.utbm.vi51.group11.lemmings.model.entity.mobile;

import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.utbm.vi51.group11.lemmings.model.Environment;
import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;

/**
 * 
 * Abstract class representing a WorldEntity that has the ability to move inside
 * the environment.
 * 
 * @author jnovak
 *
 */
public abstract class DynamicEntity extends WorldEntity
{
	/** Logger of the class */
	@SuppressWarnings("unused")
	private final static Logger	s_LOGGER	= LoggerFactory.getLogger(DynamicEntity.class);

	/** Reference to the environment of the simulation */
	protected Environment		m_environment;

	/** Current speed */
	protected Vector2f 			m_speed;
	
	/** Current acceleration */
	protected Vector2f 			m_acceleration;
	
	/*----------------------------------------------*/

	/**
	 * @return The environment of the simulation.
	 */
	public Environment getEnvironment()
	{
		return m_environment;
	}

	/*----------------------------------------------*/

	/**
	 * Method used to set a reference on the environment inside an attribute.
	 * 
	 * @param _environment
	 *            Environment to reference on.
	 */
	public void setEnvironment(
			final Environment _environment)
	{
		this.m_environment = _environment;
	}
	
	/*----------------------------------------------*/
	
	/**
	 * 
	 * @return current speed
	 */
	public Vector2f getSpeed()
	{
		return m_speed;
	}
	
	/*----------------------------------------------*/
	
	/**
	 * function makes no verification on the parameter
	 * @param _speed new speed
	 */
	public void setSpeed(Vector2f _speed)
	{
		m_speed.set(_speed);
	}
	
	/*----------------------------------------------*/
	
	/**
	 * 
	 * @return current acceleration
	 */
	public Vector2f getAcceleration()
	{
		return m_acceleration;
	}
	
	/*----------------------------------------------*/
	
	/**
	 * function makes no verification on the parameter
	 * @param _acceleration new acceleration
	 */
	public void setAcceleration(Vector2f _acceleration)
	{
		m_acceleration.set(_acceleration);
	}

}
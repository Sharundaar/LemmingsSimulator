package fr.utbm.vi51.group11.lemmings.utils.interfaces;

import java.util.List;

import org.arakhne.afc.math.continous.object2d.Vector2f;

import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.BodyState;
import fr.utbm.vi51.group11.lemmings.utils.misc.Action;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;

/**
 * 
 * Interface designed to allow the "mind" of the body to use only these
 * functions and not to access the environment and the possible transformations
 * to perform (which is not its role).
 * 
 * @author jnovak
 *
 */
public interface IControllable
{
	/**
	 * Method used to ask the environment a list of Perceivable objects the body
	 * can see through its body's frustrum.
	 * 
	 * @return List of Perceivable objects computed by the environment according
	 *         to the body's frustrum.
	 */
	public List<IPerceivable> getPerception();

	/*----------------------------------------------*/

	/**
	 * Add influence to the body.
	 * 
	 * @param _influence
	 *            Influence to add.
	 */
	public boolean addInfluence(
			Influence _influence);

	/*----------------------------------------------*/

	/**
	 * Remove influence to the body.
	 * 
	 * @param _influence
	 *            Influence to remove.
	 */
	public boolean removeInfluence(
			Influence _influence);

	/*----------------------------------------------*/

	/**
	 * Remove influence to the body.
	 * 
	 * @param _influence
	 *            Influence to remove.
	 */
	public BodyState getState();

	/*----------------------------------------------*/

	/**
	 * Method used to add a speed influence to the body.
	 * 
	 * @param _speed
	 *            Speed influence of the body.
	 */
	public void influenceSpeed(
			Vector2f _speed);

	/*----------------------------------------------*/

	/**
	 * Method used to add an acceleration influence to the body.
	 * 
	 * @param _acceleration
	 *            Acceleration influence of the body.
	 */
	public void influenceAcceleration(
			Vector2f _acceleration);

	/*----------------------------------------------*/

	/**
	 * Method used to add an Action influence to the body.
	 * 
	 * @param _action
	 *            Action influence to perform for the body.
	 */
	public void influenceAction(
			Action _action);

}
package fr.utbm.vi51.group11.lemmings.utils.interfaces;

import org.arakhne.afc.math.continous.object2d.Point2f;

import fr.utbm.vi51.group11.lemmings.model.entity.WorldEntity;
import fr.utbm.vi51.group11.lemmings.utils.enums.WorldEntityEnum;

public interface IPerceivable
{
	public WorldEntityEnum getType();
	public Point2f getWorldCoordinates();
}

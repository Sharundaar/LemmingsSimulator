package fr.utbm.vi51.group11.lemmings.model;

import java.util.LinkedList;

import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.Body;
import fr.utbm.vi51.group11.lemmings.model.entity.mobile.body.BodyState;
import fr.utbm.vi51.group11.lemmings.utils.enums.InfluenceType;
import fr.utbm.vi51.group11.lemmings.utils.misc.Influence;

public class InfluenceSolver {
	public InfluenceSolver()
	{
		
	}
	
	public void solveInfluence(LinkedList<Body> _bodies)
	{
		for(Body body : _bodies)
		{
			for(Influence inf : body.getInfluences())
			{
				if(inf.getType() == InfluenceType.SPEED && inf.getSpeed() != null)
				{
					body.getSpeed().set(0, 0);
					switch(body.getState())
					{
					case CLIMBING:
						body.getSpeed().set(inf.getSpeed());
						break;
					case DEAD:
						break;
					case FALLING:
						break;
					case NORMAL:
						body.getSpeed().setX(inf.getSpeed().getX());
						break;
					default:
						break;
					
					}
				}
				else if(inf.getType() == InfluenceType.ACTION && inf.getAction() != null)
				{
					switch(inf.getAction().getType())
					{
					case BLOCK:
						break;
					case DIG_HORIZONTAL:
						break;
					case DIG_VERTICAL:
						break;
					case UMBRELLA:
						if(body.getState() == BodyState.FALLING && !body.getStateProperty().m_chuteOpen)
							body.getStateProperty().m_chuteOpen = true;
						break;
					default:
						break;
					
					}
				}
			}
		}
	}
}

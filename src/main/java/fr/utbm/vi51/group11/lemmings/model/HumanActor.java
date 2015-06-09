package fr.utbm.vi51.group11.lemmings.model;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import fr.utbm.vi51.group11.lemmings.model.entity.immobile.map.Map;
import fr.utbm.vi51.group11.lemmings.utils.enums.CellType;

public class HumanActor implements MouseListener {

	Simulation m_simulator;
	
	public HumanActor(Simulation _simulator)
	{
		m_simulator = _simulator;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		int x = arg0.getX(), y = arg0.getY();
		Map map = m_simulator.getEnvironment().getMap();
		
		if(x < map.getWidth() && y < map.getHeight())
		{
			CellType cellType = map.getCellType(x, y, false);
			if(cellType.isCrossable() && !cellType.isDangerous())
				map.fillCell(x, y);
			else if(!cellType.isCrossable())
				map.digCell(x, y);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}

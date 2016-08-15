package colourshift.model.blocks;

import colourshift.model.Direction;
import colourshift.model.power.EmptyPower;
import colourshift.model.power.Power;

public class Empty extends Block {

	@Override
	public void colourUpdateReceived(Direction fromDirection, boolean updateEagerly) {
		
	}

	@Override
	public Power getPower() {
		return EmptyPower.getInstance();
	}

}

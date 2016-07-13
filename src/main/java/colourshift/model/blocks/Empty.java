package colourshift.model.blocks;

import java.util.Arrays;
import java.util.List;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.angle.Angle;
import colourshift.model.angle.Single;
import colourshift.model.power.Power;
import colourshift.model.power.EmptyPower;

public class Empty extends Block {

	@Override
	public void updateReceived(Direction fromDirection, Colour colour, boolean updateEagerly) {
		
	}

	@Override
	public void fullUpdate() {

	}

	@Override
	public void fullClear() {

	}

	@Override
	public Power getPower() {
		return EmptyPower.getInstance();
	}


	
}

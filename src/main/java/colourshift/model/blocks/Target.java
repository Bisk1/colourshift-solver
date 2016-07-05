package colourshift.model.blocks;

import java.util.Arrays;
import java.util.List;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.angle.Angle;
import colourshift.model.power.Power;
import colourshift.model.power.TargetPower;

public class Target extends Block {

	private TargetPower power;

	public Target(Colour requiredColour) {
		super();
		this.power = new TargetPower(requiredColour);
	}
	
	@Override
	public void updateReceived(Direction fromDirection, Colour colour) {
		if (fromDirection == angle) {
			power.setCurrent(colour);
		}
	}
	
	public boolean isActive() {
		return power.getCurrent() == power.getRequired();
	}

	@Override
	public Power getPower() {
		return power;
	}
	
}

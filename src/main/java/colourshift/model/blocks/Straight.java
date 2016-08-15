package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.power.Power;
import colourshift.model.power.SimplePower;

public class Straight extends TransitiveBlock {

	private SimplePower power;

	public Straight() {
		super();
		this.power = new SimplePower();
	}

	@Override
	void updatePower(Direction fromDirection, Colour colour) {
		power.setColour(colour);
	}

	@Override
	public Power getPower() {
		return power;
	}

}

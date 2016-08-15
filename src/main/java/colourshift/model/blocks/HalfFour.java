package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.power.HalfFourPower;
import colourshift.model.power.Power;

public class HalfFour extends TransitiveBlock {

	private HalfFourPower power;

	public HalfFour() {
		super();
		this.power = new HalfFourPower();
	}

	@Override
	void updatePower(Direction fromDirection, Colour colour) {
		switch(fromDirection) {
			case LEFT: case RIGHT:
				power.setHorizontal(colour);
				break;
			default:
				power.setVertical(colour);
		}
	}

	@Override
	public Power getPower() {
		return power;
	}

}

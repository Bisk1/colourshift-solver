package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.angle.DoubleTurnAngle;
import colourshift.model.power.DoubleTurnPower;
import colourshift.model.power.Power;

public class DoubleTurn extends TransitiveBlock {

	private DoubleTurnPower power;
	
	public DoubleTurn() {
		super();
		this.power = new DoubleTurnPower();
	}

	@Override
	void updatePower(Direction fromDirection, Colour colour) {
        if (angle == DoubleTurnAngle.LEFT_UP_AND_RIGHT_DOWN) {
            switch(fromDirection) {
                case LEFT: case UP:
                    power.setLeft(colour);
                default:
                    power.setRight(colour);
            }
        } else {
            switch(fromDirection) {
                case LEFT: case DOWN:
                    power.setLeft(colour);
                default:
                    power.setRight(colour);
            }
        }
	}

	@Override
	public Power getPower() {
		return power;
	}

}

package colourshift.model.blocks;

import java.util.Map;

import colourshift.model.angle.DoubleTurnAngle;
import com.google.common.collect.ImmutableMap;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.power.DoubleTurnPower;
import colourshift.model.power.Power;

public class DoubleTurn extends TransitiveBlock {

	private DoubleTurnPower power;
	
	public DoubleTurn() {
		super();
		this.power = new DoubleTurnPower();
	}

	@Override
	public Map<Angle, DirectionsDivision> getDirectionsDivisions() {
		return ImmutableMap.of(DoubleTurnAngle.LEFT_UP_AND_RIGHT_DOWN, new DirectionsDivision(
						new DirectionSet(Direction.LEFT, Direction.UP),
						new DirectionSet(Direction.RIGHT, Direction.DOWN)),
				DoubleTurnAngle.LEFT_DOWN_AND_UP_RIGHT, new DirectionsDivision(
						new DirectionSet(Direction.LEFT, Direction.DOWN),
						new DirectionSet(Direction.UP, Direction.RIGHT)));
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

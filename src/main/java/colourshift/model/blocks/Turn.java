package colourshift.model.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.angle.TurnAngle;
import colourshift.model.power.Power;
import colourshift.model.power.SimplePower;

public class Turn extends TransitiveBlock {

	private SimplePower power;

	public Turn() {
		super();
		this.power = new SimplePower();
	}

	@Override
	protected Map<Angle, DirectionsDivision> getDirectonsDivisions() {
		return ImmutableMap.of(
				TurnAngle.LEFT_UP, new DirectionsDivision(new DirectionSet(Direction.LEFT, Direction.UP)), 
				TurnAngle.UP_RIGHT, new DirectionsDivision(new DirectionSet(Direction.UP, Direction.RIGHT)), 
				TurnAngle.RIGHT_DOWN, new DirectionsDivision(new DirectionSet(Direction.RIGHT, Direction.DOWN)), 
				TurnAngle.LEFT_DOWN, new DirectionsDivision(new DirectionSet(Direction.LEFT, Direction.DOWN)));
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

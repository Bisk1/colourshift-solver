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
import colourshift.model.angle.ThreeAngle;
import colourshift.model.power.Power;
import colourshift.model.power.SimplePower;

public class Three extends TransitiveBlock {

	private SimplePower power;

	public Three() {
		super();
		this.power = new SimplePower();
	}

	@Override
	protected Map<Angle, DirectionsDivision> getDirectionsDivisions() {
		return ImmutableMap.of(
				ThreeAngle.NOT_LEFT, new DirectionsDivision(new DirectionSet(Direction.UP, Direction.RIGHT, Direction.DOWN)), 
				ThreeAngle.NOT_UP, new DirectionsDivision(new DirectionSet(Direction.LEFT, Direction.RIGHT, Direction.DOWN)), 
				ThreeAngle.NOT_RIGHT, new DirectionsDivision(new DirectionSet(Direction.LEFT, Direction.UP, Direction.DOWN)), 
				ThreeAngle.NOT_DOWN, new DirectionsDivision(new DirectionSet(Direction.LEFT, Direction.UP, Direction.RIGHT)));
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

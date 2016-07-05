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
import colourshift.model.angle.Orientation;
import colourshift.model.power.Power;
import colourshift.model.power.SimplePower;

public class Straight extends TransitiveBlock {

	private SimplePower power;

	public Straight() {
		super();
		this.power = new SimplePower();
	}

	@Override
	protected Map<Angle, DirectionsDivision> getDirectonsDivisions() {
		return ImmutableMap.of(
				Orientation.HORIZONTAL, new DirectionsDivision(new DirectionSet(Direction.LEFT, Direction.RIGHT)), 
				Orientation.VERTICAL, new DirectionsDivision(new DirectionSet(Direction.UP, Direction.DOWN)));
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

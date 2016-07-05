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
import colourshift.model.angle.Single;
import colourshift.model.power.DoubleTurnPower;
import colourshift.model.power.Power;

public class DoubleTurn extends TransitiveBlock {

	private DoubleTurnPower power;
	
	public DoubleTurn() {
		super();
		this.power = new DoubleTurnPower();
	}

	@Override
	protected Map<Angle, DirectionsDivision> getDirectonsDivisions() {
		return ImmutableMap.of(Single.SINGLE, new DirectionsDivision(
						new DirectionSet(Direction.LEFT, Direction.RIGHT),
						new DirectionSet(Direction.UP, Direction.DOWN)));
	}

	@Override
	void updatePower(Direction fromDirection, Colour colour) {
		switch(fromDirection) {
		case LEFT: case RIGHT:
			power.setHorizontal(colour);
		default:
			power.setVertical(colour);
		}
	}

	@Override
	public Power getPower() {
		return power;
	}

}

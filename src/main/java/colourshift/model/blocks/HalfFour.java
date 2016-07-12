package colourshift.model.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import colourshift.model.angle.Single;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.power.HalfFourPower;
import colourshift.model.power.Power;

public class HalfFour extends TransitiveBlock {

	private HalfFourPower power;

	public HalfFour() {
		super();
		this.power = new HalfFourPower();
	}

	@Override
	protected Map<Angle, DirectionsDivision> getDirectionsDivisions() {
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

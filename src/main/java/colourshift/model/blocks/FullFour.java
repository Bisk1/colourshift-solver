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
import colourshift.model.power.Power;
import colourshift.model.power.SimplePower;

public class FullFour extends TransitiveBlock {

	private SimplePower power;

	public FullFour() {
		super();
		this.power = new SimplePower();
	}

	@Override
	public Map<Angle, DirectionsDivision> getDirectionsDivisions() {
		return ImmutableMap.of(Single.SINGLE, new DirectionsDivision(
				new DirectionSet(Direction.values())));
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

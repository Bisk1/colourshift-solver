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
import colourshift.model.power.HalfFourPower;
import colourshift.model.power.Power;

public class HalfFour extends TransitiveBlock {

	private HalfFourPower power;
	
	private enum HalfFourAngle implements Angle {
		LEFT_DOWN_AND_UP_RIGHT,
		LEFT_UP_AND_RIGHT_DOWN;
	}
	
	public HalfFour() {
		super();
		this.power = new HalfFourPower();
	}

	private static List<Angle> initialAngles = Arrays.asList(HalfFourAngle.values());

	@Override
	protected Map<Angle, DirectionsDivision> getDirectonsDivisions() {
		return ImmutableMap.of(HalfFourAngle.LEFT_DOWN_AND_UP_RIGHT, new DirectionsDivision(
						new DirectionSet(Direction.LEFT, Direction.DOWN),
						new DirectionSet(Direction.UP, Direction.RIGHT)),
				HalfFourAngle.LEFT_UP_AND_RIGHT_DOWN, new DirectionsDivision(
						new DirectionSet(Direction.LEFT, Direction.UP),
						new DirectionSet(Direction.RIGHT, Direction.DOWN)));
	}

	@Override
	void updatePower(Direction fromDirection, Colour colour) {
		if (angle == HalfFourAngle.LEFT_DOWN_AND_UP_RIGHT) {
			switch(fromDirection) {
				case LEFT: case DOWN:
					power.setLeft(colour);
				default:
					power.setRight(colour);
			}
		} else {
			switch(fromDirection) {
			case LEFT: case UP:
				power.setLeft(colour);
			default:
				power.setRight(colour);
			}
		}
	}

	@Override
	protected List<Angle> getInitialAngles() {
		return initialAngles;
	}

	@Override
	public Power getPower() {
		return power;
	}

}

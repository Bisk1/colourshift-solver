package colourshift.model.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import colourshift.model.SingleDirectionsDivision;
import com.google.common.collect.ImmutableMap;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.angle.ThreeAngle;
import colourshift.model.angle.TurnAngle;

public class SourceThree extends Source {
	
	private static Map<Angle, SingleDirectionsDivision> directionsDivisionMap = ImmutableMap.of(
			ThreeAngle.NOT_LEFT, new SingleDirectionsDivision(Direction.DOWN, Direction.UP, Direction.RIGHT),
			ThreeAngle.NOT_UP, new SingleDirectionsDivision(Direction.LEFT, Direction.DOWN, Direction.RIGHT),
			ThreeAngle.NOT_RIGHT, new SingleDirectionsDivision(Direction.LEFT, Direction.UP, Direction.DOWN),
			ThreeAngle.NOT_DOWN, new SingleDirectionsDivision(Direction.LEFT, Direction.UP, Direction.RIGHT)
			);

	public SourceThree(Colour colour) {
		super(colour);
	}

	@Override
	public Map<Angle, SingleDirectionsDivision> getDirectionsDivisions() {
		return directionsDivisionMap;
	}

}

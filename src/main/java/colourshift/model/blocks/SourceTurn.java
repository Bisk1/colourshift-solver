package colourshift.model.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.angle.TurnAngle;

public class SourceTurn extends Source {
	
	private static Map<Angle, DirectionSet> directionsSets = ImmutableMap.of(
			TurnAngle.LEFT_UP, new DirectionSet(Direction.LEFT, Direction.UP),
			TurnAngle.UP_RIGHT, new DirectionSet(Direction.UP, Direction.RIGHT),
			TurnAngle.RIGHT_DOWN, new DirectionSet(Direction.RIGHT, Direction.DOWN),
			TurnAngle.LEFT_DOWN, new DirectionSet(Direction.LEFT, Direction.DOWN)
			);

	public SourceTurn(Colour colour) {
		super(colour);
	}

	@Override
	protected Map<Angle, DirectionSet> getDirectionsSets() {
		return directionsSets;
	}

}

package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.angle.TurnAngle;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

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
	public Map<Angle, DirectionSet> getDirectionsSets() {
		return directionsSets;
	}

}

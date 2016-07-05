package colourshift.model.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.angle.ThreeAngle;
import colourshift.model.angle.TurnAngle;

public class SourceThree extends Source {
	
	private static Map<Angle, DirectionSet> directionsSets = ImmutableMap.of(
			ThreeAngle.NOT_LEFT, new DirectionSet(Direction.DOWN, Direction.UP, Direction.RIGHT),
			ThreeAngle.NOT_UP, new DirectionSet(Direction.LEFT, Direction.DOWN, Direction.RIGHT),
			ThreeAngle.NOT_RIGHT, new DirectionSet(Direction.LEFT, Direction.UP, Direction.DOWN),
			ThreeAngle.NOT_DOWN, new DirectionSet(Direction.LEFT, Direction.UP, Direction.RIGHT)
			);

	public SourceThree(Colour colour) {
		super(colour);
	}

	@Override
	protected Map<Angle, DirectionSet> getDirectionsSets() {
		return directionsSets;
	}

}

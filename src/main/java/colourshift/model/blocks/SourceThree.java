package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.angle.ThreeAngle;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

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
	public Map<Angle, DirectionSet> getDirectionsSets() {
		return directionsSets;
	}

}

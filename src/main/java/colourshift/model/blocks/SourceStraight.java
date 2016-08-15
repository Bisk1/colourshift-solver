package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.angle.Orientation;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class SourceStraight extends Source {
	
	private static Map<Angle, DirectionSet> directionsSets = ImmutableMap.of(
			Orientation.HORIZONTAL, new DirectionSet(Direction.LEFT, Direction.RIGHT),
			Orientation.VERTICAL, new DirectionSet(Direction.UP, Direction.DOWN)
			);

	public SourceStraight(Colour colour) {
		super(colour);
	}

	@Override
	public Map<Angle, DirectionSet> getDirectionsSets() {
		return directionsSets;
	}

}

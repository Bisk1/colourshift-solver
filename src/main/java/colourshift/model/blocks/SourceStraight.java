package colourshift.model.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.angle.Orientation;

public class SourceStraight extends Source {
	
	private static Map<Angle, DirectionSet> directionsSets = ImmutableMap.of(
			Orientation.HORIZONTAL, new DirectionSet(Direction.LEFT, Direction.RIGHT),
			Orientation.VERTICAL, new DirectionSet(Direction.UP, Direction.DOWN)
			);

	public SourceStraight(Colour colour) {
		super(colour);
	}

	@Override
	protected List<Angle> getInitialAngles() {
		return Arrays.asList(Orientation.values());
	}

	@Override
	protected Map<Angle, DirectionSet> getDirectionsSets() {
		return directionsSets;
	}

}

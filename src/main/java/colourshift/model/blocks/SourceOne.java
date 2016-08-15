package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class SourceOne extends Source {
	
	private static Map<Angle, DirectionSet> directionsSets = ImmutableMap.of(
			Direction.LEFT, new DirectionSet(Direction.LEFT),
			Direction.UP, new DirectionSet(Direction.UP),
			Direction.RIGHT, new DirectionSet(Direction.RIGHT),
			Direction.DOWN, new DirectionSet(Direction.DOWN)
			);

	public SourceOne(Colour colour) {
		super(colour);
	}

	@Override
	public Map<Angle, DirectionSet> getDirectionsSets() {
		return directionsSets;
	}

}

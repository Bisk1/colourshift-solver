package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.angle.Single;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class SourceFour extends Source {
	
	private static Map<Angle, DirectionSet> directionsSets = ImmutableMap.of(
			Single.SINGLE, new DirectionSet(Direction.values())
			);

	public SourceFour(Colour colour) {
		super(colour);
	}

	@Override
	public Map<Angle, DirectionSet> getDirectionsSets() {
		return directionsSets;
	}

}

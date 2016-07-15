package colourshift.model.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import colourshift.model.SingleDirectionsDivision;
import colourshift.model.angle.Single;
import com.google.common.collect.ImmutableMap;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;

public class SourceOne extends Source {
	
	private static Map<Angle, SingleDirectionsDivision> directionsDivisionMap = ImmutableMap.of(
			Direction.LEFT, new SingleDirectionsDivision(Direction.LEFT),
			Direction.UP, new SingleDirectionsDivision(Direction.UP),
			Direction.RIGHT, new SingleDirectionsDivision(Direction.RIGHT),
			Direction.DOWN, new SingleDirectionsDivision(Direction.DOWN)
			);

	public SourceOne(Colour colour) {
		super(colour);
	}

	@Override
	public Map<Angle, SingleDirectionsDivision> getDirectionsDivisions() {
		return directionsDivisionMap;
	}

}

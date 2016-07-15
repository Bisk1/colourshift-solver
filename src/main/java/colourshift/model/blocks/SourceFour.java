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
import colourshift.model.angle.Single;
import colourshift.model.angle.TurnAngle;

public class SourceFour extends Source {
	
	private static Map<Angle, SingleDirectionsDivision> directionsDivisionMap = ImmutableMap.of(
			Single.SINGLE, new SingleDirectionsDivision(Direction.values())
			);

	public SourceFour(Colour colour) {
		super(colour);
	}

	@Override
	public Map<Angle, SingleDirectionsDivision> getDirectionsDivisions() {
		return directionsDivisionMap;
	}

}

package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.SingleDirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.angle.TurnAngle;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class SourceTurn extends Source {

    private Map<Angle, SingleDirectionsDivision> directionsDivisionMap = ImmutableMap.of(
            TurnAngle.LEFT_UP, new SingleDirectionsDivision(Direction.LEFT, Direction.UP),
            TurnAngle.UP_RIGHT, new SingleDirectionsDivision(Direction.UP, Direction.RIGHT),
            TurnAngle.RIGHT_DOWN, new SingleDirectionsDivision(Direction.RIGHT, Direction.DOWN),
            TurnAngle.LEFT_DOWN, new SingleDirectionsDivision(Direction.LEFT, Direction.DOWN)
    );

	public SourceTurn(Colour colour) {
		super(colour);
	}

	@Override
	public Map<Angle, SingleDirectionsDivision> getDirectionsDivisions() {
		return directionsDivisionMap;
	}

}

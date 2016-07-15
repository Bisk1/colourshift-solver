package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.angle.Single;
import colourshift.model.power.Power;
import colourshift.model.power.TargetPower;
import colourshift.solver.TargetSolver;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class Target extends Block {

	private TargetPower power;

	public Target(Colour requiredColour) {
		super();
		this.power = new TargetPower(requiredColour);
	}
	
	@Override
	public void updateReceived(Direction fromDirection, boolean updateEagerly) {
        if (fromDirection == angle) {
            Direction currentDirection = (Direction) angle;
            Colour colour = borderMap.getIncomingColourMix(new DirectionSet(currentDirection));
            power.setCurrent(colour);
		}
	}

	public boolean isActive() {
		return power.getCurrent() == power.getRequired();
	}

	@Override
	public Power getPower() {
		return power;
	}

    @Override
	public Map<Angle, DirectionsDivision> getDirectionsDivisions() {
		return ImmutableMap.of(
				Direction.LEFT, new DirectionsDivision(new DirectionSet(Direction.LEFT)),
				Direction.UP, new DirectionsDivision(new DirectionSet(Direction.UP)),
				Direction.RIGHT, new DirectionsDivision(new DirectionSet(Direction.RIGHT)),
				Direction.DOWN, new DirectionsDivision(new DirectionSet(Direction.DOWN))
				);
	}
	
}

package colourshift.model.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.power.Power;
import colourshift.model.power.TargetPower;
import com.google.common.collect.ImmutableMap;

public class Target extends Block {

	private TargetPower power;

	public Target(Colour requiredColour) {
		super();
		this.power = new TargetPower(requiredColour);
	}
	
	@Override
	public void updateReceived(Direction fromDirection, Colour colour, boolean updateEagerly) {
        if (fromDirection == angle) {
            fullUpdate();
		}
	}

	@Override
	public void fullUpdate() {
        Direction currentDirection = (Direction) angle;
        Colour colour = borderMap.getIncomingColourMix(new DirectionSet(currentDirection));
        power.setCurrent(colour);
	}

	@Override
	public void fullClear() {
        Direction currentDirection = (Direction) angle;
        updateReceived(currentDirection, Colour.GREY, false);
	}

	public boolean isActive() {
		return power.getCurrent() == power.getRequired();
	}

	@Override
	public Power getPower() {
		return power;
	}
	
}

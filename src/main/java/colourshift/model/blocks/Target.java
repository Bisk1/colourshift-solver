package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.power.TargetPower;

public class Target extends Block {

	private TargetPower power;

	public Target(Colour requiredColour) {
		super();
		this.power = new TargetPower(requiredColour);
	}

	@Override
	public void colourUpdateReceived(Direction fromDirection, boolean updateEagerly) {
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
	public TargetPower getPower() {
		return power;
	}

}

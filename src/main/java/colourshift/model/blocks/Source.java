package colourshift.model.blocks;

import java.util.Map;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.power.Power;
import colourshift.model.power.EmptyPower;

public abstract class Source extends Block {
	
	private Colour colour;
	
	public Source(Colour colour) {
		super();
		this.colour = colour;
	}
	
	protected abstract Map<Angle, DirectionSet> getDirectionsSets();

	public void activate() {
		DirectionSet currentDirectionSet = getDirectionsSets().get(angle);
		for (Direction toDirection : currentDirectionSet) {
			borderMap.send(toDirection, colour);
		}
	}

	@Override
	public void updateReceived(Direction fromDirection, Colour colour) {
		
	}
	
	@Override
	public Power getPower() {
		return EmptyPower.getInstance();
	}
}

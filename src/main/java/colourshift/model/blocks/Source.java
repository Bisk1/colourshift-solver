package colourshift.model.blocks;

import java.util.Map;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.power.Power;
import colourshift.model.power.EmptyPower;
import colourshift.model.power.SourcePower;

public abstract class Source extends Block {
	
	private SourcePower power;
	
	public Source(Colour colour) {
		super();
		this.power = new SourcePower(colour);
	}
	
	protected abstract Map<Angle, DirectionSet> getDirectionsSets();

	public void activate() {
		DirectionSet currentDirectionSet = getDirectionsSets().get(angle);
		for (Direction toDirection : currentDirectionSet) {
			borderMap.send(toDirection, power.getColour());
		}
	}

	@Override
	public void updateReceived(Direction fromDirection, Colour colour) {
		
	}
	
	@Override
	public Power getPower() {
		return power;
	}
}

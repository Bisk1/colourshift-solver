package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.SingleDirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.power.Power;
import colourshift.model.power.SourcePower;

import java.util.Map;

public abstract class Source extends Block {
	
	private SourcePower power;
	
	public Source(Colour colour) {
		super();
		this.power = new SourcePower(colour);
	}

	public abstract Map<Angle, SingleDirectionsDivision> getDirectionsDivisions();

    public void activate() {
        Colour colour = power.getColour();
        DirectionSet currentDirectionSet = getDirectionsDivisions().get(angle).getDirectionSet();
        for (Direction toDirection : currentDirectionSet) {
            borderMap.send(toDirection, colour);
        }
    }

	@Override
	public void updateReceived(Direction fromDirection, boolean updateEagerly) {
		
	}

	@Override
	public Power getPower() {
		return power;
	}

}

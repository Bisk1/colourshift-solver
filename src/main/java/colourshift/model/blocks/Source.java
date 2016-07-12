package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
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
	
	protected abstract Map<Angle, DirectionSet> getDirectionsSets();

	private void activate() {
        send(power.getColour());
	}

    private void deactivate() {
        send(Colour.GREY);
    }

    public void send(Colour colour) {
        DirectionSet currentDirectionSet = getDirectionsSets().get(angle);
        for (Direction toDirection : currentDirectionSet) {
            borderMap.send(toDirection, colour);
        }
    }

	@Override
	public void updateReceived(Direction fromDirection, Colour colour) {
		
	}

    @Override
    public void fullUpdate() {
        activate();
    }

    @Override
    public void fullClear() {
        deactivate();
    }

	@Override
	public Power getPower() {
		return power;
	}

}

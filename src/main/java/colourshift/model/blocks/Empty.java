package colourshift.model.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.angle.Single;
import colourshift.model.power.Power;
import colourshift.model.power.EmptyPower;
import com.google.common.collect.ImmutableMap;

public class Empty extends Block {

	@Override
	public void colourUpdateReceived(Direction fromDirection, boolean updateEagerly) {
		
	}

	@Override
	public Power getPower() {
		return EmptyPower.getInstance();
	}

}

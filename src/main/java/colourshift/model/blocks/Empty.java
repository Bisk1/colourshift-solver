package colourshift.model.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.angle.Single;
import colourshift.model.power.Power;
import colourshift.model.power.EmptyPower;
import com.google.common.collect.ImmutableMap;

public class Empty extends Block {

	private Map<Angle, ? extends DirectionsDivision> directionsDivisionMap =
			ImmutableMap.of(Single.SINGLE, new DirectionsDivision(new DirectionSet()));
	@Override
	public void updateReceived(Direction fromDirection, boolean updateEagerly) {
		
	}

	@Override
	public Power getPower() {
		return EmptyPower.getInstance();
	}

	@Override
	public Map<Angle, ? extends DirectionsDivision> getDirectionsDivisions() {
		return directionsDivisionMap;
	}
	
}

package colourshift.model.power;

import colourshift.model.Colour;

import java.util.Arrays;
import java.util.List;

public class SourcePower implements Power {

	private Colour colour;
	
	public SourcePower(Colour colour) {
		this.colour = colour;
	}
	
	public Colour getColour() {
		return colour;
	}

	@Override
	public Power copy() {
		return new SourcePower(colour);
	}

	public void reset() {
	}

	@Override
	public List<Colour> toColoursList() {
		return Arrays.asList(colour);
	}
}

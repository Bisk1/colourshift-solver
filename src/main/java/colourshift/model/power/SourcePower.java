package colourshift.model.power;

import colourshift.model.Colour;

public class SourcePower {

	private Colour colour;
	
	public SourcePower(Colour colour) {
		this.colour = colour;
	}
	
	public Colour getColour() {
		return colour;
	}
}

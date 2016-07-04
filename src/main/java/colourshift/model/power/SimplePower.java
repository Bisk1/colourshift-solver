package colourshift.model.power;

import java.util.Arrays;
import java.util.List;

import colourshift.model.Colour;

public class SimplePower implements Power {
	
	private Colour colour;
	
	public SimplePower() {
		this(Colour.GREY);
	}
	
	public SimplePower(Colour colour) {
		this.colour = colour;
	}
	
	public void setColour(Colour colour) {
		this.colour = colour;
	}
	
	public Colour getColour(Colour colour) {
		return colour;
	}
	
	@Override
	public Power copy() {
		return new SimplePower(colour);
	}
	
	@Override
	public List<Colour> toColoursList() {
		return Arrays.asList(colour);
	}

}

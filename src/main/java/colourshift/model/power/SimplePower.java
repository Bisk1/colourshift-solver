package colourshift.model.power;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
	
	public Colour getColour() {
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

	@Override
	public boolean equals(Object other) {
		if (other instanceof SimplePower) {
			return Objects.equals(colour, ((SimplePower) other).getColour());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return colour.hashCode();
	}

}

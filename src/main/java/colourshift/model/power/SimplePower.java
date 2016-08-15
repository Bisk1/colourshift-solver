package colourshift.model.power;

import colourshift.model.Colour;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SimplePower implements Power {
	
	private Colour colour;
	
	public SimplePower() {
        this.colour = Colour.GREY;
	}

	public void setColour(Colour colour) {
		this.colour = colour;
	}
	
	public Colour getColour() {
		return colour;
	}
	
	@Override
	public Power copy() {
		SimplePower newPower = new SimplePower();
        newPower.setColour(colour);
        return newPower;
	}

	@Override
	public void reset() {
		this.colour = Colour.GREY;
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

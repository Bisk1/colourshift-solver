package colourshift.model.power;

import java.util.Arrays;
import java.util.List;

import colourshift.model.Colour;

public class DoubleTurnPower implements Power {
	private Colour horizontal;
	private Colour vertical;
	
	public DoubleTurnPower() {
		this.horizontal = Colour.GREY;
		this.vertical = Colour.GREY;
	}
	
	@Override
	public Power copy() {
		DoubleTurnPower copy = new DoubleTurnPower();
		copy.setHorizontal(horizontal);
		copy.setVertical(vertical);
		return copy;
	}

	@Override
	public List<Colour> toColoursList() {
		return Arrays.asList(horizontal, vertical);
	}
	
	public void setHorizontal(Colour horizontal) {
		this.horizontal = horizontal;
	}
	
	public void setVertical(Colour vertical) {
		this.vertical = vertical;
	}
}
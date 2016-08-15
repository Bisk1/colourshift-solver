package colourshift.model.power;

import colourshift.model.Colour;

import java.util.Arrays;
import java.util.List;

public class HalfFourPower implements Power {
	private Colour horizontal;
	private Colour vertical;

	public HalfFourPower() {
		this.horizontal = Colour.GREY;
		this.vertical = Colour.GREY;
	}

	@Override
	public void reset() {
		this.horizontal = Colour.GREY;
		this.vertical = Colour.GREY;
	}

	@Override
	public Power copy() {
		HalfFourPower copy = new HalfFourPower();
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
package colourshift.model.power;

import colourshift.model.Colour;

import java.util.Arrays;
import java.util.List;

public class DoubleTurnPower implements Power {
	private Colour left;
	private Colour right;

	public DoubleTurnPower() {
		this.left = Colour.GREY;
		this.right = Colour.GREY;
	}

	@Override
	public void reset() {
		this.left = Colour.GREY;
		this.right = Colour.GREY;
	}

	@Override
	public Power copy() {
		DoubleTurnPower copy = new DoubleTurnPower();
		copy.setLeft(left);
		copy.setRight(right);
		return copy;
	}

	@Override
	public List<Colour> toColoursList() {
		return Arrays.asList(left, right);
	}

	public void setLeft(Colour left) {
		this.left = left;
	}

	public void setRight(Colour right) {
		this.right = right;
	}
}
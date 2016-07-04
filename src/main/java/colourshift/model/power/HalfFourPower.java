package colourshift.model.power;

import java.util.Arrays;
import java.util.List;

import colourshift.model.Colour;

public class HalfFourPower implements Power {
	private Colour left;
	private Colour right;
	
	public HalfFourPower() {
		this.left = Colour.GREY;
		this.right = Colour.GREY;
	}
	
	@Override
	public Power copy() {
		HalfFourPower copy = new HalfFourPower();
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
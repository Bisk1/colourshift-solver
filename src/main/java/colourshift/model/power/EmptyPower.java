package colourshift.model.power;

import colourshift.model.Colour;

import java.util.Arrays;
import java.util.List;

public class EmptyPower implements Power {

	private static EmptyPower INSTANCE = new EmptyPower();
	
	private EmptyPower() {}
	
	public static EmptyPower getInstance() {
		return INSTANCE;
	}

	@Override
	public void reset() {

	}

	@Override
	public Power copy() {
		return this;
	}

	@Override
	public List<Colour> toColoursList() {
		return Arrays.asList();
	}
}

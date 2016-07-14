package colourshift.model.power;

import java.util.Arrays;
import java.util.List;

import colourshift.model.Colour;

public class TargetPower implements Power {

	private Colour required;
	private Colour current;
	
	public TargetPower(Colour required) {
		this.required = required;
		this.current = Colour.GREY;
	}
	
	public void setCurrent(Colour current) {
		this.current = current;
	}

	public Colour getRequired() {
		return required;
	}

	public Colour getCurrent() {
		return current;
	}

	@Override
	public Power copy() {
		TargetPower newPower = new TargetPower(required);
		newPower.setCurrent(current);
		return newPower;
	}

	public void reset() {
		this.current = Colour.GREY;
	}

	@Override
	public List<Colour> toColoursList() {
		return Arrays.asList(required, current);
	}
}

package colourshift.model.power;

import java.util.List;

import colourshift.model.Colour;

public interface Power extends Cloneable {
	public Power copy();
	public List<Colour> toColoursList();
}

package colourshift.model.power;

import java.io.Serializable;
import java.util.List;

import colourshift.model.Colour;

public interface Power extends Serializable {
	public Power copy();
	public List<Colour> toColoursList();
}

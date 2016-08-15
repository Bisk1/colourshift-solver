package colourshift.model.power;

import colourshift.model.Colour;

import java.io.Serializable;
import java.util.List;

public interface Power extends Serializable {
	Power copy();
	List<Colour> toColoursList();
	void reset();
}

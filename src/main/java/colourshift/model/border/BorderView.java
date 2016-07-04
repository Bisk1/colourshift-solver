package colourshift.model.border;

import java.util.Optional;

import colourshift.model.Colour;
import colourshift.model.blocks.Block;

public class BorderView {
	
	private Border border;
	private Block block;
	
	public BorderView(Border border, Block block) {
		this.border = border;
		this.block = block;
	}
	
	public void send(Colour colour) {
		border.send(block, colour);
	}

	public Optional<Colour> getIncomingColour() {
		return border.getIncomingColour(block);
	}
}

package colourshift.model.border;

import colourshift.model.Colour;
import colourshift.model.blocks.Block;

import java.io.Serializable;

public class BorderView implements Serializable {
	
	private Border border;
	private Block block;
	
	public BorderView(Border border, Block block) {
		this.border = border;
		this.block = block;
	}
	
	public void send(Colour colour) {
		border.send(block, colour);
	}

	public Colour getColour() {
		return border.getColour();
	}

	public void changeBlock(Block newBlock) {
        border.changeBlock(block, newBlock);
        block = newBlock;
	}
}

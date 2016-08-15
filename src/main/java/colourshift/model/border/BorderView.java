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
	
	public void sendColour(Colour colour) {
		border.sendColour(block, colour);
	}

	public void changeBlock(Block newBlock) {
        border.changeBlock(block, newBlock);
        block = newBlock;
	}

	public Colour getIncomingColour() {
		return border.getIncomingColour(block);
	}

    public void reset() {
        border.reset(block);
    }

	public Block getNeighbour() {
		return border.otherBlock(block);
	}

	public void updateBorderStatus(BorderStatus borderStatus) {
		border.updateBorderStatus(block, borderStatus);
	}

	public BorderStatus getBorderStatus() {
		return border.getBorderStatus();
	}
}

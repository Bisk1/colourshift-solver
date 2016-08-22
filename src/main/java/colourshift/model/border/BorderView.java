package colourshift.model.border;

import colourshift.model.Colour;
import colourshift.model.blocks.Block;

import java.io.Serializable;
import java.util.Optional;

public class BorderView implements Serializable {
	private static long serialVersionUID = 0L;
	
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

	public Optional<Colour> getColour() {
		return border.getColour();
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

	public void updateBorderStatus(BorderStatus borderStatus, Colour colour) {
		border.updateBorderStatus(block, borderStatus, colour);
	}

	public BorderStatus getBorderStatus() {
		return border.getBorderStatus();
	}

	public Block getBorderStatusAuthor() {
		return border.getBorderStatusAuthor();
	}

	public boolean canReceive() {
		return border.canReceiveBy(block);
	}

	public boolean mustSend() {
		return border.getBorderStatus() == BorderStatus.MANDATORY;
	}

	public boolean canReceive(Colour colour) {
		return border.canReceiveBy(block, colour);
	}
}

package colourshift.model.border;

import colourshift.model.Colour;
import colourshift.model.blocks.Block;

import java.io.Serializable;

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

	public Block getBorderStatusAuthor() {
		return border.getBorderStatusAuthor();
	}

	/**
	 * Check if this border can receive colour from this side
	 * @return false only if it is certain that this side cannot receive colour
     */
	public boolean canReceive() {
		switch (border.getBorderStatus()) {
			case INDIFFERENT:
				return false;
			case CANNOT_SEND:
			case MANDATORY:
				// if it's the other block that set this status, then this side cannot receive
				// otherwise, it is still unknown
				return border.getBorderStatusAuthor() == block;
			default:
				return true;
		}
	}

	public boolean mustSend() {
		return border.getBorderStatus() == BorderStatus.MANDATORY;
	}

	public boolean canSend() {
		switch (border.getBorderStatus()) {
			case INDIFFERENT:
				return false;
			case CANNOT_SEND:
			case MANDATORY:
				// if it's the other block that set this status, then this side can send
				// otherwise, it is still unknown
				return border.getBorderStatusAuthor() != block;
			default:
				return true;
		}
	}
}

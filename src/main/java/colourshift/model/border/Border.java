package colourshift.model.border;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.blocks.Block;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Border implements Serializable {

	private BorderSide side1;
    private BorderSide side2;
    private BorderStatus borderStatus = BorderStatus.UNKNOWN;
    // Needed to tell the difference: if one side of the border request MANDATORY border, the other one sometimes
    // can't do the same
    private Block borderStatusAuthor = null;
    // the value is the block that sent event
    Map<Colour, Block> providedColours = Maps.newHashMap();
    Map<Colour, Block> mandatoryColours = Maps.newHashMap();
    
    public Block otherBlock(Block block) {
        return otherSide(block).block;
    }

    private class BorderSide implements Serializable {
        public Direction direction;
        public Block block;
        public Set<Colour.Component> components;
        public BorderSide(Direction direction, Block block) {
            this.direction = direction;
            this.block = block;
            this.components = Sets.newHashSet();
        }
    }

	public Border(Block neighbour1, Direction direction1, Block neighbour2) {
		this.side1 = new BorderSide(direction1, neighbour1);
        this.side2 = new BorderSide(direction1.opposite(), neighbour2);
	}

	public BorderView getView(Block block) {
		return new BorderView(this, block);
	}

    private BorderSide side(Block block) {
        return block == side1.block ? side1 : side2;
    }

	private BorderSide otherSide(Block block) {
		return block == side1.block ? side2 : side1;
	}

    /**
     * There are 3 options:
     * 1) All the incoming components are already received by the other side -> ignore
     * 2) The incoming components set is the same as the previously incoming components for this side -> ignore
     * 3) Otherwise update set and propagate
     * @param fromBlock
     * @param colour
     */
	public void sendColour(Block fromBlock, Colour colour) {
        BorderSide incomingSide = side(fromBlock);
        BorderSide otherSide = otherSide(fromBlock);
        Colour oldOutgoingColour = getIncomingColour(otherSide.block);
        Set<Colour.Component> incomingComponents = colour.getComponents();
        if (incomingComponents.equals(incomingSide.components)) {
            return;
        }
        incomingSide.components = incomingComponents;
        Colour newOutgoingColour = getIncomingColour(otherSide.block);
        if (oldOutgoingColour != newOutgoingColour) {
            otherSide.block.colourUpdateReceived(incomingSide.direction, false);
        }
	}

	public void changeBlock(Block oldBlock, Block newBlock) {
		if (oldBlock == side1.block) {
            side1.block = newBlock;
		} else {
            side2.block = newBlock;
		}
	}

    public Colour getIncomingColour(Block toBlock) {
        BorderSide fromSide = otherSide(toBlock);
        return Colour.fromComponents(fromSide.components);
    }

    public Optional<Colour> getColour() {
        switch (borderStatus) {
            case INDIFFERENT:
            case UNKNOWN:
            case CANNOT_SEND:
                return Optional.empty();
            case MANDATORY:
                return Optional.of(mandatoryColours.keySet().stream().reduce(Colour.GREY, Colour::plus));
            case RECEIVE:
                return Optional.of(providedColours.keySet().stream().reduce(Colour.GREY, Colour::plus));
            default:
                throw new RuntimeException("Unsupported status: " + borderStatus);

        }
    }

    public void reset(Block fromBlock) {
        side(fromBlock).components = Sets.newHashSet();
    }

    public void updateBorderStatus(Block fromBlock, BorderStatus newBorderStatus) {
        if (newBorderStatus.getStrength() > borderStatus.getStrength()) {
            borderStatus = newBorderStatus;
            borderStatusAuthor = fromBlock;
            otherBlock(fromBlock).statusUpdateReceived();
        }
    }

    public void updateBorderStatus(Block fromBlock, BorderStatus newBorderStatus, Colour colour) {
        switch (newBorderStatus) {
            case RECEIVE:
                providedColours.put(colour, fromBlock);
                break;
            case MANDATORY:
                mandatoryColours.put(colour, fromBlock);
                break;
            default:
                throw new RuntimeException("You can't send colour update with any border status other than" +
                        "MANDATORY or RECEIVE");
        }
        updateBorderStatus(fromBlock, newBorderStatus);
    }

    /**
     * Check if this border can receive colour from this side
     * @return false only if it is certain that this side cannot receive colour
     */
    public boolean canReceiveBy(Block block) {
        switch (borderStatus) {
            case INDIFFERENT:
                return false;
            case CANNOT_SEND:
            case MANDATORY:
                // if it's the other block that set this status, then this side cannot receive
                // otherwise, it is still unknown
                return borderStatusAuthor == block;
            default:
                return true;
        }
    }


    public boolean canReceiveBy(Block block, Colour colour) {
        return borderStatus == BorderStatus.RECEIVE
                && providedColours.containsKey(colour)
                && providedColours.get(colour) != block;
    }

    public BorderStatus getBorderStatus() {
        return borderStatus;
    }

    public Block getBorderStatusAuthor() {
        return borderStatusAuthor;
    }

}

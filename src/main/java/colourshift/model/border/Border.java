package colourshift.model.border;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.Straight;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Set;

public class Border implements Serializable {

	private BorderSide side1;
    private BorderSide side2;

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
	public void send(Block fromBlock, Colour colour) {
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
            otherSide.block.updateReceived(incomingSide.direction, false);
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

    public void reset(Block fromBlock) {
        side(fromBlock).components = Sets.newHashSet();
    }
}

package colourshift.model.border;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.blocks.Block;
import com.google.common.collect.BiMap;

import java.util.Optional;

public class Border {

	private Direction direction1;
	private Block neighbour1;
	private Direction direction2;
	private Block neighbour2;

	// Flow is mapping of the colour being sent through the border to the block which is sending the colour
	private BiMap<Colour, Block> flows;

	public Border(Block neighbour1, Direction direction1, Block neighbour2) {
		this.direction1 = direction1;
		this.neighbour1 = neighbour1;
		this.direction2 = direction1.opposite();
		this.neighbour2 = neighbour2;
	}

	public BorderView getView(Block block) {
		return new BorderView(this, block);
	}

	private Block otherNeighbour(Block neighbour) {
		return neighbour == neighbour1 ? neighbour2 : neighbour1;
	}

	private Direction neighbourDirection(Block neighbour) {
		return neighbour == neighbour1 ? direction1 : direction2;
	}

	public void send(Block fromBlock, Colour colour) {
		if (!flows.containsKey(colour)) {
			flows.put(colour, fromBlock);
			otherNeighbour(fromBlock).updateReceived(neighbourDirection(fromBlock),colour);
		}
	}

	public Optional<Colour> getIncomingColour(Block toBlock) {
		Block fromBlock = otherNeighbour(toBlock);
		if (flows.containsValue(fromBlock)) {
			return Optional.of(flows.inverse().get(fromBlock));
		} else {
			return Optional.empty();
		}
	}

	public void changeBlock(Block oldBlock, Block newBlock) {
		if (oldBlock == neighbour1) {
			neighbour1 = newBlock;
		} else {
			neighbour2 = newBlock;
		}
	}
}

package colourshift.model.border;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.collect.BiMap;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.blocks.Block;

public class Border {
	
	private SortedMap<Block, Direction> neighbours;
	
	// Flow is mapping of the colour being sent through the border to the block which is sending the colour
	private BiMap<Colour, Block> flows;
	
	public Border(Block neighbour1, Direction direction1, Block neighbour2) {
		this.neighbours = new TreeMap<>();
		neighbours.put(neighbour1, direction1);
		neighbours.put(neighbour2, direction1.opposite());
	}
	
	public BorderView getView(Block block) {
		return new BorderView(this, block);
	}
	
	private Block otherNeighbour(Block neighbour) {
		if (neighbour == neighbours.firstKey())
			return neighbours.lastKey();
		if (neighbour == neighbours.lastKey()) {
			return neighbours.firstKey();
		}
		throw new RuntimeException("Invalid border - colour sent from unrecognized neighbour");
	}

	public void send(Block fromBlock, Colour colour) {
		if (!flows.containsKey(colour)) {
			flows.put(colour, fromBlock);
			otherNeighbour(fromBlock).updateReceived(neighbours.get(fromBlock), colour);
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
}

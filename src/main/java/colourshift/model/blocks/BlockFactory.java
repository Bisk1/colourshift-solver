package colourshift.model.blocks;

import java.util.Optional;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import colourshift.model.Colour;


public class BlockFactory {

    private ImmutableSet<BlockType> blockTypesThatRequireColour = Sets.immutableEnumSet(BlockType.TARGET, BlockType.SOURCE_ONE,
			BlockType.SOURCE_STRAIGHT, BlockType.SOURCE_TURN, BlockType.SOURCE_THREE, BlockType.SOURCE_FOUR);

	private SourceManager sourceManager;
	private TargetManager targetManager;

	public BlockFactory(SourceManager sourceManager, TargetManager targetManager) {
		this.sourceManager = sourceManager;
		this.targetManager = targetManager;
	}

	public Block createEmpty() {
		return createAndInitBlock(BlockType.EMPTY, Optional.empty());
	}

	public Block createAndInitBlock(BlockType blockType, Optional<Colour> colour) {
		Block block = createBlock(blockType, colour);
		
		if (block instanceof Source) {
			sourceManager.add((Source)block);
		} else if (block instanceof Target) {
			targetManager.add((Target)block);
		}
		
		return block;
	}

	private Block createBlock(BlockType blockType, Optional<Colour> colour) {
		Preconditions.checkArgument(colour.isPresent() || !blockTypesThatRequireColour.contains(blockType));
		switch (blockType) {
		case EMPTY:
			return Empty.getInstance();
		case TARGET:
			return new Target(colour.get());
		case STRAIGHT:
			return new Straight();
		case TURN:
			return new Turn();
		case THREE:
			return new Three();
		case DOUBLE_TURN:
			return new DoubleTurn();
		case HALF_FOUR:
			return new HalfFour();
		case FULL_FOUR:
			return new FullFour();
		case SOURCE_ONE:
			return new SourceOne(colour.get());
		case SOURCE_STRAIGHT:
			return new SourceStraight(colour.get());
		case SOURCE_TURN:
			return new SourceTurn(colour.get());
		case SOURCE_THREE:
			return new SourceThree(colour.get());
		case SOURCE_FOUR:
			return new SourceFour(colour.get());
		default:
			throw new RuntimeException("Unkown block type: " + blockType);
		}
	}
}

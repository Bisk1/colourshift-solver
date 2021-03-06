package colourshift.model.blocks;

import colourshift.model.Colour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
public class BlockFactory implements Serializable {

	@Autowired
	private TargetManager targetManager;

	@Autowired
	private SourceManager sourceManager;

	public BlockFactory(TargetManager targetManager, SourceManager sourceManager) {
		this.targetManager = targetManager;
        this.sourceManager = sourceManager;
	}

	public Block createEmpty() {
		return createAndInitBlock(BlockType.EMPTY, Optional.empty());
	}

	public Block createAndInitBlock(BlockType blockType, Optional<Colour> colour) {
		Block block = createBlock(blockType, colour);

		if (block instanceof Target) {
			targetManager.add((Target)block);
		} else if (block instanceof Source) {
            sourceManager.add((Source) block);
        }
		return block;
	}

    public void deregister(Block block) {
        if (block instanceof Target) {
            targetManager.remove((Target)block);
        } else if (block instanceof Source) {
            sourceManager.remove((Source) block);
        }
    }

	private Block createBlock(BlockType blockType, Optional<Colour> colour) {
		switch (blockType) {
		case EMPTY:
			return new Empty();
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
			throw new RuntimeException("Unknown block type: " + blockType);
		}
	}

}

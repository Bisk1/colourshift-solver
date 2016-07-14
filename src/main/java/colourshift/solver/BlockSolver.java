package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.Source;
import colourshift.model.blocks.Target;
import colourshift.model.blocks.TransitiveBlock;

public abstract class BlockSolver {
    private Block block;
    public BlockSolver(Block block) {
        this.block = block;
    }

    public static BlockSolver create(Block block) {
        if (block instanceof TransitiveBlock) {
            return new TransitiveBlockSolver((TransitiveBlock)block);
        } else if (block instanceof Source) {
            return new SourceSolver((Source)block);
        } else {
            return new TargetSolver((Target) block);
        }

    }

    public void applyInitialRules() {
        if (block.getBorderMap().size() < 4) {
            reduceAnglesForEdgeBlock();
        }
    }

    protected void reduceAnglesForEdgeBlock() {
        for (Direction direction : Direction.values()) {
            if (!block.getBorderMap().contains(direction)) {
                reduceAnglesForUnusedBorder(direction);
            }
        }
    }

    protected abstract void reduceAnglesForUnusedBorder(Direction direction);
}

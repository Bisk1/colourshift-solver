package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.Target;

import java.util.Optional;

public class TargetSolver extends BlockSolver {
    private Target block;

    public TargetSolver(Target block) {
        super(block);
        this.block = block;
    }

    @Override
    protected void reduceAnglesForEdgeBlock() {
        for (Direction direction : Direction.values()) {
            if (!block.getBorderMap().contains(direction)) {
                reduceAnglesForUnusedBorder(direction);
            }
        }
    }

    @Override
    public void applyInitialRules() {
        super.applyInitialRules();
        reduceAnglesForTargetNeighbours();
    }

    private void reduceAnglesForTargetNeighbours() {
        for (Direction direction : Direction.values()) {
            Optional<Block> neighbour = block.getBorderMap().getNeighbour(direction);
            if (neighbour.isPresent() && neighbour.get() instanceof Target) {
                reduceAnglesForUnusedBorder(direction);
            }
        }
    }

    @Override
    protected void reduceAnglesForUnusedBorder(Direction unusedDirection) {
        block.forbidAngle(unusedDirection);
    }
}

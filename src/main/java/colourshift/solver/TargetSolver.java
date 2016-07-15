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
    public void applyInitialRules() {
        super.applyInitialRules();
        reduceAnglesForTargetNeighbours();
    }

    private void reduceAnglesForTargetNeighbours() {
        for (Direction direction : Direction.values()) {
            Optional<Block> neighbour = block.getBorderMap().getNeighbour(direction);
            if (neighbour.isPresent() && neighbour.get() instanceof Target) {
                block.forbidAngles(findAnglesUsedByDirection(direction));
            }
        }
    }
}

package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.Target;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    protected Set<Direction> findUnusedDirections() {
        Set<Direction> unusedDirections = new HashSet<>(Arrays.asList(Direction.values()));
        for (Angle angle : block.getFeasibleAngles()) {
            Direction direction = (Direction) angle;
            unusedDirections.remove(direction);
        }
        return unusedDirections;
    }

    @Override
    protected void reduceAnglesForUnusedBorder(Direction unusedDirection) {
        block.forbidAngle(unusedDirection);
    }
}

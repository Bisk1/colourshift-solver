package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.blocks.*;
import colourshift.model.border.BorderStatus;
import colourshift.model.border.BorderView;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

public abstract class BlockSolver implements Serializable {
    private Block block;
    public BlockSolver(Block block) {
        this.block = block;
    }

    public static BlockSolver create(Block block) {
        if (block instanceof TransitiveBlock) {
            return new TransitiveBlockSolver((TransitiveBlock)block);
        } else if (block instanceof Source) {
            return new SourceSolver((Source)block);
        } else if (block instanceof Target) {
            return new TargetSolver((Target) block);
        } else {
            return new EmptySolver((Empty) block);
        }

    }

    public void applyInitialRules() {
        if (block.getBorderMap().size() < 4) {
            reduceAnglesForEdgeBlock();
        }
        statusUpdated();
    }

    protected void reduceAnglesForEdgeBlock() {
        for (Direction direction : Direction.values()) {
            if (!block.getBorderMap().contains(direction)) {
                reduceAnglesForUnusedBorder(direction);
            }
        }
    }

    public void statusUpdated() {
        reduceAngles();
        propagateBorder();
    }

    private void reduceAngles() {
        for (Direction direction : Direction.values()) {
            Optional<BorderView> borderView = block.getBorderMap().getBorderView(direction);
            if (borderView.isPresent() && borderView.get().getBorderStatus().equals(BorderStatus.UNUSED)) {
                reduceAnglesForUnusedBorder(direction);
            }
        }
    }

    private void propagateBorder() {
        Set<Direction> unusedDirections = findUnusedDirections();
        for (Direction unusedDirection : unusedDirections) {
            block.getBorderMap().getBorderView(unusedDirection)
                    .ifPresent(borderView -> borderView.updateBorderStatus(BorderStatus.UNUSED));
        }
    }

    protected abstract Set<Direction> findUnusedDirections();

    protected abstract void reduceAnglesForUnusedBorder(Direction direction);

}

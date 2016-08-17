package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.Target;
import colourshift.model.border.BorderStatus;
import colourshift.model.border.BorderView;

import java.util.Optional;

public class TargetSolver extends BlockSolver {
    private static long serialVersionUID = 0L;

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
                tryToForbidAnglesWithBorder(direction);
            }
        }
    }

    @Override
    protected void propagateBorder() {
        super.propagateBorder();
        if (block.getFeasibleAngles().size() == 1) {
            setTheOnlyUsedBorderAsMandatory();
        }
        setUnknownBordersToCannotSend();
    }

    private void setTheOnlyUsedBorderAsMandatory() {
        Direction mandatoryDirection = (Direction) block.getFeasibleAngles().iterator().next();
        block.getBorderMap().getBorderView(mandatoryDirection).get().updateBorderStatus(BorderStatus.MANDATORY);
    }

    private void setUnknownBordersToCannotSend() {
        for (Direction direction : block.getBorderMap().getExistingBordersDirections()) {
            BorderView borderView = block.getBorderMap().getBorderView(direction).get();
            if (borderView.getBorderStatus() == BorderStatus.UNKNOWN) {
                borderView.updateBorderStatus(BorderStatus.CANNOT_SEND);
            }
        }
    }
}

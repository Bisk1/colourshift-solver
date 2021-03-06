package colourshift.solver;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.Target;
import colourshift.model.border.BorderRequirement;
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
        reduceAnglesForTargetNeighbours();
        super.applyInitialRules();
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
    protected void reduceAngles() {
        super.reduceAngles();
        if (!block.isFixed()) {
            fixAngleIfRequiredColourProvidedAnywhere();
        }
    }

    private void fixAngleIfRequiredColourProvidedAnywhere() {
        for (Angle angle : block.getFeasibleAngles()) {
            Direction direction = (Direction) angle;
            Optional<BorderView> borderView = block.getBorderMap().getBorderView(direction);
            if (borderView.isPresent()) {
                Optional<Colour> providedColour = borderView.get().provided();
                if (providedColour.isPresent() && providedColour.get() == block.getPower().getRequired()) {
                    block.fixAngle(direction);
                }
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
        Optional<BorderView> mandatoryBorderView = block.getBorderMap().getBorderView(mandatoryDirection);
        if (!mandatoryBorderView.isPresent()) { // this can happen for edge block on unsolvable board
            throw new UnsolvableException();
        }
        mandatoryBorderView.get().updateBorderStatus(BorderRequirement.mustSend(block.getPower().getRequired()));
    }

    private void setUnknownBordersToCannotSend() {
        for (Direction direction : block.getBorderMap().getExistingBordersDirections()) {
            BorderView borderView = block.getBorderMap().getBorderView(direction).get();
            if (borderView.getBorderRequirement().getBorderStatus() == BorderStatus.UNKNOWN) {
                borderView.updateBorderStatus(BorderRequirement.cannotSend());
            }
        }
    }
}

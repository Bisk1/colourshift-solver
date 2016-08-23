package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.*;
import colourshift.model.border.BorderRequirement;
import colourshift.model.border.BorderStatus;
import colourshift.model.border.BorderView;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BlockSolver implements Serializable {
    private static long serialVersionUID = 0L;

    private Block block;
    public BlockSolver(Block block) {
        this.block = block;
    }

    public static BlockSolver create(Block block) {
        if (block instanceof Target) {
            return new TargetSolver((Target) block);
        } else if (block instanceof TransitiveBlock) {
            return new TransitiveBlockSolver((TransitiveBlock) block);
        } else {
            return new BlockSolver(block);
        }

    }

    public void applyInitialRules() {
        if (block.getBorderMap().size() < 4 && block.getFeasibleAngles().size() > 1) {
            reduceAnglesForEdgeBlock();
        }
        bordersUpdated();
    }

    protected void reduceAnglesForEdgeBlock() {
        for (Direction direction : Direction.values()) {
            if (!block.getBorderMap().contains(direction)) {
                tryToForbidAnglesWithBorder(direction);
            }
        }
    }

    public void bordersUpdated() {
        for (Direction direction : block.getBorderMap().getExistingBordersDirections()) {
            BorderView borderView = block.getBorderMap().getBorderView(direction).get();
        }
        reduceAngles();
        propagateBorder();
    }

    protected void reduceAngles() {
        if (block.getFeasibleAngles().size() > 1) {
            reduceAnglesWithIndifferentBorders();
        }
        reduceAnglesWithoutMandatoryBorders();
    }

    protected void propagateBorder() {
        setIndifferentBorders();
    }

    private void reduceAnglesWithIndifferentBorders() {
        for (Direction direction : Direction.values()) {
            Optional<BorderView> borderView = block.getBorderMap().getBorderView(direction);
            if (borderView.isPresent() && borderView.get().getBorderRequirement().getBorderStatus() == BorderStatus.INDIFFERENT) {
                tryToForbidAnglesWithBorder(direction);
            }
        }
    }

    private void reduceAnglesWithoutMandatoryBorders() {
        for (Direction direction : Direction.values()) {
            Optional<BorderView> borderView = block.getBorderMap().getBorderView(direction);
            if (borderView.isPresent() && borderView.get().getBorderRequirement().getBorderStatus() == BorderStatus.MUST_SEND) {
                forbidAnglesWithoutBorder(direction);
            }
        }
    }

    /**
     * Unused border is the one that does not belong to a direction division for any feasible angle.
     */
    private void setIndifferentBorders() {
        Set<Direction> unusedDirections = findUnusedDirections();
        for (Direction unusedDirection : unusedDirections) {
            block.getBorderMap().getBorderView(unusedDirection)
                    .ifPresent(borderView -> borderView.updateBorderStatus(BorderRequirement.indifferent()));
        }
    }

    private Set<Direction> findUnusedDirections() {
        Set<Direction> usedDirections = new HashSet<>();
        for (Angle angle : block.getFeasibleAngles()) {
            DirectionsDivision directionsDivision = block.getDirectionsDivisions().get(angle);
            for (Direction direction: directionsDivision.getDirections()) {
                usedDirections.add(direction);
            }
        }
        Set<Direction> unusedDirections = new HashSet<>(Arrays.asList(Direction.values()));
        unusedDirections.removeAll(usedDirections);
        return unusedDirections;
    }

    // If there is only one angle left, it will be left without invalidating border
    protected void tryToForbidAnglesWithBorder(Direction unusedDirection) {
        Set<Angle> feasibleAngles = Sets.newHashSet(block.getFeasibleAngles());
        for (Angle angle : feasibleAngles) {
            Set<Direction> directionsSet = block.getDirectionsDivisions().get(angle).getDirections();
            if (directionsSet.contains(unusedDirection) && block.getFeasibleAngles().size() > 1) {
                block.forbidAngle(angle);
            }
        }
    }

    private void forbidAnglesWithoutBorder(Direction unusedDirection) {
        Set<Angle> feasibleAngles = Sets.newHashSet(block.getFeasibleAngles());
        for (Angle angle : feasibleAngles) {
            Set<Direction> directionsSet = block.getDirectionsDivisions().get(angle).getDirections();
            if (!directionsSet.contains(unusedDirection)) {
                block.forbidAngle(angle);
            }
        }
    }

}

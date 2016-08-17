package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.*;
import colourshift.model.border.BorderStatus;
import colourshift.model.border.BorderView;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class BlockSolver implements Serializable {
    private static long serialVersionUID = 0L;

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
        bordersUpdated();
    }

    protected void reduceAnglesForEdgeBlock() {
        for (Direction direction : Direction.values()) {
            if (!block.getBorderMap().contains(direction)) {
                forbidAnglesWithBorder(direction);
            }
        }
    }

    public void bordersUpdated() {
        System.out.println("bordersUpdated");
        System.out.println(block);
        System.out.println(block.getAngle());
        System.out.println(block.getFeasibleAngles().size());
        reduceAngles();
        propagateBorder();
    }

    private void reduceAngles() {
        reduceAnglesWithIndifferentBorders();
        reduceAnglesWithoutMandatoryBorders();
        reduceAnglesWithMandatoryBordersButWithoutBorderThatCanReceive();
    }

    protected void propagateBorder() {
        setUnusedBorderAsIndifferent();
        setMandatoryBorders();
        setCannotSendBorderIfAllOtherBordersCannotReceive();
    }

    private void reduceAnglesWithIndifferentBorders() {
        for (Direction direction : Direction.values()) {
            Optional<BorderView> borderView = block.getBorderMap().getBorderView(direction);
            if (borderView.isPresent() && borderView.get().getBorderStatus().equals(BorderStatus.INDIFFERENT)) {
                forbidAnglesWithBorder(direction);
            }
        }
    }

    private void reduceAnglesWithoutMandatoryBorders() {
        for (Direction direction : Direction.values()) {
            Optional<BorderView> borderView = block.getBorderMap().getBorderView(direction);
            if (borderView.isPresent() && borderView.get().getBorderStatus().equals(BorderStatus.MANDATORY)) {
                forbidAnglesWithoutBorder(direction);
            }
        }
    }

    private void reduceAnglesWithMandatoryBordersButWithoutBorderThatCanReceive() {
        Set<Angle> feasibleAngles = Sets.newHashSet(block.getFeasibleAngles()); // the original collection will be modified
        for (Angle angle: feasibleAngles) {
            DirectionsDivision directionsDivision = block.getDirectionsDivisions().get(angle);
            for (DirectionSet directionSet : directionsDivision) {
                Set<Direction> canReceiveDirections = Sets.newHashSet();
                Set<Direction> mustSendDirections = Sets.newHashSet();
                for (Direction direction : directionSet) {
                    if (block.getBorderMap().getBorderView(direction).isPresent()) {
                        BorderView borderView = block.getBorderMap().getBorderView(direction).get();
                        if (borderView.canReceive()) {
                            canReceiveDirections.add(direction);
                        } else if (borderView.mustSend()) {
                            mustSendDirections.add(direction);
                        }
                    }
                }
                if (!mustSendDirections.isEmpty() && canReceiveDirections.isEmpty()) {
                    block.forbidAngle(angle);
                }
            }
        }
    }

    /**
     * Unused border is the one that does not belong to a direction division for any feasible angle.
     */
    private void setUnusedBorderAsIndifferent() {
        Set<Direction> unusedDirections = findUnusedDirections();
        for (Direction unusedDirection : unusedDirections) {
            block.getBorderMap().getBorderView(unusedDirection)
                    .ifPresent(borderView -> borderView.updateBorderStatus(BorderStatus.INDIFFERENT));
        }
    }

    private void setCannotSendBorderIfAllOtherBordersCannotReceive() {
        for (Direction direction : block.getBorderMap().getExistingBordersDirections()) {
            BorderView borderView = block.getBorderMap().getBorderView(direction).get();
            if (borderView.getBorderStatus() == BorderStatus.MANDATORY ||
                    borderView.getBorderStatus() == BorderStatus.CANNOT_SEND ||
                    borderView.getBorderStatus() == BorderStatus.INDIFFERENT) {
                continue;
            }
            // 1. Find all angles that contain this border
            Set<Angle> usedAngles = Sets.newHashSet();
            for (Angle angle: block.getFeasibleAngles()) {
                if (block.getDirectionsDivisions().get(angle).contains(direction)) {
                    usedAngles.add(angle);
                }
            }
            // 2. For each angle that uses this border, check if none of borders used by this angle can receive
            // Don't check the border under investigation, as it will be determined by this check
            boolean foundBlockThatCanReceive = false;
            for (Angle angle: usedAngles) {
                DirectionSet directionSetWithTheDirection = block.getDirectionsDivisions().get(angle).getDirectionSetWith(direction).get();

                for (Direction usedDirection: directionSetWithTheDirection) {
                    if (usedDirection != direction && block.getBorderMap().getBorderView(usedDirection).isPresent() &&
                            block.getBorderMap().getBorderView(usedDirection).get().canReceive()) {
                        foundBlockThatCanReceive = true;
                    }
                }
            }
            if (!foundBlockThatCanReceive) {
                borderView.updateBorderStatus(BorderStatus.CANNOT_SEND);
            }
        }
    }

    private void setMandatoryBorders() {
        for (Direction direction : block.getBorderMap().getExistingBordersDirections()) {
            BorderView borderView = block.getBorderMap().getBorderView(direction).get();
            if (borderView.getBorderStatus() == BorderStatus.MANDATORY || borderView.getBorderStatus() == BorderStatus.INDIFFERENT) {
                continue;
            }
            // 1. Find all angles that contain this border
            Set<Angle> usedAngles = Sets.newHashSet();
            for (Angle angle: block.getFeasibleAngles()) {
                if (block.getDirectionsDivisions().get(angle).contains(direction)) {
                    usedAngles.add(angle);
                }
            }
            // 2. For each angle that uses this border, check if there is always a border in the same direction set that must send
            // and it is always the only border that can receive
            boolean foundAnotherBorderThatCanReceive = false;
            boolean foundAngleWithoutMustSendBorders = false;
            for (Angle angle: usedAngles) {
                boolean theAngleHasNoMustSendBorders = true;
                DirectionSet directionSetWithTheDirection = block.getDirectionsDivisions().get(angle).getDirectionSetWith(direction).get();

                for (Direction usedDirection: directionSetWithTheDirection) {
                    if (usedDirection != direction &&
                            block.getBorderMap().getBorderView(usedDirection).isPresent()) {
                        if (block.getBorderMap().getBorderView(usedDirection).get().canReceive()) {
                            foundAnotherBorderThatCanReceive = true;
                        }
                        if (block.getBorderMap().getBorderView(usedDirection).get().mustSend()) {
                            theAngleHasNoMustSendBorders = false;;
                        }
                    }
                }
                if (theAngleHasNoMustSendBorders) {
                    foundAngleWithoutMustSendBorders = true;
                }
            }
            if (!foundAnotherBorderThatCanReceive && !foundAngleWithoutMustSendBorders) {
                borderView.updateBorderStatus(BorderStatus.MANDATORY);
            }

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

    protected void forbidAnglesWithBorder(Direction unusedDirection) {
        Set<Angle> feasibleAngles = Sets.newHashSet(block.getFeasibleAngles());
        for (Angle angle : feasibleAngles) {
            Set<Direction> directionsSet = block.getDirectionsDivisions().get(angle).getDirections();
            if (directionsSet.contains(unusedDirection)) {
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

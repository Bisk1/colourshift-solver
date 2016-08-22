package colourshift.solver;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.TransitiveBlock;
import colourshift.model.border.BorderStatus;
import colourshift.model.border.BorderView;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

public class TransitiveBlockSolver extends BlockSolver {
    private static long serialVersionUID = 0L;

    private TransitiveBlock block;

    public TransitiveBlockSolver(TransitiveBlock block) {
        super(block);
        this.block = block;
    }

    @Override
    protected void reduceAngles() {
        super.reduceAngles();
        reduceAnglesWithMandatoryBordersButWithoutBorderThatCanReceive();

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
                        }
                        if (borderView.mustSend()) {
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


    @Override
    protected void propagateBorder() {
        super.propagateBorder();
        setMustReceiveBorders();
        setCannotSendBorders();
        setMustSendBorders();
    }

    private void setMustSendBorders() {
        List<Angle> feasibleAngles = Lists.newArrayList(block.getFeasibleAngles());
        Angle firstAngle = feasibleAngles.get(0);
        List<Angle> otherAngles = feasibleAngles.subList(1, feasibleAngles.size());

        Map<Direction, Colour> directionsToColours = findMustSendColourForAngle(firstAngle);

        for (Angle angle : otherAngles) {
            Map<Direction, Colour> directionToColourMapForAngle = findMustSendColourForAngle(angle);
            List<Direction> toRemove = Lists.newArrayList();
            for (Map.Entry<Direction, Colour> directionToColour : directionsToColours.entrySet()) {
                Direction direction = directionToColour.getKey();
                if (!directionToColourMapForAngle.containsKey(direction)) {
                    toRemove.add(direction);
                }
                if (directionToColour.getValue() != directionToColourMapForAngle.get(direction)) {
                    toRemove.add(direction);
                }
            }
            directionsToColours.keySet().removeAll(toRemove);
        }
        for (Map.Entry<Direction, Colour> directionToColour : directionsToColours.entrySet()) {
            BorderView borderView = block.getBorderMap().getBorderView(directionToColour.getKey()).get();
            if (directionToColour.getValue() != Colour.GREY) {
                borderView.updateBorderStatus(BorderStatus.RECEIVE, directionToColour.getValue());
            }
        }
    }

    private Map<Direction, Colour> findMustSendColourForAngle(Angle angle) {
        Map<Direction, Colour> directionToColour = Maps.newHashMap();
        for (DirectionSet directionSet : block.getDirectionsDivisions().get(angle)) {
            Optional<Colour> colour = block.getBorderMap().getColourMix(directionSet);
            if (colour.isPresent()) {
                for (Direction direction : directionSet) {
                    directionToColour.put(direction, colour.get());
                }
            }
        }
        return directionToColour;
    }

    protected void setCannotSendBorders() {
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

    private void setMustReceiveBorders() {
        Set<Direction> mustReceiveCandidates = Sets.newHashSet(Direction.values());
        // direction is classified as 'must receive' only if it is 'must receive' for all feasible angles
        for (Angle angle : block.getFeasibleAngles()) {
            Set<Direction> mustReceiveDirectionsForAngle = findMustReceiveBordersForAngle(angle);
            mustReceiveCandidates = mustReceiveCandidates.stream()
                    .filter(mustReceiveDirectionsForAngle::contains)
                    .collect(Collectors.toSet());
        }
        mustReceiveCandidates.forEach(
                mustReceiveDirection ->
                        block.getBorderMap().getBorderView(mustReceiveDirection).get().updateBorderStatus(BorderStatus.MANDATORY));

    }

    private Set<Direction> findMustReceiveBordersForAngle(Angle angle) {
        Set<Direction> mustReceiveBorders = Sets.newHashSet();
        DirectionsDivision directionsDivision = block.getDirectionsDivisions().get(angle);
        for (DirectionSet directionSet : directionsDivision) {
            Set<Direction> mustSendDirections = Sets.newHashSet();
            Set<Direction> canReceiveDirections = Sets.newHashSet();
            for (Direction direction : directionSet) {
                if (!block.getBorderMap().getBorderView(direction).isPresent()) {
                    continue;
                }
                BorderView borderView = block.getBorderMap().getBorderView(direction).get();
                if (borderView.canReceive()) {
                    canReceiveDirections.add(direction);
                }
                if (borderView.mustSend()) {
                    mustSendDirections.add(direction);
                }
            }
            if (!mustSendDirections.isEmpty() && canReceiveDirections.size() == 1) {
                mustReceiveBorders.add(canReceiveDirections.iterator().next());
            }
        }
        return mustReceiveBorders;
    }


}

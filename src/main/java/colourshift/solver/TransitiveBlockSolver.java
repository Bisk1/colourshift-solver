package colourshift.solver;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.TransitiveBlock;
import colourshift.model.border.BorderRequirement;
import colourshift.model.border.BorderStatus;
import colourshift.model.border.BorderView;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;

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
        reduceAngleThatCannotSatisfyMustSendRequirement();
    }


    private void reduceAngleThatCannotSatisfyMustSendRequirement() {
        Set<Angle> feasibleAngles = Sets.newHashSet(block.getFeasibleAngles()); // the original collection will be modified
        for (Angle angle: feasibleAngles) {
            DirectionsDivision directionsDivision = block.getDirectionsDivisions().get(angle);
            for (DirectionSet directionSet : directionsDivision) {
                Set<Direction> canReceiveDirections = Sets.newHashSet();
                Set<Direction> providedDirections = Sets.newHashSet();
                Set<Colour> providedColours = Sets.newHashSet();
                Set<Direction> mustSendDirections = Sets.newHashSet();
                Set<Colour> mustSendColours = Sets.newHashSet();
                for (Direction direction : directionSet) {
                    if (block.getBorderMap().getBorderView(direction).isPresent()) {
                        BorderView borderView = block.getBorderMap().getBorderView(direction).get();
                        if (borderView.canReceive()) {
                            canReceiveDirections.add(direction);
                        }
                        Optional<Colour> providedColour = borderView.provided();
                        if (providedColour.isPresent()) {
                            providedDirections.add(direction);
                            providedColours.add(providedColour.get());
                        }
                        Optional<Colour> mustSendColour = borderView.mustSend();
                        if (mustSendColour.isPresent()) {
                            mustSendDirections.add(direction);
                            mustSendColours.add(mustSendColour.get());
                        }
                    }
                }
                if (!mustSendDirections.isEmpty()) {
                    if (mustSendColours.size() > 1) {
                        block.forbidAngle(angle);
                    } else if (canReceiveDirections.isEmpty()) {
                        block.forbidAngle(angle);
                    } else if (!providedColours.isEmpty()) {
                        Colour mustSendColour = mustSendColours.iterator().next();
                        providedColours.forEach(providedColour -> {
                            if (!providedColour.isSubcolour(mustSendColour)) {
                                block.forbidAngle(angle);
                            }
                        });

                        if (providedDirections.equals(canReceiveDirections)) {
                            // this means that no other colours will be provided,
                            // should already have the msut send colour
                            Colour providedColour = Colour.mix(providedColours);
                            if (providedColour != mustSendColour) {
                                block.forbidAngle(angle);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void propagateBorder() {
        super.propagateBorder();
        setUpdates();
    }

    private void setUpdates() {
        // 1. Init map for all borders directions
        Map<Direction, Set<BorderRequirement>> directionsToUpdatesCandidates = Maps.newHashMap();
        for (Direction direction : block.getBorderMap().getExistingBordersDirections()) {
            directionsToUpdatesCandidates.put(direction, Sets.newHashSet());
        }
        // 2. Collect entries for every angle
        for (Angle angle : block.getFeasibleAngles()) {
            Map<Direction, BorderRequirement> updateCandidatesForAngle = getUpdatesCandidatesForAngle(angle);
            for (Map.Entry<Direction, Set<BorderRequirement>> directionToCandidate : directionsToUpdatesCandidates.entrySet()) {
                directionToCandidate.getValue().add(updateCandidatesForAngle.get(directionToCandidate.getKey()));
            }
        }
        // 3. Merge events and propagate them
        for (Map.Entry<Direction, Set<BorderRequirement>> directionToCandidates : directionsToUpdatesCandidates.entrySet()) {
            Optional<BorderView> borderView = block.getBorderMap().getBorderView(directionToCandidates.getKey());
            if (borderView.isPresent()) {
                BorderRequirement update = mergeUpdates(directionToCandidates.getValue());
                borderView.get().updateBorderStatus(update);
            }
        }
    }

    private Map<Direction, BorderRequirement> getUpdatesCandidatesForAngle(Angle angle) {
        Map<Direction, BorderRequirement> updatesCandidates = Maps.newHashMap();
        for (Direction direction : Direction.values()) {
            updatesCandidates.put(direction, BorderRequirement.unknown());
        }

        DirectionsDivision directionsDivision = block.getDirectionsDivisions().get(angle);

        for (DirectionSet directionSet : directionsDivision) {
            Set<Direction> canReceiveDirections = Sets.newHashSet();
            Set<Direction> mustSendDirections = Sets.newHashSet();
            Set<Colour> mustSendColours = Sets.newHashSet();

            for (Direction direction : directionSet) {
                Optional<BorderView> borderView = block.getBorderMap().getBorderView(direction);
                if (borderView.isPresent()) {
                    if (borderView.get().canReceive()) {
                        canReceiveDirections.add(direction);
                    }
                    Optional<Colour> mustSendColour = borderView.get().mustSend();
                    if (mustSendColour.isPresent()) {
                        mustSendDirections.add(direction);
                        mustSendColours.add(mustSendColour.get());
                    }
                }
            }

            Optional<Colour> colour = block.getBorderMap().getColourMix(directionSet);
            if (colour.isPresent()) {
                for (Direction direction : directionSet) {
                    updatesCandidates.put(direction, BorderRequirement.provided(colour.get()));
                }
            }

            if (canReceiveDirections.size() == 1) {
                updatesCandidates.put(canReceiveDirections.iterator().next(), BorderRequirement.cannotSend());
            }
            if (!mustSendDirections.isEmpty()
                    && mustSendColours.size() == 1
                    && canReceiveDirections.size() == 1) {
                Direction receiver = canReceiveDirections.iterator().next();
                Colour colourToReceive = mustSendColours.iterator().next();
                updatesCandidates.put(receiver, BorderRequirement.mustSend(colourToReceive));
                // Now all the directions except for receiver, can send
                directionSet.stream()
                        .filter(direction -> direction != receiver) // the one that receives
                        .filter(direction -> !mustSendDirections.contains(direction)) // those that already must send
                        .forEach(direction -> updatesCandidates.put(
                                direction, BorderRequirement.provided(colourToReceive)));
            }
        }

        Sets.newHashSet(Direction.values()).stream()
                .filter(direction -> !directionsDivision.contains(direction))
                .forEach(direction -> updatesCandidates.put(direction, BorderRequirement.indifferent()));


        return updatesCandidates;
    }

    private BorderRequirement mergeUpdates(Set<BorderRequirement> updatesCandidates) {
        if (updatesCandidates.stream()
                .anyMatch(borderRequirement -> borderRequirement.getBorderStatus() == BorderStatus.UNKNOWN)) {
            return BorderRequirement.unknown();
        }
        if (updatesCandidates.stream()
                .allMatch(borderRequirement -> borderRequirement.getBorderStatus() == BorderStatus.INDIFFERENT)) {
            return BorderRequirement.indifferent();
        }
        if (updatesCandidates.stream()
                .allMatch(borderRequirement -> borderRequirement.getBorderStatus() == BorderStatus.MUST_SEND)) {
            Colour colourCandidate = updatesCandidates.iterator().next().getColour().get();
            if (updatesCandidates.stream()
                    .allMatch(borderRequirement -> borderRequirement.getColour().get() == colourCandidate)) {
                return BorderRequirement.mustSend(colourCandidate);
            }
        }
        if (updatesCandidates.stream()
                .allMatch(borderRequirement -> borderRequirement.getBorderStatus() == BorderStatus.PROVIDED)) {
            Colour colourCandidate = updatesCandidates.iterator().next().getColour().get();
            if (updatesCandidates.stream()
                    .allMatch(borderRequirement -> borderRequirement.getColour().get() == colourCandidate)) {
                return BorderRequirement.provided(colourCandidate);
            }
        }
        if (updatesCandidates.stream()
                .allMatch(borderRequirement -> borderRequirement.getBorderStatus() == BorderStatus.CANNOT_SEND
                        || borderRequirement.getBorderStatus() == BorderStatus.INDIFFERENT
                        || borderRequirement.getBorderStatus() == BorderStatus.MUST_SEND)) {
            Optional<Colour> colourCandidate = updatesCandidates.stream()
                    .map(BorderRequirement::getColour)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst();
            if (colourCandidate.isPresent()
                    && updatesCandidates.stream()
                    .map(BorderRequirement::getColour)
                    .allMatch(colour -> colour.isPresent() && colour.get() == colourCandidate.get())) {
                return BorderRequirement.mustSend(colourCandidate.get());
            }
            return BorderRequirement.cannotSend();
        }
        return BorderRequirement.unknown();
    }

}

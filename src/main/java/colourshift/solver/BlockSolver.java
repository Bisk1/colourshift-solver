package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.*;
import com.google.common.collect.Sets;

import java.io.Serializable;
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
    }

    private void reduceAnglesForEdgeBlock() {
        Set<Angle> anglesToForbid = Sets.newHashSet();
        for (Direction unusedDirection : block.getBorderMap().findDirectionsWithStatus(BorderStatus.UNUSED)) {
            anglesToForbid.addAll(findAnglesUsedByDirection(unusedDirection));
        }
        /**
         * If every feasible angle is using at least one unused direction, they should not be fobidden
         * but it is possible to choose any and forbid the rest
         */
        if (anglesToForbid.equals(block.getFeasibleAngles())) {
            anglesToForbid.remove(anglesToForbid.iterator().next());
        }
        block.forbidAngles(anglesToForbid);
    }

    protected Set<Angle> findAnglesUsedByDirection(Direction unusedDirection) {
        Set<Angle> anglesUsedByDirection = Sets.newHashSet();
        for (Angle angle : block.getFeasibleAngles()) {
            Set<Direction> directionsSet = block.getDirectionsDivisions().get(angle).getDirections();
            if (directionsSet.contains(unusedDirection)) {
                anglesUsedByDirection.add(angle);
            }
        }
        return anglesUsedByDirection;
    }

}
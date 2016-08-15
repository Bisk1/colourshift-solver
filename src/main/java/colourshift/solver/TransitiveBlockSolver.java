package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.TransitiveBlock;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TransitiveBlockSolver extends BlockSolver {
    private TransitiveBlock block;

    public TransitiveBlockSolver(TransitiveBlock block) {
        super(block);
        this.block = block;
    }

    @Override
    protected void reduceAnglesForUnusedBorder(Direction unusedDirection) {
        Set<Angle> feasibleAngles = Sets.newHashSet(block.getFeasibleAngles());
        for (Angle angle : feasibleAngles) {
            Set<Direction> directionsSet = block.getDirectionsDivisions().get(angle).getDirections();
            if (directionsSet.contains(unusedDirection)) {
                block.forbidAngle(angle);
            }
        }
    }


    @Override
    protected Set<Direction> findUnusedDirections() {
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

}

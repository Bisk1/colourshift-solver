package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.TransitiveBlock;
import com.google.common.collect.Sets;

import java.util.Set;

public class TransitiveBlockSolver extends BlockSolver {
    private TransitiveBlock block;

    public TransitiveBlockSolver(TransitiveBlock block) {
        super(block);
        this.block = block;
    }

    @Override
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

package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.Source;
import colourshift.model.blocks.TransitiveBlock;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

public class SourceSolver extends BlockSolver {
    private Source block;

    public SourceSolver(Source block) {
        super(block);
        this.block = block;
    }

    @Override
    protected void reduceAnglesForUnusedBorder(Direction unusedDirection) {
        List<Angle> feasibleAngles = Lists.newArrayList(block.getFeasibleAngles());
        for (Angle angle : feasibleAngles) {
            DirectionSet directionSet = block.getDirectionsSets().get(angle);
            if (directionSet.contains(unusedDirection)) {
                block.forbidAngle(angle);
            }
        }
    }
}

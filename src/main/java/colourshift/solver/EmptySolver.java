package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.blocks.Empty;
import com.google.common.collect.Sets;

import java.util.Set;

public class EmptySolver extends BlockSolver {

    public EmptySolver(Empty block) {
        super(block);
    }

    @Override
    protected Set<Direction> findUnusedDirections() {
        return Sets.newHashSet();
    }

    @Override
    protected void reduceAnglesForUnusedBorder(Direction direction) {

    }
}

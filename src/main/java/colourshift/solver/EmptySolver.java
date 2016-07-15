package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.Empty;
import colourshift.model.blocks.Target;

import java.util.Optional;

public class EmptySolver extends BlockSolver {

    public EmptySolver(Empty block) {
        super(block);
    }

    @Override
    public void applyInitialRules() {
        super.applyInitialRules();
    }

}

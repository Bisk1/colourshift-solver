package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.Source;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SourceSolver extends BlockSolver {
    private Source block;

    public SourceSolver(Source block) {
        super(block);
        this.block = block;
    }

}

package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
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

}

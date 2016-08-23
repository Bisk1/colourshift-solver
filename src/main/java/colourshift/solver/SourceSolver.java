package colourshift.solver;

import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.Source;
import colourshift.model.border.BorderRequirement;
import colourshift.model.border.BorderStatus;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.stream.Collectors;

public class SourceSolver extends BlockSolver {
    private static long serialVersionUID = 0L;

    private Source block;

    public SourceSolver(Source block) {
        super(block);
        this.block = block;
    }

    @Override
    protected void propagateBorder() {
        super.propagateBorder();
        setColourProvided();
    }

    private void setColourProvided() {
        Set<Direction> colourProvidedCandidates = Sets.newHashSet(Direction.values());
        for (Angle angle: block.getFeasibleAngles()) {
            // given the angle, colour is provided simply to all directions in relevant direction set
            DirectionSet colourProvidedForThisAngle = block.getDirectionsSets().get(angle);
            colourProvidedCandidates = colourProvidedCandidates.stream()
                    .filter(colourProvidedForThisAngle::contains)
                    .collect(Collectors.toSet());
        }
        for (Direction direction : colourProvidedCandidates) {
            block.getBorderMap().getBorderView(direction).get().updateBorderStatus(BorderRequirement.canReceive(block.getPower().getColour()));
        }
    }

}

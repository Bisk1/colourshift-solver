package colourshift.solver;

import colourshift.gui.Gui;
import colourshift.model.Board;
import colourshift.model.Direction;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.Block;
import com.google.common.collect.Sets;

import java.util.Set;

public class Core {
    private Gui gui;
    private Board board;
    public Core(Gui gui, Board board) {
        this.gui = gui;
        this.board = board;
    }

    public void run() {
        validateBoard();
        applyInitialRules();
    }

    private void validateBoard() {
        if (!board.isComplete()) {
            throw new RuntimeException("Board is not complete (there are some empty blocks) - solving not possible");
        }
    }

    private void applyInitialRules() {
        for (Block block : board.getBlocks().values()) {

        }
    }

    private void applyInitialRule(Block block) {
        if (block.getBorderMap().size() < 4) {
            reduceAnglesForEdgeBlock(block);
        }
    }

    private void reduceAnglesForEdgeBlock(Block block) {
        for (Direction direction : Direction.values()) {
            if (block.getBorderMap().contains(direction)) {
                reduceAnglesForUnusedBorder(block, direction);
            }
        }
    }

    private void reduceAnglesForUnusedBorder(Block block, Direction direction) {
        Set<Angle> feasibleAngles = Sets.newHashSet(block.getFeasibleAngles());
        for (Angle angle : feasibleAngles) {
        }
    }

}

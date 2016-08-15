package colourshift.solver;

import colourshift.gui.Gui;
import colourshift.model.Board;
import colourshift.model.Direction;
import colourshift.model.angle.Angle;
import colourshift.model.blocks.Block;
import com.google.common.collect.Sets;

import java.util.Set;

public class BoardSolver {
    private Gui gui;
    private Board board;
    public BoardSolver(Gui gui, Board board) {
        this.gui = gui;
        this.board = board;
    }

    public void run() {
        validateBoard();
        applyInitialRules();
        board.refreshPower();
        gui.refreshBoardNode(board);
    }

    private void applyInitialRules() {
        for (Block block : board.getBlocks().values()) {
            BlockSolver blockSolver = BlockSolver.create(block);
            blockSolver.applyInitialRules();
        }
    }

    private void validateBoard() {
        if (!board.isComplete()) {
            throw new RuntimeException("Board is not complete (there are some empty blocks) - solving not possible");
        }
    }


}

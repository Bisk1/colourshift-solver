package colourshift.solver;

import colourshift.gui.Gui;
import colourshift.model.Board;
import colourshift.model.Direction;
import colourshift.model.blocks.Block;
import colourshift.model.border.Border;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

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
        for (Block block: board.getBlocks().values()) {
            block.getSolver().applyInitialRules();
        }
    }

    private void validateBoard() {
        if (!board.isComplete()) {
            throw new RuntimeException("Board is not complete (there are some empty blocks) - solving not possible");
        }
    }

}

package colourshift.solver;

import colourshift.gui.Gui;
import colourshift.model.Board;
import colourshift.model.blocks.Block;

public class BoardSolver {
    private Gui gui;
    private Board board;
    public BoardSolver(Gui gui, Board board) {
        this.gui = gui;
        this.board = board;
    }

    public void run() {
        validateBoard();
        System.out.println(calculateFeasibleStatesCount());
        applyInitialRules();
        System.out.println(calculateFeasibleStatesCount());
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

    private String calculateFeasibleStatesCount() {
        int indexOfTwo = 0;
        int indexOfThree = 0;
        int unfixedBlocks = 0;
        for (Block block : board.getBlocks().values()) {
            int feasibleAngleCount = block.getFeasibleAngles().size();
            if (feasibleAngleCount == 2) {
                indexOfTwo += 1;
            } else if (feasibleAngleCount == 4) {
                indexOfTwo += 2;
            } else if (feasibleAngleCount == 3) {
                indexOfThree += 1;
            }
            if (feasibleAngleCount > 1) {
                unfixedBlocks++;
            }
        }
        return "Feasible states: 2 ^ " + indexOfTwo + " + 3 ^ " + indexOfThree + " Unfixed blocks: " + unfixedBlocks;

    }


}

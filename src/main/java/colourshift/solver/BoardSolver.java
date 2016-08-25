package colourshift.solver;

import colourshift.gui.Gui;
import colourshift.model.Board;
import colourshift.model.angle.Angle;
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
        countAndLogFeasibleStates();
        try {
            solve(board);
            System.out.println("Solving successful");
        } catch (UnsolvableException e) {
            System.out.println("Impossible to solve");
        }
        countAndLogFeasibleStates();
        gui.refreshBoardNode(board);
    }

    private void solve(Board board) {
        applyInitialRules();
        board.refreshPower();
        if (!board.isSolved()) {
            //branchAndBound(board);
        }
    }

    private void branchAndBound(Board board) {
        Block blockToFix = findAnyUnfixedBlock(board);
        for (Angle angleToFix : blockToFix.getFeasibleAngles()) {
            Board boardBranch = board.copy();
            Block blockBranch = boardBranch.get(blockToFix.getRow(), blockToFix.getCol());
            blockBranch.fixAngle(angleToFix);
            try {
                blockBranch.statusUpdateReceived();
                boardBranch.refreshPower();
                if (boardBranch.isSolved()) {
                    this.board = boardBranch;
                } else {
                    branchAndBound(boardBranch);
                }
                return;
            } catch (UnsolvableException e) {
                // Unacceptable angle, try another
            }
        }
        throw new UnsolvableException();
    }

    private Block findAnyUnfixedBlock(Board board) {
        return board.getBlocks().values().stream()
                .filter(block -> !block.isFixed())
                .findFirst()
                .orElseThrow(() -> new UnsolvableException("No unfixed blocks"));
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

    private void countAndLogFeasibleStates() {
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
        System.out.println("Feasible states: 2 ^ " + indexOfTwo + " + 3 ^ " + indexOfThree + " Unfixed blocks: " + unfixedBlocks);

    }


}

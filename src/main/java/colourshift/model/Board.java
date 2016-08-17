package colourshift.model;

import colourshift.model.blocks.*;
import colourshift.model.border.BorderMap;
import com.google.common.collect.Table;

import java.io.Serializable;
import java.util.Optional;

public class Board implements Serializable {

    private Table<Integer, Integer, Block> blocks;
    private BlockFactory blockFactory;
    private SourceManager sourceManager;
    private TargetManager targetManager;

    public Board(Table<Integer, Integer, Block> blocks, BlockFactory blockFactory, SourceManager sourceManager, TargetManager targetManager) {
        this.blocks = blocks;
        this.blockFactory = blockFactory;
        this.sourceManager = sourceManager;
        this.targetManager = targetManager;
    }

    public Block get(int row, int column) {
        return blocks.get(row, column);
    }

    public int size() {
        return blocks.columnKeySet().size();
    }

    public void rotate(Block block) {
        block.rotate();
        refreshPower();
    }

    public Block changeBlockType(int row, int column, BlockType newBlockType, Colour newColour) {
        Block oldBlock = blocks.get(row, column);
        blockFactory.deregister(oldBlock);

        Block newBlock = blockFactory.createAndInitBlock(newBlockType, Optional.of(newColour));
        newBlock.setPosition(oldBlock.getX(), oldBlock.getY());

        BorderMap borderMap = oldBlock.getBorderMap();
        borderMap.changeBlock(newBlock);
        newBlock.setBorderMap(borderMap);
        blocks.put(row, column, newBlock);

        refreshPower();
        return newBlock;
    }

    public void refreshPower() {
        resetPower();
        sourceManager.activateAll();
    }

    public void resetPower() {
        for (Block block : blocks.values()) {
            block.resetPower();
        }
    }

    /**
     * If there are any Empty blocks instances, the board is incomplete
     * @return
     */
    public boolean isComplete() {
        return blocks.values()
                .stream()
                .noneMatch(e -> e instanceof Empty);
    }

    public Table<Integer, Integer, Block> getBlocks() {
        return blocks;
    }
}

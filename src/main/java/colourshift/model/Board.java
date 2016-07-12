package colourshift.model;

import colourshift.model.Colour;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.BlockFactory;
import colourshift.model.blocks.BlockType;
import colourshift.model.border.BorderMap;
import com.google.common.collect.Table;

import java.io.Serializable;
import java.util.Optional;

import static colourshift.util.IterationUtils.getNextFromList;

public class Board implements Serializable {

	private Table<Integer, Integer, Block> blocks;
	private BlockFactory blockFactory;

	public Board(Table<Integer, Integer, Block> blocks, BlockFactory blockFactory) {
		this.blocks = blocks;
		this.blockFactory = blockFactory;
	}

	public Block get(int row, int column) {
        return blocks.get(row, column);
    }

	public int size() {
		return blocks.columnKeySet().size();
	}

	public Block changeBlockType(int row, int column) {
        Block oldBlock = blocks.get(row, column);
        oldBlock.fullClear();
        BlockType oldBlockType = BlockType.fromJavaClass(oldBlock.getClass());
        BlockType newBlockType = getNextFromList(oldBlockType, BlockType.valuesList);
        Block newBlock = blockFactory.createAndInitBlock(newBlockType, Optional.of(Colour.GREEN));

		BorderMap borderMap = oldBlock.getBorderMap();
        borderMap.changeBlock(newBlock);
        newBlock.setBorderMap(borderMap);
        blocks.put(row, column, newBlock);
        newBlock.fullUpdate();
        return newBlock;
	}
}

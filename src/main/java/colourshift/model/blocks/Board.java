package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.border.BorderMap;
import com.google.common.collect.Table;

import java.util.Optional;

import static colourshift.util.IterationUtils.getNextFromList;

public class Board {

	private Table<Integer, Integer, Block> blocks;
	private BlockFactory blockFactory;

	public Board(Table<Integer, Integer, Block> blocks, BlockFactory blockFactory) {
		this.blocks = blocks;
		this.blockFactory = blockFactory;
	}

	public Block get(int x, int y) {
        return blocks.get(x, y);
    }

	public int size() {
		return blocks.columnKeySet().size();
	}

	public Block changeBlockType(int x, int y) {
        Block oldBlock = blocks.get(x, y);
        BlockType oldBlockType = BlockType.fromJavaClass(oldBlock.getClass());
        BlockType newBlockType = getNextFromList(oldBlockType, BlockType.valuesList);
        if (newBlockType == BlockType.SOURCE_ONE) {
            System.out.printf("Suspect");
        }
        Block newBlock = blockFactory.createAndInitBlock(newBlockType, Optional.of(Colour.GREEN));

		BorderMap borderMap = oldBlock.getBorderMap();
        borderMap.changeBlock(newBlock);
        newBlock.setBorderMap(borderMap);
        blocks.put(x, y, newBlock);
        return newBlock;
	}
}

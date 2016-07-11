package colourshift.model.blocks;

import com.google.common.collect.Table;

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
}

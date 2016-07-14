package colourshift.model;

import colourshift.model.blocks.Block;
import colourshift.model.blocks.BlockFactory;
import colourshift.model.blocks.SourceManager;
import colourshift.model.blocks.TargetManager;
import colourshift.model.border.Border;
import colourshift.model.border.BorderMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardFactory {

    public enum Wrap {
        ENABLED,
        DISABLED
    }

	@Autowired
	private BlockFactory blockFactory;

	@Autowired
	private SourceManager sourceManager;

	@Autowired
	private TargetManager targetManager;

	public BoardFactory(BlockFactory blockFactory) {
		this.blockFactory = blockFactory;
	}

	public Board createEmpty(int rows, int cols, Wrap wrap) {
		
		Table<Integer, Integer, Block> blocks = createBlocks(cols, rows);
		wireNeighbours(blocks, rows, cols, wrap);
		return new Board(blocks, blockFactory, sourceManager, targetManager);
	}
	private Table<Integer, Integer, Block> createBlocks(int rows, int cols) {
		Table<Integer, Integer, Block> blocks = HashBasedTable.create(rows, cols);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				blocks.put(i, j, blockFactory.createEmpty());
			}
		}
		return blocks;
	}

	private void wireNeighbours(Table<Integer, Integer, Block> blocks, int rows, int cols, Wrap wrap) {
		Table<Integer, Integer, BorderMap.Builder> bordersMapsBuilders = HashBasedTable.create(rows, cols);
		/**
		 * In the first iteration create border on the left and up - all borders will
		 * be created without duplicates, but blocks will not have relevant borders 
		 * set on the right and down yet
		 */
		for (Cell<Integer, Integer, Block> cell : blocks.cellSet()) {
            BorderMap.Builder builder = new BorderMap.Builder();
            bordersMapsBuilders.put(cell.getColumnKey(), cell.getRowKey(), builder);
            boolean isLeftmostBlock = cell.getColumnKey() == 0;
            boolean isUpmostBlock = cell.getRowKey() == 0;
            if (!isLeftmostBlock || wrap == Wrap.ENABLED) {
                Block leftBlock = isLeftmostBlock ?
                        blocks.get(cell.getRowKey(), cols - 1) :
                        blocks.get(cell.getRowKey(), cell.getColumnKey() - 1);
                builder.setBorder(Direction.LEFT, new Border(leftBlock, Direction.LEFT, cell.getValue()));
            }
            if (!isUpmostBlock || wrap == Wrap.ENABLED) {
                Block upBlock = isUpmostBlock ?
                        blocks.get(rows - 1, cell.getColumnKey()) :
                        blocks.get(cell.getRowKey() - 1, cell.getColumnKey());
                builder.setBorder(Direction.UP, new Border(upBlock, Direction.UP, cell.getValue()));
            }
        }
		/**
		 * In the second iteration set border on the right and down - but don't create them
		 * - reuse the relevant borders created in the first iteration.
		 */

		for (Cell<Integer, Integer, Block> cell : blocks.cellSet()) {
			BorderMap.Builder builder = bordersMapsBuilders.get(cell.getColumnKey(), cell.getRowKey());
			boolean isRightmostBlock = cell.getColumnKey() == cols - 1;
			boolean isDownmostBlock = cell.getRowKey() == rows - 1;
			if (!isRightmostBlock || wrap == Wrap.ENABLED) {
                int rightBlockColumnKey = isRightmostBlock ? 0 : cell.getColumnKey() + 1;
				BorderMap.Builder rightBuilder = bordersMapsBuilders.get(rightBlockColumnKey, cell.getRowKey());
				builder.setBorder(Direction.RIGHT, rightBuilder.getBorder(Direction.LEFT));
			}
			if (!isDownmostBlock || wrap == Wrap.ENABLED) {
                int downBlockRowKey = isDownmostBlock ? 0 : cell.getRowKey() + 1;
                BorderMap.Builder downBuilder = bordersMapsBuilders.get(cell.getColumnKey(), downBlockRowKey);
                builder.setBorder(Direction.DOWN, downBuilder.getBorder(Direction.UP));
            }
			// All borders set - builders ready to execute
			BorderMap borderMap = builder.build(cell.getValue());
			cell.getValue().setBorderMap(borderMap);
		}
	}
	
}

package colourshift.model.blocks;

import java.util.Map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import colourshift.model.Direction;
import colourshift.model.border.Border;
import colourshift.model.border.BorderMap;

public class BoardFactory {

	private BlockFactory blockFactory;
	
	public BoardFactory(BlockFactory blockFactory) {
		this.blockFactory = blockFactory;
	}

	public Board createEmpty(int rows, int cols, boolean wrapEnabled) {
		
		Table<Integer, Integer, Block> blocks = createBlocks(cols, rows);
		wireNeighbours(blocks, rows, cols, wrapEnabled);
		return new Board(blocks, blockFactory);
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

	private void wireNeighbours(Table<Integer, Integer, Block> blocks, int rows, int cols, boolean wrapEnabled) {
		Map<Block, BorderMap.Builder> bordersMapsBuilders = Maps.newHashMap();
		/**
		 * In the first iteration create border on the left and up - all borders will
		 * be created without duplicates, but blocks will not have relevant borders 
		 * set on the right and down yet
		 */
		for (Cell<Integer, Integer, Block> cell : blocks.cellSet()) {
			BorderMap.Builder builder = new BorderMap.Builder();
			bordersMapsBuilders.put(cell.getValue(), builder);
			boolean isLeftmostBlock = cell.getColumnKey() == 0;
			boolean isUpmostBlock = cell.getRowKey() == 0;
			if (!isLeftmostBlock || wrapEnabled) {
				Block leftBlock = isLeftmostBlock ? 
						blocks.get(cell.getRowKey(), cols - 1):
						blocks.get(cell.getRowKey(), cell.getColumnKey() - 1);
				builder.setBorder(Direction.LEFT, new Border(cell.getValue(), Direction.LEFT, leftBlock));
			}
			if (!isUpmostBlock || wrapEnabled) {
				Block upBlock = isUpmostBlock ? 
						blocks.get(rows - 1, cell.getColumnKey()) :
						blocks.get(cell.getRowKey() - 1, cell.getColumnKey());
				builder.setBorder(Direction.UP, new Border(cell.getValue(), Direction.UP, upBlock));
			}
		}
		/**
		 * In the second iteration set border on the right and down - but don't create them
		 * - reuse the relevant borders created in the first iteration.
		 */

		for (Cell<Integer, Integer, Block> cell : blocks.cellSet()) {
			BorderMap.Builder builder = bordersMapsBuilders.get(cell.getValue());
			boolean isRightmostBlock = cell.getColumnKey() == cols - 1;
			boolean isDownmostBlock = cell.getRowKey() == rows - 1;
			if (!isRightmostBlock || wrapEnabled) {
				Block rightBlock = isRightmostBlock ? 
						blocks.get(cell.getRowKey(), 0):
						blocks.get(cell.getRowKey(), cell.getColumnKey() - 1);
				BorderMap.Builder rightBuilder = bordersMapsBuilders.get(rightBlock);
				
				builder.setBorder(Direction.RIGHT, rightBuilder.getBorder(Direction.LEFT));
			}
			if (isDownmostBlock || wrapEnabled) {
				Block downBlock = isDownmostBlock ? 
						blocks.get(0, cell.getColumnKey()) :
						blocks.get(cell.getRowKey() - 1, cell.getColumnKey());
						
				BorderMap.Builder downBuilder = bordersMapsBuilders.get(downBlock);
				builder.setBorder(Direction.DOWN, downBuilder.getBorder(Direction.DOWN));
			}
			// All borders set - builders ready to execute
			cell.getValue().setBorderMap(builder.build(cell.getValue()));
		}
	}
	
}

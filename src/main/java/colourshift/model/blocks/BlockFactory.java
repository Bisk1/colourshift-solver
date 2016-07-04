package colourshift.model.blocks;

import java.util.Map;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import colourshift.model.Colour;

public class BlockFactory {

	public enum BlockType {
		EMPTY, TARGET, STRAIGHT, TURN, THREE, DOUBLE_TURN, HALF_FOUR, FULL_FOUR, SOURCE_ONE, SOURCE_STRAIGHT, SOURCE_TURN, SOURCE_THREE, SOURCE_FOUR;
		
		private static Map<Class<? extends Block>, BlockType> blockTypeToEnum;
		
		static {
			blockTypeToEnum.put(Empty.class, EMPTY);
			blockTypeToEnum.put(Target.class, TARGET);
			blockTypeToEnum.put(Straight.class, STRAIGHT);
			blockTypeToEnum.put(Turn.class, TURN);
			blockTypeToEnum.put(Three.class, THREE);
			blockTypeToEnum.put(DoubleTurn.class, DOUBLE_TURN);
			blockTypeToEnum.put(HalfFour.class, HALF_FOUR);
			blockTypeToEnum.put(FullFour.class, FULL_FOUR);
			blockTypeToEnum.put(SourceOne.class, SOURCE_ONE);
			blockTypeToEnum.put(SourceStraight.class, SOURCE_STRAIGHT);
			blockTypeToEnum.put(SourceTurn.class, SOURCE_TURN);
			blockTypeToEnum.put(SourceThree.class, SOURCE_THREE);
			blockTypeToEnum.put(SourceFour.class, SOURCE_FOUR);
		}
		
		public static BlockType fromJavaClass(Class<? extends Block> type) {
			return blockTypeToEnum.get(type);
		}
	}
	
	private ImmutableSet<BlockType> blockTypesThatRequireColour = Sets.immutableEnumSet(BlockType.TARGET, BlockType.SOURCE_ONE,
			BlockType.SOURCE_STRAIGHT, BlockType.SOURCE_TURN, BlockType.SOURCE_THREE, BlockType.SOURCE_FOUR);

	private SourceManager sourceManager;
	private TargetManager targetManager;

	public BlockFactory(SourceManager sourceManager, TargetManager targetManager) {
		this.sourceManager = sourceManager;
		this.targetManager = targetManager;
	}

	public Block createEmpty() {
		return createAndInitBlock(BlockType.EMPTY, Optional.empty());
	}

	public Block createAndInitBlock(BlockType blockType, Optional<Colour> colour) {
		Preconditions.checkArgument(colour.isPresent() || !blockTypesThatRequireColour.contains(blockType));
		Block block = createBlock(blockType, colour.get());
		
		if (block instanceof Source) {
			sourceManager.add((Source)block);
		} else if (block instanceof Target) {
			targetManager.add((Target)block);
		}
		
		return block;
	}

	private Block createBlock(BlockType blockType, Colour colour) {
		switch (blockType) {
		case EMPTY:
			return Empty.getInstance();
		case TARGET:
			return new Target(colour);
		case STRAIGHT:
			return new Straight();
		case TURN:
			return new Turn();
		case THREE:
			return new Three();
		case DOUBLE_TURN:
			return new DoubleTurn();
		case HALF_FOUR:
			return new HalfFour();
		case FULL_FOUR:
			return new FullFour();
		case SOURCE_ONE:
			return new SourceOne(colour);
		case SOURCE_STRAIGHT:
			return new SourceStraight(colour);
		case SOURCE_TURN:
			return new SourceTurn(colour);
		case SOURCE_THREE:
			return new SourceThree(colour);
		case SOURCE_FOUR:
			return new SourceFour(colour);
		default:
			throw new RuntimeException("Unkown block type: " + blockType);
		}
	}
}

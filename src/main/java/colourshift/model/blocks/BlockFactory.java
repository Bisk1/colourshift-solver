package colourshift.model.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import colourshift.model.Direction;
import colourshift.model.angle.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import colourshift.model.Colour;


public class BlockFactory {

	public enum BlockType {
		EMPTY(Empty.class, 0, Single.values()),
		TARGET(Target.class, 2, Direction.values()),
		STRAIGHT(Straight.class, 1, Orientation.values()),
		TURN(Turn.class, 1, TurnAngle.values()),
		THREE(Three.class, 1, ThreeAngle.values()),
		DOUBLE_TURN(DoubleTurn.class, 2, Single.values()),
		HALF_FOUR(HalfFour.class, 2, Single.values()),
		FULL_FOUR(FullFour.class, 1, Single.values()),
		SOURCE_ONE(SourceOne.class, 1, Direction.values()),
        SOURCE_STRAIGHT(SourceStraight.class, 1, Orientation.values()),
		SOURCE_TURN(SourceTurn.class, 1, TurnAngle.values()),
		SOURCE_THREE(SourceThree.class, 1, ThreeAngle.values()),
		SOURCE_FOUR(SourceFour.class, 1, Single.values());

		private static class Holder {
			public static Map<Class<? extends Block>, BlockType> blockTypeToEnum = Maps.newHashMap();
		}

		private int coloursCount;
        private Class<? extends Block> javaClass;
        private List<Angle> initialAngles;

		BlockType(Class<? extends Block> javaClass, int coloursCount, Angle[] initialAngles) {
            this.coloursCount = coloursCount;
            this.javaClass = javaClass;
            this.initialAngles = Arrays.asList(initialAngles);
            Holder.blockTypeToEnum.put(javaClass, this);
        }

		public static BlockType fromJavaClass(Class<? extends Block> javaClass) {
			return Holder.blockTypeToEnum.get(javaClass);
		}

        public Class<? extends Block> getJavaClass() {
            return javaClass;
        }

		public int getColoursCount() {
			return coloursCount;
		}

        public List<Angle> getInitialAngles() {
            return initialAngles;
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
		Block block = createBlock(blockType, colour);
		
		if (block instanceof Source) {
			sourceManager.add((Source)block);
		} else if (block instanceof Target) {
			targetManager.add((Target)block);
		}
		
		return block;
	}

	private Block createBlock(BlockType blockType, Optional<Colour> colour) {
		Preconditions.checkArgument(colour.isPresent() || !blockTypesThatRequireColour.contains(blockType));
		switch (blockType) {
		case EMPTY:
			return Empty.getInstance();
		case TARGET:
			return new Target(colour.get());
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
			return new SourceOne(colour.get());
		case SOURCE_STRAIGHT:
			return new SourceStraight(colour.get());
		case SOURCE_TURN:
			return new SourceTurn(colour.get());
		case SOURCE_THREE:
			return new SourceThree(colour.get());
		case SOURCE_FOUR:
			return new SourceFour(colour.get());
		default:
			throw new RuntimeException("Unkown block type: " + blockType);
		}
	}
}

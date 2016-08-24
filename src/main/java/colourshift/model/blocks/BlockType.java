package colourshift.model.blocks;

import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.*;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum BlockType {
    EMPTY(Empty.class, 0, Single.values(),
            ImmutableBiMap.of()),
    TARGET(Target.class, 2, Direction.values(),
            ImmutableBiMap.of(
                    Direction.LEFT, new DirectionsDivision(new DirectionSet(Direction.LEFT)),
                    Direction.UP, new DirectionsDivision(new DirectionSet(Direction.UP)),
                    Direction.RIGHT, new DirectionsDivision(new DirectionSet(Direction.RIGHT)),
                    Direction.DOWN, new DirectionsDivision(new DirectionSet(Direction.DOWN)))),
    STRAIGHT(Straight.class, 1, Orientation.values(),
            ImmutableBiMap.of(
                    Orientation.HORIZONTAL, new DirectionsDivision(new DirectionSet(Direction.LEFT, Direction.RIGHT)),
                    Orientation.VERTICAL, new DirectionsDivision(new DirectionSet(Direction.UP, Direction.DOWN)))),
    TURN(Turn.class, 1, TurnAngle.values(),
            ImmutableBiMap.of(
                    TurnAngle.LEFT_UP, new DirectionsDivision(new DirectionSet(Direction.LEFT, Direction.UP)),
                    TurnAngle.UP_RIGHT, new DirectionsDivision(new DirectionSet(Direction.UP, Direction.RIGHT)),
                    TurnAngle.RIGHT_DOWN, new DirectionsDivision(new DirectionSet(Direction.RIGHT, Direction.DOWN)),
                    TurnAngle.LEFT_DOWN, new DirectionsDivision(new DirectionSet(Direction.LEFT, Direction.DOWN)))),
    THREE(Three.class, 1, ThreeAngle.values(),
            ImmutableBiMap.of(
                    ThreeAngle.NOT_LEFT, new DirectionsDivision(new DirectionSet(Direction.UP, Direction.RIGHT, Direction.DOWN)),
                    ThreeAngle.NOT_UP, new DirectionsDivision(new DirectionSet(Direction.LEFT, Direction.RIGHT, Direction.DOWN)),
                    ThreeAngle.NOT_RIGHT, new DirectionsDivision(new DirectionSet(Direction.LEFT, Direction.UP, Direction.DOWN)),
                    ThreeAngle.NOT_DOWN, new DirectionsDivision(new DirectionSet(Direction.LEFT, Direction.UP, Direction.RIGHT)))),
    DOUBLE_TURN(DoubleTurn.class, 2, DoubleTurnAngle.values(),
            ImmutableBiMap.of(
                    DoubleTurnAngle.LEFT_DOWN_AND_UP_RIGHT, new DirectionsDivision(
                            new DirectionSet(Direction.LEFT, Direction.DOWN),
                            new DirectionSet(Direction.UP, Direction.RIGHT)),
                    DoubleTurnAngle.LEFT_UP_AND_RIGHT_DOWN, new DirectionsDivision(
                            new DirectionSet(Direction.LEFT, Direction.UP),
                            new DirectionSet(Direction.RIGHT, Direction.DOWN)))),
    HALF_FOUR(HalfFour.class, 2, Single.values(),
            ImmutableBiMap.of(
                    Single.SINGLE, new DirectionsDivision(
                            new DirectionSet(Direction.LEFT, Direction.RIGHT),
                            new DirectionSet(Direction.UP, Direction.DOWN)))),
    FULL_FOUR(FullFour.class, 1, Single.values(),
            ImmutableBiMap.of(Single.SINGLE, new DirectionsDivision(
                    new DirectionSet(Direction.values())))),
    SOURCE_ONE(SourceOne.class, 1, Direction.values(), TARGET.getDirectionsDivisions()),
    SOURCE_STRAIGHT(SourceStraight.class, 1, Orientation.values(), STRAIGHT.getDirectionsDivisions()),
    SOURCE_TURN(SourceTurn.class, 1, TurnAngle.values(), TURN.getDirectionsDivisions()),
    SOURCE_THREE(SourceThree.class, 1, ThreeAngle.values(), THREE.getDirectionsDivisions()),
    SOURCE_FOUR(SourceFour.class, 1, Single.values(), FULL_FOUR.getDirectionsDivisions());

    private static class Holder {
        public static Map<Class<? extends Block>, BlockType> blockTypeToEnum = Maps.newHashMap();
    }

    private int coloursCount;
    private Class<? extends Block> javaClass;
    private List<Angle> initialAngles;
    private ImmutableBiMap<Angle, DirectionsDivision> directionsDivisions;

    BlockType(Class<? extends Block> javaClass, int coloursCount, Angle[] initialAngles, ImmutableBiMap<Angle, DirectionsDivision> directionsDivisions) {
        this.coloursCount = coloursCount;
        this.javaClass = javaClass;
        this.initialAngles = Arrays.asList(initialAngles);
        this.directionsDivisions = directionsDivisions;
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

    public static List<BlockType> valuesList = Arrays.asList(values());

    public ImmutableBiMap<Angle, DirectionsDivision> getDirectionsDivisions() {
        return directionsDivisions;
    }
}

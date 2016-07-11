package colourshift.model.blocks;

import colourshift.model.Direction;
import colourshift.model.angle.*;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public static List<BlockType> valuesList = Arrays.asList(values());

}

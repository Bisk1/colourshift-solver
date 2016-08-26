import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.angle.Orientation;
import colourshift.model.angle.Single;
import colourshift.model.angle.ThreeAngle;
import colourshift.model.angle.TurnAngle;
import colourshift.model.blocks.*;
import colourshift.model.border.BorderMap;
import colourshift.model.border.BorderRequirement;
import colourshift.model.border.BorderStatus;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;

public class PropagationTest {

    private void mockBorderMap(Block block, Map<Direction, BorderRequirement> directionsToBorderRequirement, Set<Direction> statusDirectionsSetByMe) {
        BorderMap.Builder otherSideBorderMapBuilder = new BorderMap.Builder();

        BorderMap.Builder borderMapBuilder = new BorderMap.Builder();
        Block neighbour = mock(Block.class);

        for (Direction direction : directionsToBorderRequirement.keySet()) {
            borderMapBuilder.createAndSetBorder(direction, block, neighbour);
            otherSideBorderMapBuilder.setBorder(direction.opposite(), borderMapBuilder.getBorder(direction));
        }
        BorderMap borderMap = borderMapBuilder.build(block);
        BorderMap otherSideBorderMap = otherSideBorderMapBuilder.build(neighbour);

        block.setBorderMap(borderMap);

        for (Map.Entry<Direction, BorderRequirement> directionToBorderRequirement : directionsToBorderRequirement.entrySet()) {
            Direction direction = directionToBorderRequirement.getKey();
            BorderRequirement borderRequirement = directionToBorderRequirement.getValue();
            if (statusDirectionsSetByMe.contains(direction)) {
                borderMap.getBorderView(direction).get().updateBorderStatus(borderRequirement);
            } else {
                otherSideBorderMap.getBorderView(direction.opposite()).get().updateBorderStatus(borderRequirement);
            }
        }

    }

    private void mockBorderMap(Block block, Map<Direction, BorderRequirement> directionsToBorderRequirement) {
        mockBorderMap(block, directionsToBorderRequirement, Sets.newHashSet());
    }

    @Test
    public void targetWithOnlyOnePossibleBorderShouldSetItAsMandatory() {
        Target target = new Target(Colour.GREEN);
        mockBorderMap(target, ImmutableMap.of(
                Direction.LEFT, BorderRequirement.indifferent(),
                Direction.UP, BorderRequirement.unknown(),
                Direction.RIGHT, BorderRequirement.indifferent(),
                Direction.DOWN, BorderRequirement.indifferent()
        ));

        target.getSolver().bordersUpdated();

        Assert.assertThat(target.getAngle(), is(equalTo(Direction.UP)));
        Assert.assertThat(target.getFeasibleAngles(), is(equalTo(Sets.newHashSet(Direction.UP))));
        Assert.assertTrue(target.getBorderMap().getBorderView(Direction.UP).get().getBorderRequirement().getBorderStatus() == BorderStatus.MUST_SEND);
    }

    @Test
    public void targetShouldSetAllPossibleBordersToCannotSend() {
        Target target = new Target(Colour.GREEN);

        mockBorderMap(target, ImmutableMap.of(
                Direction.LEFT, BorderRequirement.unknown(),
                Direction.UP, BorderRequirement.unknown(),
                Direction.RIGHT, BorderRequirement.unknown(),
                Direction.DOWN, BorderRequirement.unknown()
        ));

        target.getSolver().bordersUpdated();

        for (Direction direction : Direction.values()) {
            Assert.assertTrue(target.getBorderMap().getBorderView(direction).get().getBorderRequirement().getBorderStatus() == BorderStatus.CANNOT_SEND);
        }
    }

    @Test
    public void turnWithMandatoryBorderShouldSetAllPossibleBordersToCannotSend() {
        Turn turn = new Turn();

        mockBorderMap(turn, ImmutableMap.of(
                Direction.LEFT, BorderRequirement.unknown(),
                Direction.UP, BorderRequirement.unknown(),
                Direction.RIGHT, BorderRequirement.unknown(),
                Direction.DOWN, BorderRequirement.mustSend(Colour.GREEN)
        ));

        turn.getSolver().bordersUpdated();

        Assert.assertEquals(BorderStatus.INDIFFERENT, turn.getBorderMap().getBorderView(Direction.UP).get().getBorderRequirement().getBorderStatus());
        Assert.assertEquals(BorderStatus.CANNOT_SEND, turn.getBorderMap().getBorderView(Direction.LEFT).get().getBorderRequirement().getBorderStatus());
        Assert.assertEquals(BorderStatus.CANNOT_SEND, turn.getBorderMap().getBorderView(Direction.RIGHT).get().getBorderRequirement().getBorderStatus());
    }

    @Test
    public void turnWithMandatoryBorderAndCannotReceiveBorderShouldForbidCannotSendBorderAndSetItToIndifferent() {
        Turn turn = new Turn();

        mockBorderMap(turn, ImmutableMap.of(
                Direction.LEFT, BorderRequirement.unknown(),
                Direction.UP, BorderRequirement.unknown(),
                Direction.RIGHT, BorderRequirement.cannotSend(),
                Direction.DOWN, BorderRequirement.mustSend(Colour.GREEN)
        ));

        turn.getSolver().bordersUpdated();

        Assert.assertEquals(Sets.newHashSet(TurnAngle.LEFT_DOWN), turn.getFeasibleAngles());

        Assert.assertEquals(BorderStatus.INDIFFERENT, turn.getBorderMap().getBorderView(Direction.UP).get().getBorderRequirement().getBorderStatus());
        Assert.assertEquals(BorderStatus.MUST_SEND, turn.getBorderMap().getBorderView(Direction.LEFT).get().getBorderRequirement().getBorderStatus());
        Assert.assertEquals(BorderStatus.INDIFFERENT, turn.getBorderMap().getBorderView(Direction.RIGHT).get().getBorderRequirement().getBorderStatus());
    }

    @Test
    public void straightBlockWithOneUnusedBorderShouldFixAngleAndSetOppositeSideAsUnused() {
        Block straight = new Straight();

        mockBorderMap(straight, ImmutableMap.of(
                Direction.LEFT, BorderRequirement.indifferent(),
                Direction.UP, BorderRequirement.unknown(),
                Direction.RIGHT, BorderRequirement.unknown(),
                Direction.DOWN, BorderRequirement.unknown()
        ));

        straight.getSolver().bordersUpdated();

        Assert.assertThat(straight.getAngle(), is(equalTo(Orientation.VERTICAL)));
        Assert.assertEquals(BorderStatus.INDIFFERENT,
                straight.getBorderMap().getBorderView(Direction.RIGHT).get().getBorderRequirement().getBorderStatus());
    }

    @Test
    public void blockWithMandatoryBorderShouldForbidAnglesWithoutThisBorder() {
        Block turn = new Turn();

        mockBorderMap(turn, ImmutableMap.of(
                Direction.LEFT, BorderRequirement.unknown(),
                Direction.UP, BorderRequirement.unknown(),
                Direction.RIGHT, BorderRequirement.unknown(),
                Direction.DOWN, BorderRequirement.mustSend(Colour.GREEN)
        ));

        turn.getSolver().bordersUpdated();

        Assert.assertEquals(Sets.newHashSet(TurnAngle.LEFT_DOWN, TurnAngle.RIGHT_DOWN), turn.getFeasibleAngles());
        Assert.assertEquals(BorderStatus.INDIFFERENT, turn.getBorderMap().getBorderView(Direction.UP).get().getBorderRequirement().getBorderStatus());
    }

    @Test
    public void blockThatMustUseIndifferentBorderIsValid() {
        HalfFour halfFour = new HalfFour();

        mockBorderMap(halfFour, ImmutableMap.of(
                Direction.LEFT, BorderRequirement.unknown(),
                Direction.UP, BorderRequirement.indifferent(),
                Direction.RIGHT, BorderRequirement.unknown(),
                Direction.DOWN, BorderRequirement.unknown()
        ));

        halfFour.getSolver().bordersUpdated();

        Assert.assertEquals(Sets.newHashSet(Single.SINGLE), halfFour.getFeasibleAngles());
    }


    @Test
    public void mandatoryBorderSetByTheBlockShouldNotBeRecognizedAsSender() {
        Block turn = new Turn();

        mockBorderMap(turn,
                ImmutableMap.of(
                        Direction.LEFT, BorderRequirement.mustSend(Colour.GREEN),
                        Direction.UP, BorderRequirement.indifferent(),
                        Direction.RIGHT, BorderRequirement.indifferent(),
                        Direction.DOWN, BorderRequirement.mustSend(Colour.GREEN)
                ),
                Sets.newHashSet(Direction.LEFT));

        turn.getSolver().bordersUpdated();

        Assert.assertEquals(Sets.newHashSet(TurnAngle.LEFT_DOWN), turn.getFeasibleAngles());
    }

    @Test
    public void setCanReceiveWhenDirectionSetHasMandatoryReceiver() {
        Block block = new Three();

        mockBorderMap(block,
                ImmutableMap.of(
                        Direction.LEFT, BorderRequirement.cannotSend(),
                        Direction.UP, BorderRequirement.canReceive(Colour.GREEN),
                        Direction.RIGHT, BorderRequirement.mustSend(Colour.GREEN),
                        Direction.DOWN, BorderRequirement.indifferent()
                ));

        block.getSolver().bordersUpdated();

        Assert.assertEquals(BorderStatus.CAN_RECEIVE, block.getBorderMap().getBorderView(Direction.LEFT).get().getBorderRequirement().getBorderStatus());
        Assert.assertEquals(Sets.newHashSet(ThreeAngle.NOT_DOWN), block.getFeasibleAngles());
    }

    @Test
    public void setCanReceiveWhenDirectionSetHasNonMandatoryReceiver() {
        Block block = new Straight();

        mockBorderMap(block,
                ImmutableMap.of(
                        Direction.LEFT, BorderRequirement.cannotSend(),
                        Direction.UP, BorderRequirement.indifferent(),
                        Direction.RIGHT, BorderRequirement.canReceive(Colour.GREEN),
                        Direction.DOWN, BorderRequirement.indifferent()
                ));

        block.getSolver().bordersUpdated();

        Assert.assertEquals(BorderStatus.CAN_RECEIVE, block.getBorderMap().getBorderView(Direction.LEFT).get().getBorderRequirement().getBorderStatus());
        Assert.assertEquals(Sets.newHashSet(Orientation.HORIZONTAL), block.getFeasibleAngles());
    }

    @Test
    public void fixedSourceShouldSetProvidedWherePossible() {
        Block block = new SourceStraight(Colour.GREEN);

        mockBorderMap(block,
                ImmutableMap.of(
                        Direction.LEFT, BorderRequirement.unknown(),
                        Direction.UP, BorderRequirement.unknown(),
                        Direction.RIGHT, BorderRequirement.unknown(),
                        Direction.DOWN, BorderRequirement.mustSend(Colour.GREEN))
        );

        block.getSolver().bordersUpdated();

        Assert.assertEquals(Sets.newHashSet(Orientation.VERTICAL), block.getFeasibleAngles());
        Assert.assertEquals(BorderStatus.CAN_RECEIVE, block.getBorderMap().getBorderView(Direction.UP).get().getBorderRequirement().getBorderStatus());
    }

}

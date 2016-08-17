import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.angle.Orientation;
import colourshift.model.angle.TurnAngle;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.Straight;
import colourshift.model.blocks.Target;
import colourshift.model.blocks.Turn;
import colourshift.model.border.Border;
import colourshift.model.border.BorderMap;
import colourshift.model.border.BorderStatus;
import colourshift.model.border.BorderView;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

public class PropagationTest {


    private BorderMap mockBorderMap(Block block, Map<Direction, BorderStatus> directionToBorderStatus) {
        BorderMap borderMapMock = mock(BorderMap.class);

        for (Direction direction : Direction.values()) {
            Border borderMock = mock(Border.class);
            when(borderMock.getBorderStatus()).thenReturn(directionToBorderStatus.get(direction));

            BorderView borderView = new BorderView(borderMock, block);
            BorderView borderViewSpy = spy(borderView);

            when(borderMapMock.getBorderView(direction)).thenReturn(Optional.of(borderViewSpy));
            when(borderMapMock.contains(direction)).thenReturn(true);
            when(borderMapMock.getExistingBordersDirections()).thenReturn(directionToBorderStatus.keySet());
        }
        return borderMapMock;
    }

    @Test
    public void targetWithOnlyOnePossibleBorderShouldSetItAsMandatory() {
        Target target = new Target(Colour.GREEN);
        BorderMap borderMapMock = mockBorderMap(target, ImmutableMap.of(
                Direction.LEFT, BorderStatus.INDIFFERENT,
                Direction.UP, BorderStatus.UNKNOWN,
                Direction.RIGHT, BorderStatus.INDIFFERENT,
                Direction.DOWN, BorderStatus.INDIFFERENT
        ));
        target.setBorderMap(borderMapMock);

        target.getSolver().bordersUpdated();

        Assert.assertThat(target.getAngle(), is(equalTo(Direction.UP)));
        Assert.assertThat(target.getFeasibleAngles(), is(equalTo(Sets.newHashSet(Direction.UP))));
        verify(borderMapMock.getBorderView(Direction.UP).get()).updateBorderStatus(BorderStatus.MANDATORY);
    }

    @Test
    public void targetShouldSetAllPossibleBordersToCannotSend() {
        Target target = new Target(Colour.GREEN);

        BorderMap borderMapMock = mockBorderMap(target, ImmutableMap.of(
                Direction.LEFT, BorderStatus.UNKNOWN,
                Direction.UP, BorderStatus.UNKNOWN,
                Direction.RIGHT, BorderStatus.UNKNOWN,
                Direction.DOWN, BorderStatus.UNKNOWN
        ));
        target.setBorderMap(borderMapMock);

        target.getSolver().bordersUpdated();

        for (Direction direction : Direction.values()) {
            verify(borderMapMock.getBorderView(direction).get()).updateBorderStatus(BorderStatus.CANNOT_SEND);
        }
    }

    @Test
    public void turnWithMandatoryBorderShouldSetAllPossibleBordersToCannotSend() {
        Turn turn = new Turn();

        BorderMap borderMapMock = mockBorderMap(turn, ImmutableMap.of(
                Direction.LEFT, BorderStatus.UNKNOWN,
                Direction.UP, BorderStatus.UNKNOWN,
                Direction.RIGHT, BorderStatus.UNKNOWN,
                Direction.DOWN, BorderStatus.MANDATORY
        ));
        turn.setBorderMap(borderMapMock);

        turn.getSolver().bordersUpdated();

        verify(borderMapMock.getBorderView(Direction.UP).get()).updateBorderStatus(BorderStatus.INDIFFERENT);
        verify(borderMapMock.getBorderView(Direction.LEFT).get()).updateBorderStatus(BorderStatus.CANNOT_SEND);
        verify(borderMapMock.getBorderView(Direction.RIGHT).get()).updateBorderStatus(BorderStatus.CANNOT_SEND);
    }

    @Test
    public void turnWithMandatoryBorderAndCannotReceiveBorderShouldForbidCannotSendBorderAndSetItToIndifferent() {
        Turn turn = new Turn();

        BorderMap borderMapMock = mockBorderMap(turn, ImmutableMap.of(
                Direction.LEFT, BorderStatus.UNKNOWN,
                Direction.UP, BorderStatus.UNKNOWN,
                Direction.RIGHT, BorderStatus.CANNOT_SEND,
                Direction.DOWN, BorderStatus.MANDATORY
        ));
        turn.setBorderMap(borderMapMock);

        turn.getSolver().bordersUpdated();

        Assert.assertEquals(Sets.newHashSet(TurnAngle.LEFT_DOWN), turn.getFeasibleAngles());
        verify(borderMapMock.getBorderView(Direction.UP).get()).updateBorderStatus(BorderStatus.INDIFFERENT);
        verify(borderMapMock.getBorderView(Direction.LEFT).get()).updateBorderStatus(BorderStatus.MANDATORY);
        verify(borderMapMock.getBorderView(Direction.RIGHT).get()).updateBorderStatus(BorderStatus.INDIFFERENT);
    }

    @Test
    public void straightBlockWithOneUnusedBorderShouldFixAngleAndSetOppositeSideAsUnused() {
        Block straight = new Straight();

        BorderMap borderMapMock = mockBorderMap(straight, ImmutableMap.of(
                Direction.LEFT, BorderStatus.INDIFFERENT,
                Direction.UP, BorderStatus.UNKNOWN,
                Direction.RIGHT, BorderStatus.UNKNOWN,
                Direction.DOWN, BorderStatus.UNKNOWN
        ));
        straight.setBorderMap(borderMapMock);

        straight.getSolver().bordersUpdated();

        Assert.assertThat(straight.getAngle(), is(equalTo(Orientation.VERTICAL)));
        verify(borderMapMock.getBorderView(Direction.RIGHT).get()).updateBorderStatus(BorderStatus.INDIFFERENT);
    }

    @Test
    public void blockWithMandatoryBorderShouldForibdAnglesWithoutThisBorder() {
        Block turn = new Turn();

        BorderMap borderMapMock = mockBorderMap(turn, ImmutableMap.of(
                Direction.LEFT, BorderStatus.UNKNOWN,
                Direction.UP, BorderStatus.UNKNOWN,
                Direction.RIGHT, BorderStatus.UNKNOWN,
                Direction.DOWN, BorderStatus.MANDATORY
        ));
        turn.setBorderMap(borderMapMock);

        turn.getSolver().bordersUpdated();

        Assert.assertEquals(Sets.newHashSet(TurnAngle.LEFT_DOWN, TurnAngle.RIGHT_DOWN), turn.getFeasibleAngles());
        verify(borderMapMock.getBorderView(Direction.UP).get()).updateBorderStatus(BorderStatus.INDIFFERENT);
    }


}

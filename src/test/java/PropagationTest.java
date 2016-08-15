import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.angle.Orientation;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.FullFour;
import colourshift.model.blocks.Straight;
import colourshift.model.border.Border;
import colourshift.model.border.BorderMap;
import colourshift.model.border.BorderStatus;
import colourshift.model.border.BorderView;
import colourshift.model.power.Power;
import colourshift.solver.BlockSolver;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PropagationTest {


    private BorderMap mockBorderMap(Map<Direction, BorderStatus> directionToBorderStatus) {
        BorderMap borderMapMock = mock(BorderMap.class);

        for (Direction direction : Direction.values()) {
            BorderView borderViewMock = mock(BorderView.class);
            when(borderViewMock.getBorderStatus()).thenReturn(directionToBorderStatus.get(direction));

            when(borderMapMock.getBorderView(direction)).thenReturn(Optional.of(borderViewMock));
            when(borderMapMock.contains(direction)).thenReturn(true);
        }
        return borderMapMock;
    }

    @Test
    public void straightBlockWithOneUnusedBorderShouldFixAngleAndSetOppositeSideAsUnused() {
        Block straight = new Straight();
        Map<Direction, BorderStatus> directionToBorderStatus = ImmutableMap.of(
                Direction.LEFT, BorderStatus.UNUSED,
                Direction.UP, BorderStatus.UNKNOWN,
                Direction.RIGHT, BorderStatus.UNKNOWN,
                Direction.DOWN, BorderStatus.UNKNOWN
        );

        BorderMap borderMapMock = mockBorderMap(directionToBorderStatus);
        straight.setBorderMap(borderMapMock);

        straight.getSolver().statusUpdated();

        Assert.assertThat(straight.getAngle(), is(equalTo(Orientation.VERTICAL)));
        BorderView rightBorderViewMock = borderMapMock.getBorderView(Direction.RIGHT).orElseThrow(() -> new RuntimeException("Missing border"));
        verify(rightBorderViewMock).updateBorderStatus(BorderStatus.UNUSED);
    }
}

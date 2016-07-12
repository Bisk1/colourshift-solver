package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.power.Power;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Base class for all blocks that after receiving colour signal will generally
 * send them to other blocks.
 */
public abstract class TransitiveBlock extends Block {

	/**
	 * When colour signal is traversing the block, paths help determine in which
	 * directions it should be propagated. For given angle (1st key) and given
	 * direction (2nd key) paths holds the set of all directions that the colour
	 * should be propagated to.
	 */
	private Table<Angle, Direction, DirectionSet> paths;
	
	public TransitiveBlock() {
		super();
		this.paths = buildPaths();
	}

	abstract Map<Angle, DirectionsDivision> getDirectionsDivisions();

	abstract void updatePower(Direction fromDirection, Colour colour);
	
	private Table<Angle, Direction, DirectionSet> buildPaths() {
		Table<Angle, Direction, DirectionSet> paths = HashBasedTable.create();
		for (Entry<Angle, DirectionsDivision> angleToDirDivision : getDirectionsDivisions().entrySet()) {
			Angle angle = angleToDirDivision.getKey();
			for (DirectionSet dirset : angleToDirDivision.getValue()) {
				for (Direction fromDirection : dirset) {
					DirectionSet toDirections = dirset.minus(fromDirection);
					paths.put(angle, fromDirection, toDirections);
				}
			}

		}
		return paths;
	}

	@Override
	public void updateReceived(Direction fromDirection, Colour colour) {
		Map<Direction, DirectionSet> path = paths.row(angle);
		if (!path.containsKey(fromDirection)) {
			return;
		}
		boolean powerChanged = updatePowerAndCheckIfChanged(fromDirection, colour);
		if (!powerChanged) {
			return;
		}
		DirectionSet toDirections = path.get(fromDirection);
		for (Direction toDirection : toDirections) {
			borderMap.send(toDirection, colour);
		}
	}

	private boolean updatePowerAndCheckIfChanged(Direction fromDirection, Colour colour) {
		Power oldPower = getPower().copy();
		updatePower(fromDirection, colour);
		return !oldPower.equals(getPower());
	}

    @Override
    public void fullUpdate() {
        DirectionsDivision directionsDivision = getDirectionsDivisions().get(angle);
        for (DirectionSet directionSet : directionsDivision) {
            Colour fromColour = borderMap.getColourMix(directionSet);
            Direction fromDirection = directionSet.getAny();
            boolean powerChanged = updatePowerAndCheckIfChanged(fromDirection, fromColour);
            if (!powerChanged) {
                continue;
            }
            for (Direction toDirection : directionSet) {
                borderMap.send(toDirection, fromColour);
            }
        }
    }

    @Override
    public void fullClear() {
        for (Direction direction : Direction.values()) {
            borderMap.send(direction, Colour.GREY);
        }
    }

}

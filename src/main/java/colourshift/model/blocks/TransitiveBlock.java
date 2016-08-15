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
import java.util.Optional;

/**
 * Base class for all blocks that after receiving colour signal will generally
 * sendColour them to other blocks.
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
	public void colourUpdateReceived(Direction fromDirection, boolean updateEagerly) {
		Map<Direction, DirectionSet> path = paths.row(angle);
		if (!path.containsKey(fromDirection)) {
			return;
		}
		Optional<DirectionSet> connectedDirectionSet = getDirectionsDivisions().get(angle).get(fromDirection);
        if (!connectedDirectionSet.isPresent()) {
            return;
        }
		Colour dirsetColour = borderMap.getIncomingColourMix(connectedDirectionSet.get());
		boolean powerChanged = updatePowerAndCheckIfChanged(fromDirection, dirsetColour);
		if (!powerChanged && !updateEagerly) {
			return;
		}
		DirectionSet toDirections = path.get(fromDirection);
		for (Direction toDirection : toDirections) {
            DirectionSet otherDirsSet = connectedDirectionSet.get().minus(toDirection);
            Colour otherDirsColour = borderMap.getIncomingColourMix(otherDirsSet);
			borderMap.send(toDirection, otherDirsColour);
		}
	}

	private boolean updatePowerAndCheckIfChanged(Direction fromDirection, Colour colour) {
		Power oldPower = getPower().copy();
		updatePower(fromDirection, colour);
        Power newPower = getPower();
		return !oldPower.equals(newPower);
	}

}

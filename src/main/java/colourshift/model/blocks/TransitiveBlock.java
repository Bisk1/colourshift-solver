package colourshift.model.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.power.Power;

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

	abstract Map<Angle, DirectionsDivision> getDirectonsDivisions();

	abstract void updatePower(Direction fromDirection, Colour colour);
	
	private Table<Angle, Direction, DirectionSet> buildPaths() {
		Table<Angle, Direction, DirectionSet> paths = HashBasedTable.create();
		for (Entry<Angle, DirectionsDivision> angleToDirDivision : getDirectonsDivisions().entrySet()) {
			Angle angle = angleToDirDivision.getKey();
			Map<Direction, DirectionSet> angleDirectionSet = new HashMap<>();
			for (DirectionSet dirset : angleToDirDivision.getValue()) {
				for (Direction fromDirection : dirset) {
					DirectionSet toDirections = dirset.minus(fromDirection);
					angleDirectionSet.put(fromDirection, toDirections);
				}
			}
			paths.rowMap().put(angle, angleDirectionSet);
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
		return oldPower.equals(getPower());
	}

}

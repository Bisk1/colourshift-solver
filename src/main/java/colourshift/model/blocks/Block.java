package colourshift.model.blocks;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.angle.Angle;
import colourshift.model.border.BorderMap;
import colourshift.model.power.Power;
import colourshift.util.IterationUtils;

public abstract class Block {

	/**
	 * Angle can be modified by rotating the block.
	 */
	protected Angle angle;
	/**
	 * Represents 4 borders (except for the blocks on the board border) of the block.
	 * All communication intended to reach neighbouring blocks is handled by this map.
	 */
	protected BorderMap borderMap;
	/**
	 * All angles that are feasible for specific block type. This list defines also order of rotating.
	 */
	private List<? extends Angle> initialAngles;
	/**
	 * All angles that are still plausible for this block for a valid solution of the board.
	 */
	private Set<Angle> feasibleAngles;

	public Block() {
		this.initialAngles = getInitialAngles();
		this.feasibleAngles = Sets.newHashSet(initialAngles);
		this.angle = initialAngles.get(0);
	}

	public void setBorderMap(BorderMap borderMap) {
		this.borderMap = borderMap;
	}

	public abstract void updateReceived(Direction fromDirection, Colour colour);
	
	protected abstract List<? extends Angle> getInitialAngles();
	
	public boolean isFixed() {
		return feasibleAngles.size() == 1;
	}

	public void rotate() {
		if (!isFixed()) {
			angle = IterationUtils.getNextFromListIfInSet(angle, initialAngles, feasibleAngles);
		}
	}
	
	public Angle getAngle() {
		return angle;
	}
	

	public abstract Power getPower();
}

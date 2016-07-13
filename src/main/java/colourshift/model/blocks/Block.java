package colourshift.model.blocks;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.angle.Angle;
import colourshift.model.border.BorderMap;
import colourshift.model.power.Power;
import colourshift.util.IterationUtils;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public abstract class Block implements Serializable {

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
		this.initialAngles = BlockType.fromJavaClass(this.getClass()).getInitialAngles();
		this.feasibleAngles = Sets.newHashSet(initialAngles);
		this.angle = initialAngles.get(0);
	}

	public void setBorderMap(BorderMap borderMap) {
		this.borderMap = borderMap;
	}

	public abstract void updateReceived(Direction fromDirection, Colour colour, boolean updateEagerly);

    public abstract void fullUpdate();

    public abstract void fullClear();

	public boolean isFixed() {
		return feasibleAngles.size() == 1;
	}

	public void rotate() {
        fullClear();
		if (!isFixed()) {
			angle = IterationUtils.getNextFromListIfInSet(angle, initialAngles, feasibleAngles);
		}
        fullUpdate();
	}
	
	public Angle getAngle() {
		return angle;
	}

	public abstract Power getPower();

	public BorderMap getBorderMap() {
		return borderMap;
	}
}

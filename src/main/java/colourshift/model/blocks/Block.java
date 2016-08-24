package colourshift.model.blocks;

import colourshift.model.Direction;
import colourshift.model.DirectionsDivision;
import colourshift.model.angle.Angle;
import colourshift.model.border.BorderMap;
import colourshift.model.power.Power;
import colourshift.solver.BlockSolver;
import colourshift.solver.UnsolvableException;
import colourshift.util.IterationUtils;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public abstract class Block implements Serializable {
	private static long serialVersionUID = 0L;

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

	private BlockSolver solver;

	public Block() {
		this.initialAngles = BlockType.fromJavaClass(this.getClass()).getInitialAngles();
		this.feasibleAngles = Sets.newHashSet(initialAngles);
		this.angle = initialAngles.get(0);
		this.solver = BlockSolver.create(this);
	}

	public void setBorderMap(BorderMap borderMap) {
		this.borderMap = borderMap;
	}

	public abstract void colourUpdateReceived(Direction fromDirection, boolean updateEagerly);

	public void statusUpdateReceived() {
		solver.bordersUpdated();
	}

    public void resetPower() {
        getPower().reset();
        borderMap.reset(this);
    }

	public boolean isFixed() {
		return feasibleAngles.size() == 1;
	}

	public void rotate() {
		if (!isFixed()) {
			angle = IterationUtils.getNextFromListIfInSet(angle, initialAngles, feasibleAngles);
		}
	}

    /**
     * Remove the angle from the set of feasible angles
     * and update the current angle so that it is within the
     * feasible angles set.
     *
     * @param angleToForbid angle to forbid
     */
    public void forbidAngle(Angle angleToForbid) {
        feasibleAngles.remove(angleToForbid);
		if (feasibleAngles.isEmpty()) {
			throw new UnsolvableException();
		}
        if (angleToForbid == angle) {
            angle = feasibleAngles.iterator().next();
        }
    }

	/**
	 * Make the specified angle the only feasible one. Also set it as current.
	 * @param angleToFix
     */
	public void fixAngle(Angle angleToFix) {
		feasibleAngles = Sets.newHashSet(angleToFix);
		angle = angleToFix;
	}

	public Angle getAngle() {
		return angle;
	}

	public abstract Power getPower();

	public BorderMap getBorderMap() {
		return borderMap;
	}

	public Set<Angle> getFeasibleAngles() {
		return feasibleAngles;
	}

	// TODO: can keep reference to blockType instance as a member rather than calling fromJavaClass every time?
	public ImmutableBiMap<Angle, DirectionsDivision> getDirectionsDivisions() {
		return BlockType.fromJavaClass(this.getClass()).getDirectionsDivisions();
	}

    public BlockSolver getSolver() {
        return solver;
    }

    //debugging
    private int row;
	private int col;
    public void setPosition(int row, int col) {
    	this.row = row;
		this.col = col;
	}

	public int getRow() {
	    return row;
    }

    public int getCol() {
        return col;
    }
}

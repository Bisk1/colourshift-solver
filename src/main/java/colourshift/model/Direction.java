package colourshift.model;

import colourshift.model.angle.Angle;

public enum Direction implements Angle {
	LEFT,
	UP,
	RIGHT,
	DOWN;
	
	public Direction opposite() {
		switch (this) {
			case LEFT: return RIGHT;
			case UP: return DOWN;
			case RIGHT: return LEFT;
			case DOWN: return UP;
			default: throw new RuntimeException("Unknown direction: " + this);
		}
	}
}

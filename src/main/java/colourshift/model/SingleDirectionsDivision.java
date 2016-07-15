package colourshift.model;

/**
 * Direction division that has exactly one direction set
 */
public class SingleDirectionsDivision extends DirectionsDivision {
	private DirectionSet directionSet;

    public SingleDirectionsDivision(Direction... directions) {
        this(new DirectionSet(directions));
    }

	public SingleDirectionsDivision(DirectionSet directionSet) {
        super(directionSet);
		this.directionSet = directionSet;
	}

	public DirectionSet getDirectionSet() {
        return directionSet;
    }
}

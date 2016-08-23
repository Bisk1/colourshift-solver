package colourshift.solver;

public class UnsolvableException extends RuntimeException {
    public UnsolvableException() {
        super();
    }

    public UnsolvableException(String message) {
        super(message);
    }
}

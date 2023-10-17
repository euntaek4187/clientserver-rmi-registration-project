package Exception;
public class NullDataException extends Exception {
	private static final long serialVersionUID = 1L;
    private final String errorMessage;
    public NullDataException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
    @Override
    public String toString() {
        return errorMessage;
    }
}

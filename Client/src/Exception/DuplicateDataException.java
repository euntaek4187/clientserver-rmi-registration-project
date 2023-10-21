package Exception;
public class DuplicateDataException extends Exception {
	private static final long serialVersionUID = 1L;
	private final String errorMessage;
    public DuplicateDataException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
    @Override
    public String toString() {
        return errorMessage;
    }
}
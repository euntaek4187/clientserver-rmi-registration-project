package Exception;
public class AuthenticationException extends Exception {
	private static final long serialVersionUID = 1L;
	private final String errorMessage;
    public AuthenticationException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
    @Override
    public String toString() {
        return errorMessage;
    }
}

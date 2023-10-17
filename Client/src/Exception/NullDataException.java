package Exception;
public class NullDataException extends Exception {
	private static final long serialVersionUID = 1L;
	NullDataException(String errormessage){
		super(errormessage);
	}
}
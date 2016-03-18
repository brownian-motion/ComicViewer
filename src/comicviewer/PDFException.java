package comicviewer;
/**
 * This class is an exception intended to be used by the PDFNode class and others that manage PDFFile objects and encounter an error.
 * @author Jack
 *
 */
public class PDFException extends RuntimeException {

	public PDFException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PDFException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public PDFException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PDFException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PDFException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}

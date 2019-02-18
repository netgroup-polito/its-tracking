package it.polito.dp2.rest.rns.exceptions;

@SuppressWarnings("serial")
public class UnsatisfiableException extends Exception {

	public UnsatisfiableException() {
		super();
	}

	public UnsatisfiableException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnsatisfiableException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsatisfiableException(String message) {
		super(message);
	}

	public UnsatisfiableException(Throwable cause) {
		super(cause);
	}
	
}

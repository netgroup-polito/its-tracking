package it.polito.dp2.rest.rns.exceptions;

@SuppressWarnings("serial")
public class InvalidPathException extends Exception {

	public InvalidPathException() {
	}

	public InvalidPathException(String arg0) {
		super(arg0);
	}

	public InvalidPathException(Throwable cause) {
		super(cause);
	}

	public InvalidPathException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPathException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

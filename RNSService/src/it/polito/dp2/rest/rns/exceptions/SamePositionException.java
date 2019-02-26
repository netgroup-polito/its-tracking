package it.polito.dp2.rest.rns.exceptions;

@SuppressWarnings("serial")
public class SamePositionException extends Exception {

	public SamePositionException() {
	}

	public SamePositionException(String arg0) {
		super(arg0);
	}

	public SamePositionException(Throwable cause) {
		super(cause);
	}

	public SamePositionException(String message, Throwable cause) {
		super(message, cause);
	}

	public SamePositionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

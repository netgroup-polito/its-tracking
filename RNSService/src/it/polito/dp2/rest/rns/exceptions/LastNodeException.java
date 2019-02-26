package it.polito.dp2.rest.rns.exceptions;

@SuppressWarnings("serial")
public class LastNodeException extends Exception {

	public LastNodeException() {
	}

	public LastNodeException(String message) {
		super(message);
	}

	public LastNodeException(Throwable cause) {
		super(cause);
	}

	public LastNodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public LastNodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

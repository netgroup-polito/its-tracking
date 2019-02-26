package it.polito.dp2.rest.rns.exceptions;

@SuppressWarnings("serial")
public class InvalidDestinationPlaceException extends Exception {

	public InvalidDestinationPlaceException() {
	}

	public InvalidDestinationPlaceException(String message) {
		super(message);
	}

	public InvalidDestinationPlaceException(Throwable cause) {
		super(cause);
	}

	public InvalidDestinationPlaceException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDestinationPlaceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

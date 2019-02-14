package it.polito.dp2.rest.rns.exceptions;

@SuppressWarnings("serial")
public class InvalidEntryPlaceException extends Exception {

	public InvalidEntryPlaceException() {
	}

	public InvalidEntryPlaceException(String message) {
		super(message);
	}

	public InvalidEntryPlaceException(Throwable cause) {
		super(cause);
	}

	public InvalidEntryPlaceException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidEntryPlaceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}

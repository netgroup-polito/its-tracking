package it.polito.dp2.rest.rns.graph;

@SuppressWarnings("serial")
public class PlaceFullException extends Exception {

	public PlaceFullException() {
	}

	public PlaceFullException(String message) {
		super(message);
	}

	public PlaceFullException(Throwable cause) {
		super(cause);
	}

	public PlaceFullException(String message, Throwable cause) {
		super(message, cause);
	}

	public PlaceFullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

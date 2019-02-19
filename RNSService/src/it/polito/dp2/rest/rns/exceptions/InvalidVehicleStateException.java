package it.polito.dp2.rest.rns.exceptions;

@SuppressWarnings("serial")
public class InvalidVehicleStateException extends Exception {

	public InvalidVehicleStateException() {
	}

	public InvalidVehicleStateException(String message) {
		super(message);
	}

	public InvalidVehicleStateException(Throwable cause) {
		super(cause);
	}

	public InvalidVehicleStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidVehicleStateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

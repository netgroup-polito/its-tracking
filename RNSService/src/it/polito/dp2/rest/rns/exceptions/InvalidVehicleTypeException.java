package it.polito.dp2.rest.rns.exceptions;

@SuppressWarnings("serial")
public class InvalidVehicleTypeException extends Exception {

	public InvalidVehicleTypeException() {
	}

	public InvalidVehicleTypeException(String arg0) {
		super(arg0);
	}

	public InvalidVehicleTypeException(Throwable cause) {
		super(cause);
	}

	public InvalidVehicleTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidVehicleTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

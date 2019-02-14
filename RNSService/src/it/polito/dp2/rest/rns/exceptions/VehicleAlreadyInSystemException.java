package it.polito.dp2.rest.rns.exceptions;

@SuppressWarnings("serial")
public class VehicleAlreadyInSystemException extends Exception {

	public VehicleAlreadyInSystemException() {
	}

	public VehicleAlreadyInSystemException(String message) {
		super(message);
	}

	public VehicleAlreadyInSystemException(Throwable cause) {
		super(cause);
	}

	public VehicleAlreadyInSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public VehicleAlreadyInSystemException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

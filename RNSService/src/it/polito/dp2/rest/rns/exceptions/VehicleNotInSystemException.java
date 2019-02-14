package it.polito.dp2.rest.rns.exceptions;

@SuppressWarnings("serial")
public class VehicleNotInSystemException extends Exception {
	public VehicleNotInSystemException() {
		super();
	}

	public VehicleNotInSystemException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public VehicleNotInSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public VehicleNotInSystemException(String message) {
		super(message);
	}

	public VehicleNotInSystemException(Throwable cause) {
		super(cause);
	}
	
}

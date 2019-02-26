package it.polito.dp2.rest.rns.exceptions;

@SuppressWarnings("serial")
public class InvalidEntryTimeException extends Exception {

	public InvalidEntryTimeException() {
	}

	public InvalidEntryTimeException(String arg0) {
		super(arg0);
	}

	public InvalidEntryTimeException(Throwable arg0) {
		super(arg0);
	}

	public InvalidEntryTimeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidEntryTimeException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}

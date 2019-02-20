package it.polito.dp2.rest.rns.exceptions;

@SuppressWarnings("serial")
public class IncompatibleMaterialsCarriedException extends Exception {

	public IncompatibleMaterialsCarriedException() {
	}

	public IncompatibleMaterialsCarriedException(String arg0) {
		super(arg0);
	}

	public IncompatibleMaterialsCarriedException(Throwable arg0) {
		super(arg0);
	}

	public IncompatibleMaterialsCarriedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public IncompatibleMaterialsCarriedException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}

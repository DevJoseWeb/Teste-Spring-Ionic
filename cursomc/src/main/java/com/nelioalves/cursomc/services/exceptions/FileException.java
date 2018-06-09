package com.nelioalves.cursomc.services.exceptions;

//excessao vai ser lançada caso alguma autorizacao manual de problema
public class FileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileException(String msg) {
		super(msg);
	}

	public FileException(String msg, Throwable cause) {
		super(msg, cause);
	}
}

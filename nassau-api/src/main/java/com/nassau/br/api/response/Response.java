package com.nassau.br.api.response;

/**
 * Resposta de uma requisição
 * @author fabio
 */
public class Response {
	public static enum Status {
		OK,
		ERROR;
	}
	
	private Status status;
	private String message;

	/**
	 * Resposta padrão! :P
	 */
	public Response() {
		this(Status.OK, null);
	}
	
	/**
	 * Construtor parametrizado
	 * @param status
	 * @param message
	 */
	public Response(Status status, String message) {
		super();
		this.status 	= status;
		this.message 	= message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}

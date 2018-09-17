package javache.http;

import javache.WebConstants;

public enum HttpStatus {
	OK("200 OK"),
	Created("201 Created"),
	NoContent("204 No Content"),
	SeeOther("303 See Other"),
	BadRequest("400 Bad Request"),
	Unauthorized("401 Unauthorized"),
	Forbidden("403 Forbidden"),
	NotFound("404 Not Found"),
	InternalServerError("500 Internal Server Error");

	private String statusPhrase;

	private HttpStatus(String statusPhrase) {
		this.statusPhrase = statusPhrase;
	}
	
	public String getStatusPhrase() {
		return statusPhrase;
	}

	public void setStatusPhrase(String statusPhrase) {
		this.statusPhrase = statusPhrase;
	}

	public int getStatusCode() {
		return Integer.parseInt(this.statusPhrase.split(" ")[0]);
	}
	
	
	
	
}

package javache.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpResponseImpl implements HttpResponse {

	private HashMap<String, String> headers;
	
	private HashMap<String, HttpCookie> cookies;

	private HttpStatus statusCode;

	private byte[] content;

	public HttpResponseImpl() {
		this.setContent(new byte[0]);
		this.headers = new HashMap<>();
		this.cookies = new HashMap<>();
	}

	@Override
	public HashMap<String, String> getHeaders() {
		return this.headers;
	}

	@Override
	public HttpStatus getStatusCode() {
		return this.statusCode;
	}

	@Override
	public byte[] getBytes() {
		byte[] headersBytes = this.getHeadersBytes();
		byte[] bodyBytes = this.getContent();

		byte[] fullResponse = new byte[headersBytes.length + bodyBytes.length];

		System.arraycopy(headersBytes, 0, fullResponse, 0, headersBytes.length);
		System.arraycopy(bodyBytes, 0, fullResponse, headersBytes.length, bodyBytes.length);

		return fullResponse;
	}

	public byte[] getContent() {
		return content;
	}

	@Override
	public void setStatusCode(HttpStatus StatusCode) {
		this.statusCode = StatusCode;

	}

	@Override
	public void setContent(byte[] content) {
		this.content = content;

	}

	@Override
	public void addHeader(String header, String value) {
		this.headers.putIfAbsent(header, value);

	}

	private byte[] getHeadersBytes() {
		StringBuilder result = new StringBuilder();
		result.append(ResponseLines.getResponseLine(this.getStatusCode().getStatusCode()))
				.append(System.lineSeparator());
		for (Map.Entry<String, String> header : this.getHeaders().entrySet()) {
			result.append(header.getKey()).append(": ").append(header.getValue()).append(System.lineSeparator());
		}
		
		if(!this.cookies.isEmpty()) {
			result.append("Set-Cookie: ");
			for(HttpCookie cookie:this.cookies.values()) {
				result.append(cookie.toString()).append("; ");
				result.replace(result.length()-2, result.length(), "");
				result.append(System.lineSeparator());
			}
		}
		
		
		
		
		result.append(System.lineSeparator());

		return result.toString().getBytes();

	}

	@Override
	public void addCookie(String name, String value) {
		this.cookies.putIfAbsent(name, new HttpCookieImpl(name, value));
		
	}

}

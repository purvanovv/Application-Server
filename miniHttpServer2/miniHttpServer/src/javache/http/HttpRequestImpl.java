package javache.http;

import java.util.HashMap;

public class HttpRequestImpl implements HttpRequest {

	private String method;

	private String requestUrl;
	
	private HashMap<String, HttpCookie> cookies;

	private HashMap<String, String> headers;

	private HashMap<String, String> bodyParameters;

	public HttpRequestImpl(String requestContent) {
		String[] requestInfo = requestContent.split("\r\n");
		this.initMethod(requestInfo);
		this.initRequestUrl(requestInfo);
		this.initHeaders(requestInfo);
		this.initBodyParameters(requestInfo);
		this.initCookies();
	}

	private void initBodyParameters(String[] requestInfo) {
		this.bodyParameters = new HashMap<>();
		String[] requestUrlInfo = requestInfo[0].split(" ");
		if (this.getMethod().equals("GET")) {
			String[] pathInfo = requestUrlInfo[1].split("\\?");
			if(pathInfo.length > 1) {
				String[] parameters = pathInfo[1].split("&");
				for (int i = 0; i < parameters.length; i++) {
					String[] parameterPair = parameters[i].split("=");
					addBodyParameter(parameterPair[0], parameterPair[1]);
				}
			}
			
		}
		else if(this.getMethod().equals("POST")) {
			String[] pathInfo = requestInfo[requestInfo.length-1].split("\\?");
			String[] parameters = pathInfo[0].split("&");
			for (int i = 0; i < parameters.length; i++) {
				String[] parameterPair = parameters[i].split("=");
				addBodyParameter(parameterPair[0], parameterPair[1]);
			}
		}
	}

	private void initHeaders(String[] requestInfo) {
		this.headers = new HashMap<>();
		for (int i = 1; i < requestInfo.length; i++) {
			if (requestInfo[i].equals("")) {
				return;
			}
			String[] headerPair = requestInfo[i].split(": ");
			addHeader(headerPair[0], headerPair[1]);
		}

	}

	private void initRequestUrl(String[] requestInfo) {
		String[] requestUrlInfo = requestInfo[0].split(" ");
		String[] pathInfo = requestUrlInfo[1].split("\\?");
		setRequestUrl(pathInfo[0]);
	}

	private void initMethod(String[] requestInfo) {
		String[] requestUrlInfo = requestInfo[0].split(" ");
		setMethod(requestUrlInfo[0]);
	}
	
	private void initCookies() {
		this.cookies = new HashMap<>();
		if(!this.headers.containsKey("Cookie")) {
			return;
		}
		String cookiesHeader = this.headers.get("Cookie");
		String[] allCookies = cookiesHeader.split(";");
		for (int i = 0; i < allCookies.length; i++) {
			String[] cookieNameValuePair = allCookies[i].split("=");
			
			this.cookies.putIfAbsent(cookieNameValuePair[0], new HttpCookieImpl(cookieNameValuePair[0], cookieNameValuePair[1]));
			
		}
	}

	@Override
	public HashMap<String, String> getheaders() {
		return this.headers;
	}

	@Override
	public HashMap<String, String> getBodyParameters() {
		return this.bodyParameters;
	}
	
	@Override
	public HashMap<String, HttpCookie> getCookies() {
		return this.cookies;
	}

	@Override
	public String getMethod() {
		return this.method;
	}

	@Override
	public void setMethod(String method) {
		this.method = method;

	}

	@Override
	public String getRequestUrl() {
		return this.requestUrl;
	}

	@Override
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;

	}

	@Override
	public void addHeader(String header, String value) {
		this.headers.putIfAbsent(header, value);

	}

	@Override
	public void addBodyParameter(String parameter, String value) {
		this.bodyParameters.putIfAbsent(parameter, value);

	}

	@Override
	public boolean isResource() {
		return this.requestUrl.contains(".");
	}

}

package javache;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javache.http.HttpRequestImpl;
import javache.http.HttpResponseImpl;
import javache.http.HttpSession;
import javache.http.HttpSessionImpl;
import javache.http.HttpSessionStorage;
import javache.http.HttpStatus;

public class RequestHandler {
	private HttpRequestImpl request;

	private HttpResponseImpl response;

	private HttpSessionStorage sessionStorage;

	public RequestHandler(HttpSessionStorage sessionStorage) {
		this.sessionStorage = sessionStorage;
	}

	public byte[] handleRequest(String requestContent) {
		this.request = new HttpRequestImpl(requestContent);
		this.response = new HttpResponseImpl();
		byte[] result = null;

		if (this.request.getMethod().equals("GET")) {
			result = this.processGetRequest();
		}
		this.sessionStorage.refreshSession();

		return result;

	}

	private byte[] processGetRequest() {
		if (this.request.getRequestUrl().equals("/")) {
			// INDEX
			return this.processPageRequest("index");
		} else if (this.request.getRequestUrl().equals("/login")) {
			// LOGIN
			HttpSession session = new HttpSessionImpl();
			session.addAtributes("username", "Pesho");
			this.sessionStorage.addSession(session);
			this.response.addCookie("Javache", session.getId());
			return this.processPageRequest("login");
		} else if (this.request.getRequestUrl().equals("/logout")) {
			// LOGOUT
			if (!this.request.getCookies().containsKey("Javache")) {
				return this.redirect("you must login to access this route".getBytes(), "/");
			}
			String sessionId = this.request.getCookies().get("Javache").getValue();
			this.sessionStorage.getById(sessionId).invalidate();

			this.response.addCookie("Javache", "deleted; expires=Thu, 01 Jan 1970 00:00:00 GMT");
			return this.ok("Cookie expired successfuly".getBytes());
		}
		// FORBIDEN
		else if (this.request.getRequestUrl().equals("/forbiden")) {
			if (!this.request.getCookies().containsKey("Javache")) {
				return this.redirect("you must login to access this route".getBytes(), "/");
			}
			String sessionId = this.request.getCookies().get("Javache").getValue();
			HttpSession session = this.sessionStorage.getById(sessionId);
			String username = session.getAttributes().get("username").toString();
			return this.ok(("Hello " + username + "!").getBytes());
		}
		return processResourceRequest();
	}

	private String getMimeType(File file) {
		String fileName = file.getName();

		if (fileName.endsWith("css")) {
			return "text/css";
		} else if (fileName.endsWith("html")) {
			return "text/html";
		} else if (fileName.endsWith("jpg") || fileName.endsWith("jpeg")) {
			return "image/jpeg";
		} else if (fileName.endsWith("png")) {
			return "image/png";
		}

		return "text/plain";
	}

	private byte[] processPageRequest(String page) {
		String pagePath = WebConstants.PAGES_FOLDER_PATH + page + WebConstants.HTML_EXTENSION_AND_SEPARATOR;

		File file = new File(pagePath);

		if (!file.exists() || file.isDirectory()) {
			return this.notFound(("Page not found!").getBytes());
		}

		byte[] result = null;

		try {
			result = Files.readAllBytes(Paths.get(pagePath));
		} catch (IOException e) {
			return this.internalServerError(("Something went wrong!").getBytes());
		}

		this.response.addHeader("Content-Type", this.getMimeType(file));

		return this.ok(result);

	}

	private byte[] processResourceRequest() {
		String assetPath = WebConstants.ASSETS_FOLDER_PATH + this.request.getRequestUrl().substring(1);

		File file = new File(assetPath);

		if (!file.exists() || file.isDirectory()) {
			return this.notFound(("Asset not found!").getBytes());
		}

		byte[] result = null;

		try {
			result = Files.readAllBytes(Paths.get(assetPath));
		} catch (IOException e) {
			return this.internalServerError(("Something went wrong!").getBytes());
		}

		this.response.addHeader("Content-Type", this.getMimeType(file));
		this.response.addHeader("Content-Length", result.length + "");
		this.response.addHeader("Content-Disposition", "inline");

		return this.ok(result);
	}

	private byte[] internalServerError(byte[] content) {
		this.response.setStatusCode(HttpStatus.InternalServerError);
		this.response.setContent(content);
		return this.response.getBytes();
	}

	private byte[] ok(byte[] content) {
		this.response.setStatusCode(HttpStatus.OK);
		this.response.setContent(content);
		return this.response.getBytes();
	}

	private byte[] notFound(byte[] content) {
		this.response.setStatusCode(HttpStatus.NotFound);
		this.response.setContent(content);
		return this.response.getBytes();
	}

	private byte[] redirect(byte[] content, String location) {
		this.response.setStatusCode(HttpStatus.SeeOther);
		this.response.addHeader("Location", location);
		this.response.setContent(content);
		return this.response.getBytes();
	}

}

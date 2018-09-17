package javache;

import java.io.IOException;

public class StartUp {
	public static void main(String args[]) throws IOException {
		Server server = new Server(8080);
		server.run();
	}
}

package javache;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

import javache.http.HttpSessionStorage;

public class Server {
	
	private ServerSocket server;
	private Integer port;
	
	public Server(Integer port) {
		this.port = port;
	}
	
	public void run() throws IOException {
		this.server = new ServerSocket(this.port);
		System.out.println("Server listening on port:"+port);
		this.server.setSoTimeout(WebConstants.SOCKET_TIMEOUT_MILLISECONDS);
		HttpSessionStorage sessionStorage = new HttpSessionStorage();
		while(true) {
			try(Socket clientSocket = this.server.accept()){
				clientSocket.setSoTimeout(WebConstants.SOCKET_TIMEOUT_MILLISECONDS);
				System.out.println("Socket timeout");
				ConnectionHandler connectionHandler = new ConnectionHandler(clientSocket, new RequestHandler(sessionStorage));
				FutureTask<?> task = new FutureTask<>(connectionHandler,null);
				task.run();
			}catch(SocketTimeoutException e) {
				e.getMessage();
			}
		}
	}
	
	
	
	
}

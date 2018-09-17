package javache;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javache.io.Reader;
import javache.io.Writer;

public class ConnectionHandler extends Thread{
	
	private Socket clientSocket;
	
	private InputStream clientSocketInputStream;
	
	private OutputStream clientSocketOutputStream;
	
	private RequestHandler requestHandler;

	public ConnectionHandler(Socket clientSocket, RequestHandler requestHandler) {
		this.initializeConnection(clientSocket);
		this.requestHandler = requestHandler;
	}

	private void initializeConnection(Socket clientSocket) {
		this.clientSocket = clientSocket;
		try {
			this.clientSocketInputStream = this.clientSocket.getInputStream();
			this.clientSocketOutputStream = this.clientSocket.getOutputStream();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		}
		
	@Override
	public void run() {
		try{
			String requestContent = Reader.readAllLines(this.clientSocketInputStream);
			byte[] responseContent = this.requestHandler.handleRequest(requestContent);
			Writer.writeBytes(responseContent,this.clientSocketOutputStream);
			clientSocket.close();
			clientSocketInputStream.close();
			clientSocketOutputStream.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	
	
	}
	
	
	
	

}

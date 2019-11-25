package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class Server implements Runnable {

	private ServerSocket mainSocket;
	private Socket requestSocket;
	
	private int port;
	private boolean serverIsUp;
	
	Server(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		
		serverIsUp = true;
		
		try {
			
			// Used to optimize thread handling and enable threads that are finished
			// reused if needed
			Executor threadPool = Executors.newCachedThreadPool();
			mainSocket = new ServerSocket(port);
			
			while (serverIsUp) {
			
				requestSocket = mainSocket.accept();
				
				//threadPool.execute(new HttpRequest(requestSocket));
				new Thread(new HttpRequest(requestSocket)).start();
				
			}
			
		}
		// Log message etc
		catch (IOException e) {
			e.printStackTrace();
		}
			
		
	}
	
	public static void main(String[] args) {
		new Thread(new Server(8700)).start();
	}
	
}

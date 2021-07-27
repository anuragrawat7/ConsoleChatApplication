import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
	
	public static final String EXIT = "exit";
	
	public static final int PORT = 8888;
	
	static Map<String, Socket> nameSocketMap = new HashMap<>();

	public static void main(String[] args) {
		try (ServerSocket ss = new ServerSocket(PORT)) {
			System.out.println("Waiting for client connection");
			while (true) {
				try {
					Socket socket = ss.accept();
					System.out.println("Connected to Client");
					new Thread(new ChatServerRunnable(socket)).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

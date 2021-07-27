import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	private static Scanner scanner = new Scanner(System.in);
	public static void print() {
		System.out.println("Hello");
	}

	public static void main(String[] args) {
		try (Socket socket = new Socket("127.0.0.1", 8888);
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
			
			ClientMessageReceiver messageReceiver = new ClientMessageReceiver(dis);
			new Thread(messageReceiver).start();
			print();
			String input = null;
			do {
				input = scanner.nextLine();
				write(dos, input);
			} while (!Server.EXIT.equals(input));
			messageReceiver.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static void write(DataOutputStream dos, String message) throws IOException {
		dos.writeUTF(message);
		dos.flush();
	}

}

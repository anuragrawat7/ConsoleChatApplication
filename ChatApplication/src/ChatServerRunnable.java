import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChatServerRunnable implements Runnable {

	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	private String currentUsername;
	public ChatServerRunnable(Socket socket) throws IOException {
		this.socket = socket;
		this.dos = new DataOutputStream(socket.getOutputStream());
		this.dis = new DataInputStream(socket.getInputStream());
	}

	public void run() {
		try {
			write("Welcome to the Chat Server");
			login();
			System.out.println(currentUsername + " - user login successfully");
			write("Logged In Successfully .\nEnter [Date] to view the date. \nEnter [Time] to view the current time.\nEnter [exit] to exit the server");
			String input = dis.readUTF();
			while (!Server.EXIT.equals(input)) {
				System.out.println(currentUsername + " Entered " + input);
				if ("Date".equals(input)) {
					showDate();
				} else if("Time".equals(input)) {
					showTime();
				}
				else {
					write("The command you entered is illegal, please re-enter!");
				}
				input = dis.readUTF();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Server.nameSocketMap.remove(currentUsername);
			try {
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private void login() throws IOException {
		write("Please enter your name:");
		while (true) {
			String name = dis.readUTF();
			System.out.println("The user has entered a name:" + name);
			synchronized (ChatServerRunnable.class) {
				if (!Server.nameSocketMap.containsKey(name)) {
					currentUsername = name;
					Server.nameSocketMap.put(name, socket);
					break;
				} else {
					write("The name you entered already exists, please re-enter:");
				}
			}
		}
	}
	
	private void showDate() throws IOException {
		write(new Date().toString());
	}

	private void showTime() throws IOException {
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        write( sdf.format(cal.getTime()) );
	}
	private void write(String message) throws IOException {
		SocketUtils.writeToDataOutputStream(dos, message);
	}

}

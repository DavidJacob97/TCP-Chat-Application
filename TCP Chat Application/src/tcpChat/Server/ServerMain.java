package tcpChat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

// main class for the server
public class ServerMain {

	public static void main(String[] args) {
		ServerSocket ss = null;
		Server server = null;
		if (args.length < 1) {
			System.err.println("Usage: java Server portnumber");
			System.exit(-1);
		}

		try {
			int port = Integer.parseInt(args[0]);
			server = new Server(port);
			// listen on specified port
			ss = new ServerSocket(port);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			System.out.println("Server: waiting for connection ..");
			try {
				// accept connection from the client
				Socket socket = ss.accept();
				Scanner r = new Scanner(socket.getInputStream());
				// first message from the client is the client name
				String name = r.nextLine();
				// register the client
				server.addClient(name, socket);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

package tcpChat.Server;

import java.util.Scanner;

//responsible for receiving messages from the client
public class ListenerThread extends Thread {
	Server server = null;
	ClientConnection cc;
	Scanner r;

	// Construtor
	public ListenerThread(Server server, ClientConnection cc) {
		this.server = server;
		this.cc = cc;
		r = cc.getScanner();
	}

	// keep receving messages until the socket is closed
	public void run() {
		while (r.hasNextLine()) {
			String message = r.nextLine();
			System.out.println("message recevied" + message);
			// call method to process the message
			server.processMessage(message, cc);
		}
	}

}

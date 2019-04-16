package tcpChat.Server;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * @author brom
 */
// Mainly responsible for sending messages
public class ClientConnection {

	private final String name;
	Socket socket;
	PrintWriter p;
	Scanner r;

	// Constructor
	public ClientConnection(String name, Socket socket) {
		this.name = name;
		this.socket = socket;
		try {
			p = new PrintWriter(socket.getOutputStream());
			r = new Scanner(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// send message to client
	public void sendMessage(String message) {
		System.out.println("reply sent");
		p.println(message);
		p.flush();
	}

	public String getName() {
		return name;
	}

	public boolean hasName(String testName) {
		return testName.equals(name);
	}

	public Scanner getScanner() {
		return r;
	}
}

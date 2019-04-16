package tcpChat.Client;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import tcpChat.ChatMessage;

/**
 *
 * @author brom
 */
public class ServerConnection {
	String hostName;
	int port;
	String clientName = "";
	Socket socket;
	PrintWriter p;
	Scanner r;

	// Construtor
	public ServerConnection(String hostName, int port, String clientName) {
		this.hostName = hostName;
		this.port = port;
		this.clientName = clientName;
	}

	// Connect to server
	public boolean connect() {
		try {
			socket = new Socket(hostName, port);
			OutputStream o = socket.getOutputStream();
			p = new PrintWriter(o);
			InputStream in = socket.getInputStream();
			r = new Scanner(in);
			// Send client name to join
			p.println(clientName);
			p.flush();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	// receive message from server
	public String receiveChatMessage() {
		String message = null;
		if (r.hasNextLine()) {
			message = r.nextLine();
		}
		return message;
	}

	// send message to server
	public void sendChatMessage(String message) {
		// /broadcast hi there
		// /tell stephen, hi
		// /help
		String command;
		String parameter = "";

		// parse the the command string
		int index = message.indexOf(" ");
		if (index == -1) {
			command = message;
		} else {
			command = message.substring(0, index);
			parameter = message.substring(index + 1);
		}
		// creates chat message object
		ChatMessage c = new ChatMessage(command, parameter);
		// convert to JSON and send
		p.println(c.toJSONString());
		p.flush();
		System.out.println("message sent");
		// close socket connection and exit
		if (c.getCommand().equals("/leave")) {
			p.close();
			System.exit(0);
		}

	}

}

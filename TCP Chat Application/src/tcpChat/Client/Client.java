package tcpChat.Client;

import java.awt.event.*;

public class Client implements ActionListener {

	private String name = null;
	private final ChatGUI gui;
	private ServerConnection connection = null;

	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("Usage: java Client serverhostname serverportnumber username");
			System.exit(-1);
		}

		try {
			Client instance = new Client(args[2]);
			instance.connectToServer(args[0], Integer.parseInt(args[1]));
		} catch (NumberFormatException e) {
			System.err.println("Error: port number must be an integer.");
			System.exit(-1);
		}
	}

	private Client(String userName) {
		name = userName;
		// Start up GUI (runs in its own thread)
		gui = new ChatGUI(this, name);
	}

	private void connectToServer(String hostName, int port) {
		// Create a new server connection
		connection = new ServerConnection(hostName, port, name);
		if (connection.connect()) {
			listenForServerMessages();
		} else {
			System.err.println("Unable to connect to server");
		}
	}

	private void listenForServerMessages() {
		do {
			String message = connection.receiveChatMessage();
			gui.displayMessage(message);

		} while (true);
	}

	// Sole ActionListener method; acts as a callback from GUI when user hits
	// enter in input field
	@Override
	public void actionPerformed(ActionEvent e) {
		// Since the only possible event is a carriage return in the text input
		// field, the text in the chat input field can now be sent to the
		// server.

		// send chatMessageAtLeastOnce() instead of sendChatMessage()
		connection.sendChatMessage(gui.getInput());
		gui.clearInput();
	}
}

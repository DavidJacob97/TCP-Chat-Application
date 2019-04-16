package tcpChat.Server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tcpChat.ChatMessage;

public class Server {
	// an array list of Clients
	private ArrayList<ClientConnection> connectedClients = new ArrayList<ClientConnection>();
	// quotes
	List<String> quotes = new ArrayList<String>();
	// help
	Map<String, String> helpMap = new HashMap<String, String>();

	public Server(int portNumber) {
		quotes.add("Hunger is the best sauce");
		quotes.add("Beauty is in the eye of the beholder");
		quotes.add("Theres no such this as bad weather, only bad clothes");
		quotes.add("Everyone talks about the weather but nobody does anything about it");

		helpMap.put("leave", "/leave - Leave the chat and exit the program");
		helpMap.put("tell", "/tell - Send message to another client");
		helpMap.put("broadcast", "/broadcast - Send message to all clients");
		helpMap.put("help", "/help - Lists all commands");
		helpMap.put("list", "/list - Lists all clients");
		helpMap.put("qotd", "/qotd - Displays quote of the day");
	}

	// all methods are synchronized as this object is used by a number of
	// threads

	// process the JSON message
	public synchronized void processMessage(String jsonMessage, ClientConnection cc) {

		String senderName = cc.getName();
		// Construct ChatMessage object from JSON message
		ChatMessage c = new ChatMessage(jsonMessage);

		if (c.getCommand().equals("/help")) {
			// help command
			System.out.println("help");
			String param = c.getParameters();
			// check if there is a parameter
			if (param.equals("")) {
				// no parameter
				cc.sendMessage("help leave broadcast tell list qotd");
			} else {
				String m = helpMap.get(param);
				// check if parameter has allowable value
				if (m != null) {
					cc.sendMessage(m);
				} else {
					cc.sendMessage("Command does not exist");
					cc.sendMessage("help leave broadcast tell list qotd");
				}
			}
		} else if (c.getCommand().equals("/list")) {
			// list command
			System.out.println("/list");
			String cList = getClientList();
			cc.sendMessage(cList);

		} else if (jsonMessage.startsWith("/leave")) {
			// leave command
			System.out.println("/leave");
			leave(senderName);

		} else if (c.getCommand().equals("/broadcast")) {
			// broadcast command
			System.out.println("broadcast");
			String name = cc.getName();
			String m = c.getParameters();
			broadcast(name + ": " + m);

		} else if (c.getCommand().equals("/leave")) {
			// leave command
			System.out.println("/leave");
			leave(senderName);

		} else if (c.getCommand().equals("/tell")) {
			// tell command
			System.out.println("/tell");
			String params = c.getParameters();
			String[] tokens = params.split(",");
			String receiverName = tokens[0];
			String message = senderName + ": " + tokens[1];
			sendPrivateMessage(message, receiverName);

		} else if (c.getCommand().equals("/qotd")) {
			// quote of the day command
			int rand = (int) (Math.random() * quotes.size());
			System.out.println(rand);
			// pick a random quote
			String quote = quotes.get(rand);
			cc.sendMessage(quote);

		} else {
			// wrong message format
			System.out.println("error in message format");
			cc.sendMessage("Error in message format");
			cc.sendMessage("/help /leave /broadcast /tell /list /qotd");
		}
	}

	// adds a client, returns false if name is already used
	public synchronized boolean addClient(String name, Socket socket) {
		ClientConnection c;
		for (Iterator<ClientConnection> itr = connectedClients.iterator(); itr.hasNext();) {
			c = itr.next();
			if (c.hasName(name)) {
				return false; // Already exists a client with this name
			}
		}
		ClientConnection cc = new ClientConnection(name, socket);
		connectedClients.add(cc);
		ListenerThread t = new ListenerThread(this, cc);
		t.start();
		broadcast(name + " has joined the chatroom");
		System.out.println("Client added");
		return true;
	}

	// sends message to defined client
	public synchronized void sendPrivateMessage(String message, String name) {
		ClientConnection c;
		for (Iterator<ClientConnection> itr = connectedClients.iterator(); itr.hasNext();) {
			c = itr.next();
			if (c.hasName(name)) {
				c.sendMessage(message);
			}
		}
	}

	// message to everyone connected
	public synchronized void broadcast(String message) {
		for (Iterator<ClientConnection> itr = connectedClients.iterator(); itr.hasNext();) {
			itr.next().sendMessage(message);
		}
	}

	// gets a list of clients connected
	public synchronized String getClientList() {
		String clientList = "";
		for (ClientConnection c : connectedClients) {
			clientList += " | " + c.getName();
		}
		return clientList;
	}

	// removes said client
	public synchronized void leave(String name) {
		ClientConnection removeConnection = null;
		for (ClientConnection c : connectedClients) {
			if (c.getName().equals(name)) {
				removeConnection = c;
			}
		}
		connectedClients.remove(removeConnection);
		broadcast(name + " has left");
	}

}

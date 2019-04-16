package tcpChat;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ChatMessage {

	private JSONObject obj = new JSONObject();
	JSONParser parser = new JSONParser();

	// Construtor
	public ChatMessage(String command, String parameters) {
		obj.put("command", command);
		obj.put("parameters", parameters);
		obj.put("timestamp", System.currentTimeMillis());
	}

	// Construct chat message from a JSON string
	public ChatMessage(String jsonString) {
		try {
			obj = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getCommand() {
		return (String) obj.get("command");
	}

	public String getParameters() {
		return (String) obj.get("parameters");
	}

	public String getTimeStamp() {
		return obj.get("timestamp").toString();
	}

	// convert to JSON string
	public String toJSONString() {
		return obj.toJSONString();
	}
}

package net.nemez.chatapi.click;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.UUID;

public class CallbackMap {

	private static HashMap<String, HashMap<Integer, ClickCallback>> map = new HashMap<String, HashMap<Integer, ClickCallback>>();

	protected static int register(UUID uuid, ClickCallback callback) {
		HashMap<Integer, ClickCallback> playerMap = map.get(uuid.toString());
		if (playerMap == null) {
			playerMap = new HashMap<Integer, ClickCallback>();
			map.put(uuid.toString(), playerMap);
		}
		int largestId = 0;
		for (int i : playerMap.keySet()) {
			if (i > largestId) {
				largestId = i;
			}
		}
		int id = largestId + 1;
		playerMap.put(id, callback);
		return id;
	}

	protected static void execute(CommandSender sender, UUID uuid, int id) {
		HashMap<Integer, ClickCallback> playerMap = map.get(uuid.toString());
		if (playerMap == null) {
			return;
		}
		ClickCallback cb = playerMap.get(id);
		if (cb == null) {
			return;
		}
		if (cb.isAsynchronous()) {
			Thread t = new Thread() {
				public void run() {
					cb.execute(sender);
				}
			};
			t.start();
		} else {
			cb.execute(sender);
		}
	}

	protected static void discard(UUID uuid) {
		map.remove(uuid.toString());
	}
}

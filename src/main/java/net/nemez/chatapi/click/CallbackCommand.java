package net.nemez.chatapi.click;

import net.nemez.chatapi.ChatAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CallbackCommand extends Command {

	public CallbackCommand(String internalCommandName) {
		super(internalCommandName);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			ChatAPI.send(sender, ChatAPI.MESSAGE_PLAYER_CLICK_CALLBACK);
			return true;
		}
		if ((args.length == 1 && args[0].equals("help")) || args.length != 1) {
			ChatAPI.send(sender, ChatAPI.MESSAGE_HELP_CLICK_CALLBACK);
			return true;
		}
		int id;
		try {
			id = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			ChatAPI.send(sender, ChatAPI.MESSAGE_HELP_CLICK_CALLBACK);
			return true;
		}
		UUID uuid = ((Player) sender).getUniqueId();
		CallbackMap.execute(sender, uuid, id);
		return true;
	}
}

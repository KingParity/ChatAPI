package net.nemez.chatapi;

import java.lang.reflect.Field;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.nemez.chatapi.click.CallbackCommand;
import net.nemez.chatapi.click.Message;
import net.nemez.chatapi.click.PlayerQuitListener;

public class ChatAPI
{
	
	/* message coloring permission */
	public static final String PERMISSION_CHAT_COLOR = "chat.color";
	/* message formatting permission */
	public static final String PERMISSION_CHAT_FORMAT = "chat.format";
	/* message magic formatting permission */
	public static final String PERMISSION_CHAT_MAGIC = "chat.magic";
	/* permission to send messages in chat */
	public static final String PERMISSION_CHAT_USE = "chat.use";
	/* message to send when the internal command is not ran correctly (ran by user) */
	public static final String MESSAGE_HELP_CLICK_CALLBACK = "&cThis is an internal command for ChatAPI and should not be ran by players manually.";
	/* message to send when the internal command is not ran by a player */
	public static final String MESSAGE_PLAYER_CLICK_CALLBACK = "&cThis command can only be run by a player";
	/* the actual command name for use in click callbacks */
	private static String internalCommandName;
	
	/** Initializes ChatAPI and registers the required commands for clickable chat to function. */
	public static void initialize(JavaPlugin plugin)
	{
		if (internalCommandName != null)
		{
			return;
		}
		Random rand = new Random(System.currentTimeMillis());
		internalCommandName = "chatapi-exec-" + Integer.toHexString(rand.nextInt(0xEFFF) + 0x1000);
		try
		{
			final Field cmdMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			cmdMap.setAccessible(true);
			CommandMap map = (CommandMap) cmdMap.get(Bukkit.getServer());
			map.register("net/nemez/chatapi", new CallbackCommand(internalCommandName));
		}
		catch (Exception e)
		{
			plugin.getLogger().severe("Failed to register internal command '" + internalCommandName + "'");
			e.printStackTrace();
		}
		// For some reason the "chatapi:..." cmd is no longer registered. Disabling this until further investigation and fixing.
		// internalCommandName = "chatapi:" + internalCommandName;
		plugin.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), plugin);
		plugin.getLogger().info("ChatAPI initialized");
	}
	
	/** Colorifies a message using &format codes. Respects permissions.
	 * 
	 * @param sender the command sender whose permissions to use. null if permissions are to be ignored.
	 * @param message the message to color
	 * @return colored message */
	public static String colorify(CommandSender sender, String message)
	{
		if (sender == null || sender.hasPermission(PERMISSION_CHAT_COLOR))
		{
			message = message.replaceAll("&([0-9a-fA-FrR])", "§$1");
		}
		if (sender == null || sender.hasPermission(PERMISSION_CHAT_FORMAT))
		{
			message = message.replaceAll("&([l-oL-OrR])", "§$1");
		}
		if (sender == null || sender.hasPermission(PERMISSION_CHAT_MAGIC))
		{
			message = message.replaceAll("&([kKrR])", "§$1");
		}
		message = message.replace("&§", "&");
		return message;
	}
	
	/** Sends a colorified message to the command sender.
	 * 
	 * @param sender the command sender to whom to send the message.
	 * @param message the message to send. */
	public static void send(CommandSender sender, String message)
	{
		if (sender == null)
		{
			return;
		}
		sender.sendMessage(colorify(null, message));
	}
	
	/** Checks if a command sender has the permission node required to send chat messages.
	 * 
	 * @param sender the command sender to check.
	 * @return true/false if sender can chat or is null. */
	public static boolean canChat(CommandSender sender)
	{
		if (sender == null)
		{
			return true;
		}
		else
		{
			return sender.hasPermission(PERMISSION_CHAT_USE);
		}
	}
	
	/** Creates a new message object that will be sent to the given command sender with regards to the second command sender's permissions.
	 * 
	 * @param sender the command sender to whom to send the message.
	 * @param permissionSender the command sender whose permissions to use.
	 * @return message object */
	public static Message createMessage(CommandSender sender, CommandSender permissionSender)
	{
		return new Message(sender, permissionSender);
	}
	
	/** Creates a new message object that will be sent to the given command sender.
	 * 
	 * @param sender the command sender to whom to send the message.
	 * @return message object. */
	public static Message createMessage(CommandSender sender)
	{
		return createMessage(sender, null);
	}
	
	/** Gets the name of the internal ChatAPI command used for click callbacks.
	 * This function is used internally and you don't need to worry about it.
	 * 
	 * @return callback command name */
	public static String getInternalCallbackCommand()
	{
		return internalCommandName;
	}
}

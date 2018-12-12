package net.nemez.chatapi.click;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.nemez.chatapi.ChatAPI;

public class Message
{
	
	private CommandSender sender;
	private CommandSender permission;
	private TextComponent message;
	private String rawMessage;
	
	public Message(CommandSender sender, CommandSender permission)
	{
		this.sender = sender;
		this.permission = permission;
		message = new TextComponent("");
		rawMessage = "";
	}
	
	public Message appendText(String text)
	{
		text = ChatAPI.colorify(permission, text);
		BaseComponent[] components = TextComponent.fromLegacyText(text);
		for (BaseComponent component : components)
		{
			message.addExtra(component);
		}
		rawMessage += text;
		return this;
	}
	
	public Message appendLink(String text, String url)
	{
		text = ChatAPI.colorify(permission, text);
		BaseComponent[] components = TextComponent.fromLegacyText(text);
		for (BaseComponent component : components)
		{
			component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
			message.addExtra(component);
		}
		rawMessage += text;
		return this;
	}
	
	public Message appendSendChat(String text, String msg)
	{
		text = ChatAPI.colorify(permission, text);
		BaseComponent[] components = TextComponent.fromLegacyText(text);
		for (BaseComponent component : components)
		{
			component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, msg));
			message.addExtra(component);
		}
		rawMessage += text;
		return this;
	}
	
	public Message appendSuggest(String text, String suggestion)
	{
		text = ChatAPI.colorify(permission, text);
		BaseComponent[] components = TextComponent.fromLegacyText(text);
		for (BaseComponent component : components)
		{
			component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestion));
			message.addExtra(component);
		}
		rawMessage += text;
		return this;
	}
	
	public Message appendCallback(String text, ClickCallback callback)
	{
		if (sender instanceof Player)
		{
			int id = CallbackMap.register(((Player) sender).getUniqueId(), callback);
			return appendSendChat(text, "/" + ChatAPI.getInternalCallbackCommand() + " " + id);
		}
		else
		{
			return appendText(text);
		}
	}
	
	public Message appendTextHover(String text, String hover)
	{
		text = ChatAPI.colorify(permission, text);
		BaseComponent[] components = TextComponent.fromLegacyText(text);
		for (BaseComponent component : components)
		{
			addHoverText(component, hover);
			message.addExtra(component);
		}
		rawMessage += text;
		return this;
	}
	
	public Message appendLinkHover(String text, String url, String hover)
	{
		text = ChatAPI.colorify(permission, text);
		BaseComponent[] components = TextComponent.fromLegacyText(text);
		for (BaseComponent component : components)
		{
			component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
			addHoverText(component, hover);
			message.addExtra(component);
		}
		rawMessage += text;
		return this;
	}
	
	public Message appendSendChatHover(String text, String msg, String hover)
	{
		text = ChatAPI.colorify(permission, text);
		BaseComponent[] components = TextComponent.fromLegacyText(text);
		for (BaseComponent component : components)
		{
			component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, msg));
			addHoverText(component, hover);
			message.addExtra(component);
		}
		rawMessage += text;
		return this;
	}
	
	public Message appendSuggestHover(String text, String suggestion, String hover)
	{
		text = ChatAPI.colorify(permission, text);
		BaseComponent[] components = TextComponent.fromLegacyText(text);
		for (BaseComponent component : components)
		{
			component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestion));
			addHoverText(component, hover);
			message.addExtra(component);
		}
		rawMessage += text;
		return this;
	}
	
	public Message appendCallbackHover(String text, ClickCallback callback, String hover)
	{
		if (sender instanceof Player)
		{
			int id = CallbackMap.register(((Player) sender).getUniqueId(), callback);
			return appendSendChatHover(text, "/" + ChatAPI.getInternalCallbackCommand() + " " + id, hover);
		}
		else
		{
			return appendTextHover(text, hover);
		}
	}
	
	public Message appendMessage(Message msg) {
		message.addExtra(msg.message);
		rawMessage += msg.rawMessage;
		return this;
	}
	
	public void send()
	{
		if (sender == null || !ChatAPI.canChat(this.permission))
		{
			return;
		}
		if (sender instanceof Player)
		{
			((Player) sender).spigot().sendMessage(message);
		}
		else
		{
			sender.sendMessage(rawMessage);
		}
	}
	
	public void sendAsActionBar()
	{
		if (sender == null)
		{
			return;
		}
		if (sender instanceof Player)
		{
			((Player) sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
		}
		else
		{
			sender.sendMessage(rawMessage);
		}
	}
	
	private void addHoverText(BaseComponent comp, String text)
	{
		comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(ChatAPI.colorify(permission, text)).create()));
	}
}

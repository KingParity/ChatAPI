package net.nemez.chatapi.click;

import net.nemez.chatapi.ChatAPI;
import org.bukkit.command.CommandSender;

public abstract class ClickCallback {

	private boolean repeatable, async;
	private String  expiredMessage;
	private boolean expired;

	public ClickCallback(boolean repeatable, boolean async, String expiredMessage) {
		this.repeatable = repeatable;
		this.async = async;
		this.expiredMessage = expiredMessage;
		this.expired = false;
	}

	public final void execute(CommandSender sender) {
		if (!expired) {
			run(sender);
		} else {
			if (sender != null) {
				sender.sendMessage(ChatAPI.colorify(null, expiredMessage));
			}
		}
		if (!repeatable) {
			expired = true;
		}
	}

	public abstract void run(CommandSender sender);

	public boolean isRepeatable() {
		return repeatable;
	}

	public boolean isAsynchronous() {
		return async;
	}

	public String getExpiredMessage() {
		return expiredMessage;
	}
}

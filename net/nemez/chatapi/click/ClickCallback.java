package net.nemez.chatapi.click;

import org.bukkit.command.CommandSender;

public abstract class ClickCallback {

	private boolean repeatable, async;
	private String expiredMessage;
	private boolean expired;
	
	public ClickCallback(boolean repeatable, boolean async, String expiredMessage) {
		this.repeatable = repeatable;
		this.async = async;
		this.expiredMessage = expiredMessage;
		this.expired = false;
	}
	
	public abstract void run(CommandSender sender);
	
	public final void execute(CommandSender sender) {
		if (!expired) {
			run(sender);
		}
		if (!repeatable) {
			expired = true;
		}
	}
	
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

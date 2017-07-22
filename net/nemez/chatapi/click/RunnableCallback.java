package net.nemez.chatapi.click;

import org.bukkit.command.CommandSender;

public class RunnableCallback extends ClickCallback {

	private Runnable runnable;
	
	public RunnableCallback(Runnable runnable, boolean repeatable, boolean async, String expiredMessage) {
		super(repeatable, async, expiredMessage);
	}

	@Override
	public void run(CommandSender sender) {
		runnable.run();
	}
}

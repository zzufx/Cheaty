package tc.oc.occ.bolt;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import tc.oc.pgm.community.events.PlayerReportEvent;

public class DiscordBot {

	private DiscordApi api;
	private BotConfig config;
	private Logger logger;

	public DiscordBot(BotConfig config, Logger logger) {
		this.config = config;
		this.logger = logger;
		reload();
	}
	
	public BotConfig getConfig() {
		return config;
	}

	public void enable() {
		if(config.isEnabled()) {
			logger.info("Enabling DiscordBot...");
			new DiscordApiBuilder()
			.setToken(config.getToken())
			.setWaitForServersOnStartup(false)
			.setWaitForUsersOnStartup(false)
			.login().thenAcceptAsync(api -> {
				setAPI(api);
				api.setMessageCacheSize(1, 60 * 60);
				api.addServerBecomesAvailableListener(listener -> {
					logger.info(listener.getServer().getName() + " is now available");
				});
				logger.info("Discord Bot is now active!");
			});
		} 
	}

	private void setAPI(DiscordApi api) {
		this.api = api;
	}

	public void disable() {
		if(this.api != null) {
			this.api.disconnect();
		}
		this.api = null;
	}

	private void sendMessage(String message, boolean report) {
		if(api != null) {
			api.getServerById(config.getDiscordServerId()).ifPresent(server -> {
				server.getChannelById(report ? config.getReportChannel() : config.getAntiCheatChannel()).ifPresent(channel -> {
					channel.asTextChannel().ifPresent(text -> {
						text.sendMessage(format(message));
					});
				});
			});
		}
	}

	public void sendReport(PlayerReportEvent event) {
		if(!config.isReportsEnabled()) return;
		String reporter = event.getSender().getNameLegacy();
		String reported = event.getPlayer().getNameLegacy();
		String reason   = event.getReason();
		String formatted = config.getReportFormat()
				.replace("%reporter%", reporter)
				.replace("%reported%", reported)
				.replace("%reason%", reason);
		sendMessage(config.getReportPrefix() + formatted, true);		
	}

	public void sendRelay(String message, RelayType type) {
		if(!config.isRelayCommandEnabled()) return;
		String formatted = config.getRelayFormat().replace("%message%", message);
		sendMessage(getPrefix(type) + formatted, false);
	}

	public void reload() {
		if(this.api != null && !config.isEnabled()) {
			disable();
		} else if(this.api == null && config.isEnabled()) {
			enable();
		}
	}
	
	private String format(String text) {
		text = ChatColor.translateAlternateColorCodes('&', text);
		text = ChatColor.stripColor(text);
		text = text.replace("@", "");
		return text.trim();
	}
	
	public String getPrefix(RelayType type) {
		switch(type) {
		case AUTOKILL:
			return config.getAutoKillPrefix();
		case MATRIX:
			return config.getMatrixPrefix();
		default:
			return config.getCommandPrefix();
		}
	}
	
	public static enum RelayType {
		AUTOKILL,
		MATRIX,
		COMMAND;
	}

}

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

	private void sendMessage(String message) {
		if(api != null) {
			api.getServerById(config.getServerId()).ifPresent(server -> {
				server.getChannelById(config.getChannel()).ifPresent(channel -> {
					channel.asTextChannel().ifPresent(text -> {
						text.sendMessage(format(message));
					});
				});
			});
		}
	}

	public void sendReport(PlayerReportEvent event) {
		if(!config.isReports()) return;
		String reporter = event.getSender().getNameLegacy();
		String reported = event.getPlayer().getNameLegacy();
		String reason   = event.getReason();
		String formatted = config.getReportFormat()
				.replace("%reporter%", reporter)
				.replace("%reported%", reported)
				.replace("%reason%", reason);
		sendMessage(formatted);
	}

	public void sendRelay(String message) {
		if(!config.isRelayCommand()) return;
		String formatted = config.getRelayFormat().replace("%message%", message);
		sendMessage(formatted);
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
		return text;
	}

}

package tc.oc.occ.cheaty;

import dev.pgm.community.assistance.Report;
import dev.pgm.community.events.PlayerReportEvent;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

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
    if (config.isEnabled()) {
      logger.info("Enabling DiscordBot...");
      new DiscordApiBuilder()
          .setToken(config.getToken())
          .setWaitForServersOnStartup(false)
          .setWaitForUsersOnStartup(false)
          .login()
          .thenAcceptAsync(
              api -> {
                setAPI(api);
                api.setMessageCacheSize(1, 60 * 60);
                api.addServerBecomesAvailableListener(
                    listener -> {
                      logger.info(listener.getServer().getName() + " is now available");
                    });
                logger.info("Cheaty has connected to Discord!");
              });
    }
  }

  private void setAPI(DiscordApi api) {
    this.api = api;
  }

  public void disable() {
    if (this.api != null) {
      this.api.disconnect();
    }
    this.api = null;
  }

  private void sendMessage(String message, boolean report) {
    if (api != null) {
      api.getServerById(config.getDiscordServerId())
          .ifPresent(
              server -> {
                server
                    .getChannelById(
                        report ? config.getReportChannel() : config.getAntiCheatChannel())
                    .ifPresent(
                        channel -> {
                          channel
                              .asTextChannel()
                              .ifPresent(
                                  text -> {
                                    text.sendMessage(format(message));
                                  });
                        });
              });
    }
  }

  public void sendReport(PlayerReportEvent event) {
    if (!config.isReportsEnabled()) return;
    Report report = event.getReport();
    String reporter = getUsername(report.getSenderId());
    String reported = getUsername(report.getTargetId());
    String reason = report.getReason();
    String formatted =
        config
            .getReportFormat()
            .replace("%reporter%", reporter)
            .replace("%reported%", reported)
            .replace("%reason%", reason);
    sendMessage(config.getReportPrefix() + formatted, true);
  }

  private String getUsername(UUID playerId) {
    Player player = Bukkit.getPlayer(playerId);
    return player != null ? player.getName() : null;
  }

  public void sendRelay(String message, RelayType type) {
    if (!config.isRelayCommandEnabled()) return;
    String formatted = config.getRelayFormat().replace("%message%", message);
    sendMessage(getPrefix(type) + formatted, false);
  }

  public void reload() {
    if (this.api != null && !config.isEnabled()) {
      disable();
    } else if (this.api == null && config.isEnabled()) {
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
    switch (type) {
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

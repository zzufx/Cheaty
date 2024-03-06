package tc.oc.occ.cheaty;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.Configuration;

public class BotConfig {

  private boolean enabled;

  private String discordToken;
  private String discordServerId;

  private String serverName;

  private boolean reportsEnabled;
  private boolean relayCommandEnabled;

  private String reportFormat;
  private String relayFormat;

  private String reportPrefix;
  private String autoKillPrefix;
  private String matrixPrefix;
  private String commandPrefix;

  private String reportChannel;
  private String cheatChannel;

  private boolean cheatNotifyEnabled;
  private String cheatNotifyPermission;
  private Component cheatNotifyPrefix;

  private boolean akNotifyEnabled;
  private String akNotifyPermission;
  private Component akNotifyPrefix;

  private boolean pingsEnabled;
  private int reportThreshold;
  private int reportWindow;
  private int staffIdleTime;
  private List<String> pingDiscordRoles;

  private String staffPermissionNode;
  private String offDutyPermissionNode;

  public BotConfig(Configuration config) {
    reload(config);
  }

  public void reload(Configuration config) {
    this.enabled = config.getBoolean("enabled");
    this.discordToken = config.getString("token");
    this.discordServerId = config.getString("server");

    String serverEnv = System.getenv("SERVER_NAME");
    this.serverName =
        (serverEnv != null && !serverEnv.isEmpty()) ? serverEnv : config.getString("server-name");

    this.reportsEnabled = config.getBoolean("types.reports");
    this.relayCommandEnabled = config.getBoolean("types.relay-command");

    this.reportFormat = config.getString("report-format");
    this.relayFormat = config.getString("relay-format");

    this.reportPrefix = config.getString("prefix.report");
    this.autoKillPrefix = config.getString("prefix.autokill");
    this.matrixPrefix = config.getString("prefix.matrix");
    this.commandPrefix = config.getString("prefix.command");

    this.reportChannel = config.getString("channels.reports");
    this.cheatChannel = config.getString("channels.anticheat");

    this.cheatNotifyEnabled = config.getBoolean("cheat-notify.enabled");
    this.cheatNotifyPermission = config.getString("cheat-notify.permission-node");
    this.cheatNotifyPrefix =
        LegacyComponentSerializer.legacyAmpersand()
            .deserialize(config.getString("cheat-notify.alert-prefix"));

    this.akNotifyEnabled = config.getBoolean("ak-notify.enabled");
    this.akNotifyPermission = config.getString("ak-notify.permission-node");
    this.akNotifyPrefix =
        LegacyComponentSerializer.legacyAmpersand()
            .deserialize(config.getString("ak-notify.alert-prefix"));

    this.pingsEnabled = config.getBoolean("pings.enabled");
    this.reportThreshold = config.getInt("pings.report-threshold");
    this.reportWindow = config.getInt("pings.report-window");
    this.staffIdleTime = config.getInt("pings.staff-idle-time");
    this.pingDiscordRoles = config.getStringList("pings.discord-roles");
    this.staffPermissionNode = config.getString("staff-permission");
    this.offDutyPermissionNode = config.getString("off-duty-permission");
  }

  public boolean isEnabled() {
    return enabled;
  }

  public String getToken() {
    return discordToken;
  }

  public String getDiscordServerId() {
    return discordServerId;
  }

  public String getServerName() {
    return serverName;
  }

  public boolean isReportsEnabled() {
    return reportsEnabled;
  }

  public boolean isRelayCommandEnabled() {
    return relayCommandEnabled;
  }

  public String getReportFormat() {
    return replaceServerName(reportFormat);
  }

  public String getRelayFormat() {
    return replaceServerName(relayFormat);
  }

  private String replaceServerName(String format) {
    return format.replace("%server%", getServerName());
  }

  public String getReportPrefix() {
    return reportPrefix;
  }

  public String getAutoKillPrefix() {
    return autoKillPrefix;
  }

  public String getMatrixPrefix() {
    return matrixPrefix;
  }

  public String getCommandPrefix() {
    return commandPrefix;
  }

  public String getReportChannel() {
    return reportChannel;
  }

  public String getAntiCheatChannel() {
    return cheatChannel;
  }

  public boolean isCheatNotifyEnabled() {
    return cheatNotifyEnabled;
  }

  public String getCheatNotifyPermission() {
    return cheatNotifyPermission;
  }

  public Component getCheatNotifyPrefix() {
    return cheatNotifyPrefix;
  }

  public boolean isAKNotifyEnabled() {
    return akNotifyEnabled;
  }

  public String getAKNotifyPermission() {
    return akNotifyPermission;
  }

  public Component getAKNotifyPrefix() {
    return akNotifyPrefix;
  }

  public boolean isPingEnabled() {
    return pingsEnabled;
  }

  public int getReportThreshold() {
    return reportThreshold;
  }

  public int getReportWindowMinutes() {
    return reportWindow;
  }

  public int getStaffIdleMinutes() {
    return staffIdleTime;
  }

  public List<String> getDiscordPingRoles() {
    return pingDiscordRoles;
  }

  public String getStaffPermission() {
    return staffPermissionNode;
  }

  public String getOffDutyPermission() {
    return offDutyPermissionNode;
  }
}

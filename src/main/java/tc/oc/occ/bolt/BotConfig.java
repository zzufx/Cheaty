package tc.oc.occ.bolt;

import org.bukkit.configuration.Configuration;

public class BotConfig {
	
	private boolean enabled;
	
	private String token;
	private String serverId;
	
	private String serverName;
	
	private boolean reports;
	private boolean relayCommand;
	
	private String reportFormat;
	private String relayFormat;
	
	private String reportPrefix;
	private String autoKillPrefix;
	private String matrixPrefix;
	private String commandPrefix;
	
	private String reportChannel;
	private String cheatChannel;
	
	public BotConfig(Configuration config) {
		reload(config);
	}
	
	public void reload(Configuration config) {
		this.enabled = config.getBoolean("enabled");
		this.token = config.getString("token");
		this.serverId = config.getString("server");
		
		this.serverName = config.getString("server-name");
		
		this.reports = config.getBoolean("types.reports");
		this.relayCommand = config.getBoolean("types.relay-command");
		
		this.reportFormat = config.getString("report-format");
		this.relayFormat = config.getString("relay-format");
		
		this.reportPrefix = config.getString("prefix.report");
		this.autoKillPrefix = config.getString("prefix.autokill");
		this.matrixPrefix = config.getString("prefix.matrix");
		this.commandPrefix = config.getString("prefix.command");
		
		this.reportChannel = config.getString("channels.reports");
		this.cheatChannel  = config.getString("channels.anticheat");
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getToken() {
		return token;
	}

	public String getServerId() {
		return serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public boolean isReports() {
		return reports;
	}

	public boolean isRelayCommand() {
		return relayCommand;
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
}

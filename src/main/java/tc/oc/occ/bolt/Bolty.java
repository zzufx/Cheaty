package tc.oc.occ.bolt;

import co.aikar.commands.BukkitCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import tc.oc.occ.bolt.commands.AdminCommands;
import tc.oc.occ.bolt.commands.BotCommands;

public class Bolty extends JavaPlugin {

  private DiscordBot bot;
  private BotConfig config;
  private BukkitCommandManager commands;

  @Override
  public void onEnable() {
    this.saveDefaultConfig();
    this.reloadConfig();

    this.config = new BotConfig(getConfig());
    this.bot = new DiscordBot(config, getLogger());

    this.setupCommands();
    this.registerListeners();
  }

  public void setupCommands() {
    this.commands = new BukkitCommandManager(this);
    commands.registerDependency(DiscordBot.class, bot);
    commands.registerDependency(BotConfig.class, config);
    commands.registerCommand(new BotCommands());
    commands.registerCommand(new AdminCommands());
  }

  private void registerListeners() {
    this.getServer().getPluginManager().registerEvents(new BotListener(bot), this);
  }

  public void reloadBotConfig() {
    this.reloadConfig();
    config.reload(getConfig());
    bot.reload();
  }
}

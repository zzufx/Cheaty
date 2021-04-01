package tc.oc.occ.bolt.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Description;
import tc.oc.occ.bolt.Bolty;

public class AdminCommands extends BaseCommand {
	
	@Dependency private Bolty plugin;
	
	@CommandAlias("bolty")
	@Description("Reload bolty config")
	@CommandPermission("bolt.admin")
	public void reload(CommandSender sender) {
		plugin.reloadBotConfig();
		sender.sendMessage(color("&a&lReloaded config!"));
	}
	
	public static String color(String format, Object... args) {
		return ChatColor.translateAlternateColorCodes('&', String.format(format, args));
	}
}

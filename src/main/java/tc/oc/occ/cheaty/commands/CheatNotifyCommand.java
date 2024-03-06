package tc.oc.occ.cheaty.commands;

import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tc.oc.occ.cheaty.BotConfig;
import tc.oc.occ.cheaty.DiscordBot;
import tc.oc.occ.cheaty.DiscordBot.RelayType;
import tc.oc.pgm.api.PGM;
import tc.oc.pgm.api.match.Match;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.util.Audience;
import tc.oc.pgm.util.named.NameStyle;
import tc.oc.pgm.util.text.TextTranslations;

@CommandAlias("cheaty")
public class CheatNotifyCommand extends BaseCommand {

  @Dependency private BotConfig config;
  @Dependency private DiscordBot bot;

  @Subcommand("notify")
  @Syntax("[type] [player] [message]")
  @CommandPermission("cheaty.notify")
  public void notify(CommandSender sender, Boolean isAK, String target, String message) {

    if (!config.isCheatNotifyEnabled() && !isAK) {
      sender.sendMessage(ChatColor.RED + "Cheat alerts are not enabled - Check the config.yml");
      return;
    }

    if (!config.isAKNotifyEnabled() && isAK) {
      sender.sendMessage(
          ChatColor.RED + "AutoKiller alerts are not enabled - Check the config.yml");
      return;
    }

    Component formattedTrigger = text(target, NamedTextColor.DARK_AQUA);

    Player player = Bukkit.getPlayer(target);
    Match match = PGM.get().getMatchManager().getMatch(player);
    MatchPlayer matchPlayer = match != null ? match.getPlayer(player) : null;

    if (matchPlayer != null) {
      formattedTrigger = matchPlayer.getName(NameStyle.VERBOSE);
    }

    // Create component from formatted '&' string
    TextComponent notification = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

    Component formatted =
        text()
            .append(isAK ? config.getAKNotifyPrefix() : config.getCheatNotifyPrefix())
            .append(space())
            .append(formattedTrigger)
            .append(space())
            .append(notification)
            .build();

    Bukkit.getOnlinePlayers().stream()
        .filter(
            user ->
                user.hasPermission(
                    isAK ? config.getAKNotifyPermission() : config.getCheatNotifyPermission()))
        .map(Audience::get)
        .forEach(viewer -> viewer.sendMessage(formatted));

    Audience.get(Bukkit.getConsoleSender()).sendMessage(formatted);

    // Replace '§' chars with '&' as translate legacy uses these
    String legacyString = TextTranslations.translateLegacy(formatted).replace('§', '&');

    if (isAK) {
      bot.sendRelay(legacyString, RelayType.AUTOKILL);
    } else {
      bot.sendRelay(legacyString, RelayType.MATRIX);
    }
  }
}

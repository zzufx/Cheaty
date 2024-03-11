package tc.oc.occ.cheaty;

import dev.pgm.community.events.PlayerReportEvent;
import net.climaxmc.autokiller.events.AutoKillCheatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BotListener implements Listener {

  private final DiscordBot bot;

  public BotListener(DiscordBot bot) {
    this.bot = bot;
  }

  @EventHandler
  public void onPlayerReport(PlayerReportEvent event) {
    bot.sendReport(event);
  }

  @EventHandler
  public void onAutoKillerViolation(AutoKillCheatEvent event) {
    bot.sendAutoKiller(event);
  }
}

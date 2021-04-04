package tc.oc.occ.bolt;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.climaxmc.autokiller.events.AutoKillCheatEvent;
import tc.oc.morpheus.NotifyCommandEvent;
import tc.oc.occ.bolt.DiscordBot.RelayType;
import tc.oc.pgm.community.events.PlayerReportEvent;

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
		bot.sendRelay(event.getAlert(), RelayType.AUTOKILL);
	}
	
	@EventHandler
	public void onMorpheusNotify(NotifyCommandEvent event) {
		bot.sendRelay(event.getCommand(), RelayType.MATRIX);
	}

}

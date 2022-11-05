package tc.oc.occ.cheaty;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CheatyUtils {

  public static String convertCommunityRenderedName(String input) {
    String internal = StringUtils.substringBetween(input, "<", ">");
    String[] parts = internal.split(":");
    String username = null;

    if (parts.length == 2) {
      String uuid = parts[0];
      UUID playerId = UUID.fromString(uuid);

      if (playerId != null) {
        Player player = Bukkit.getPlayer(playerId);
        if (player != null) {
          username = "`" + player.getName() + "`";
        }
      }
    }

    if (username != null) {
      input = input.replace(internal, username);
    }

    return input;
  }
}

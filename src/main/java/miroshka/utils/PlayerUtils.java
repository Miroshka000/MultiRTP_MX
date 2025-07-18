package miroshka.utils;

import cn.nukkit.Player;
import java.util.List;

import static miroshka.utils.LocationUtils.RANDOM;

public class PlayerUtils {
    public static Player findRandomNearbyPlayer(Player player) {
        List<Player> nearby = player.getServer().getOnlinePlayers().values().stream()
                .filter(p -> !p.equals(player))
                .toList();

        return nearby.isEmpty() ? null :
                nearby.get(RANDOM.nextInt(nearby.size()));
    }
}
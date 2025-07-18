package miroshka.utils;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

import java.util.concurrent.CompletableFuture;

public class WorldUtils {
    public static Level resolveTargetLevel(Player player, String targetWorldName) {
        if (targetWorldName == null || targetWorldName.isEmpty()) {
            return player.getLevel();
        }
        return player.getServer().getLevelByName(targetWorldName);
    }

    public static CompletableFuture<Boolean> ensureChunkLoaded(Position position) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                position.getLevel().loadChunk(position.getFloorX() >> 4, position.getFloorZ() >> 4);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }
}
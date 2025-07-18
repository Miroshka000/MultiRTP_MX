package miroshka.utils;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.DustParticle;
import cn.nukkit.utils.BlockColor;

import java.util.concurrent.CompletableFuture;

public class TeleportUtils {
    public static CompletableFuture<Boolean> teleport(Player player, Position position) {
        return WorldUtils.ensureChunkLoaded(position)
                .thenCompose(loaded -> {
                    if (!loaded) {
                        return CompletableFuture.completedFuture(false);
                    }

                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            player.teleport(position);
                            return true;
                        } catch (Exception e) {
                            return false;
                        }
                    });
                });
    }

    public static void playTeleportEffects(Player player) {
        player.getLevel().addSound(player.getLocation(), Sound.MOB_ENDERMEN_PORTAL);

        for (int i = 0; i < 20; i++) {
            player.getLevel().addParticle(new DustParticle(
                    player.getLocation().add(0.0, 1.0, 0.0),
                    BlockColor.PINK_BLOCK_COLOR
            ));
        }
    }
}
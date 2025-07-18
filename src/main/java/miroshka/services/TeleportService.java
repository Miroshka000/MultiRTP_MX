package miroshka.services;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import cn.nukkit.Player;

public interface TeleportService {
    CompletableFuture<TeleportResult> teleportToRandomLocation(Player player);
    CompletableFuture<TeleportResult> teleportNearPlayer(Player player);
    CompletableFuture<TeleportResult> teleportBack(UUID playerUuid);
    CompletableFuture<TeleportResult> teleportToRandomLocation(Player player, String targetWorldName);

    void saveBackLocation(Player player);

    enum TeleportResult {
        SUCCESS,
        NO_BACK_LOCATION,
        NO_NEAR_PLAYERS,
        TELEPORT_FAILED,
        WORLD_NOT_LOADED,
        INTERNAL_ERROR
    }
}
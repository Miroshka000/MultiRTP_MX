package miroshka.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import miroshka.Settings;
import miroshka.services.TeleportService;
import miroshka.utils.LocationUtils;
import miroshka.utils.PlayerUtils;
import miroshka.utils.TeleportUtils;
import miroshka.utils.WorldUtils;

public class TeleportServiceImpl implements TeleportService {
    private final Map<UUID, Location> backLocations = new HashMap<>();

    @Override
    public CompletableFuture<TeleportResult> teleportToRandomLocation(Player player) {
        return teleportToRandomLocation(player, Settings.getTargetWorldName());
    }

    @Override
    public CompletableFuture<TeleportResult> teleportToRandomLocation(Player player, String targetWorldName) {
        if (!player.isOnline()) {
            return CompletableFuture.completedFuture(TeleportResult.TELEPORT_FAILED);
        }

        saveBackLocation(player);

        return CompletableFuture.supplyAsync(() -> {
            Position safeLocation = findRandomSafeLocation(player, targetWorldName);
            if (safeLocation == null) {
                return TeleportResult.TELEPORT_FAILED;
            }

            return TeleportUtils.teleport(player, safeLocation)
                    .thenApply(result -> {
                        if (result) {
                            TeleportUtils.playTeleportEffects(player);
                            return TeleportResult.SUCCESS;
                        }
                        return TeleportResult.TELEPORT_FAILED;
                    }).join();
        });
    }

    @Override
    public CompletableFuture<TeleportResult> teleportNearPlayer(Player player) {
        if (!player.isOnline()) {
            return CompletableFuture.completedFuture(TeleportResult.TELEPORT_FAILED);
        }

        saveBackLocation(player);

        return CompletableFuture.supplyAsync(() -> {
            Position safeLocation = findNearbySafeLocation(player);
            if (safeLocation == null) {
                return TeleportResult.NO_NEAR_PLAYERS;
            }

            return TeleportUtils.teleport(player, safeLocation)
                    .thenApply(result -> {
                        if (result) {
                            TeleportUtils.playTeleportEffects(player);
                            return TeleportResult.SUCCESS;
                        }
                        return TeleportResult.TELEPORT_FAILED;
                    }).join();
        });
    }

    @Override
    public CompletableFuture<TeleportResult> teleportBack(UUID playerUuid) {
        if (!hasBackLocation(playerUuid)) {
            return CompletableFuture.completedFuture(TeleportResult.NO_BACK_LOCATION);
        }

        Location backLocation = getBackLocation(playerUuid);
        Player player = Server.getInstance().getPlayer(playerUuid).orElse(null);

        if (player == null || !player.isOnline()) {
            return CompletableFuture.completedFuture(TeleportResult.TELEPORT_FAILED);
        }

        return TeleportUtils.teleport(player, backLocation)
                .thenApply(result -> {
                    if (result) {
                        removeBackLocation(playerUuid);
                        TeleportUtils.playTeleportEffects(player);
                        return TeleportResult.SUCCESS;
                    }
                    return TeleportResult.TELEPORT_FAILED;
                });
    }

    private Position findRandomSafeLocation(Player player, String targetWorldName) {
        Level targetLevel = WorldUtils.resolveTargetLevel(player, targetWorldName);
        return targetLevel != null ?
                LocationUtils.findRandomSafeLocation(
                        targetLevel,
                        Settings.getMaxX(),
                        Settings.getMaxZ()
                ) : null;
    }

    private Position findNearbySafeLocation(Player player) {
        Player targetPlayer = PlayerUtils.findRandomNearbyPlayer(player);
        if (targetPlayer == null) return null;

        return LocationUtils.findSafeLocationAround(
                targetPlayer.getLocation(),
                Settings.getTeleportNearPlayerRadius()
        );
    }

    public void saveBackLocation(Player player) {
        backLocations.put(player.getUniqueId(), player.getLocation());
    }

    private Location getBackLocation(UUID playerUuid) {
        return backLocations.get(playerUuid);
    }

    private void removeBackLocation(UUID playerUuid) {
        backLocations.remove(playerUuid);
    }

    private boolean hasBackLocation(UUID playerUuid) {
        return backLocations.containsKey(playerUuid);
    }
}
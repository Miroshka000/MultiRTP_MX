package miroshka.services;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class TeleportService {
    private static final Random random = new Random(System.currentTimeMillis());
    private final Map<UUID, Location> backLocations = new HashMap<>();
    private final ConfigService configService;

    public TeleportService(ConfigService configService) {
        this.configService = configService;
    }

    public void saveBackLocation(Player player) {
        backLocations.put(player.getUniqueId(), player.getLocation());
    }

    public Location getBackLocation(UUID playerUuid) {
        return backLocations.get(playerUuid);
    }

    public void removeBackLocation(UUID playerUuid) {
        backLocations.remove(playerUuid);
    }

    public boolean hasBackLocation(UUID playerUuid) {
        return backLocations.containsKey(playerUuid);
    }

    public Position getRandomTeleportLocation(Player player) {
        String targetWorld = configService.getTargetWorldName();
        Position randomPos;
        
        for (int attempts = 0; attempts < 20; attempts++) {
            if (targetWorld == null || targetWorld.isEmpty()) {
                randomPos = new Position(
                    rand(-configService.getMaxX(), configService.getMaxX()),
                    0,
                    rand(-configService.getMaxZ(), configService.getMaxZ()),
                    player.getLevel()
                );
            } else if (player.getServer().isLevelLoaded(targetWorld)) {
                randomPos = new Position(
                    rand(-configService.getMaxX(), configService.getMaxX()),
                    0,
                    rand(-configService.getMaxZ(), configService.getMaxZ()),
                    player.getServer().getLevelByName(targetWorld)
                );
            } else {
                return null;
            }

            Position safePos = getHighestPosition(randomPos);
            if (safePos != null) {
                return safePos;
            }
        }
        
        return null;
    }

    public Position getHighestPosition(Position p) {
        int ex = p.getFloorX();
        int ze = p.getFloorZ();
        
        int maxY = p.getLevel().getHighestBlockAt(ex, ze);
        if (maxY <= 0) return null;

        for (int y = maxY; y >= 0; y--) {
            int blockId = p.getLevel().getBlockIdAt(ex, y, ze);
            
            if (blockId != 0 && blockId != 8 && blockId != 9 && blockId != 10 && blockId != 11) {
                if (p.getLevel().getBlockIdAt(ex, y + 1, ze) == 0 && 
                    p.getLevel().getBlockIdAt(ex, y + 2, ze) == 0) {
                    return new Position(ex + 0.5, y + 1, ze + 0.5, p.getLevel());
                }
            }
        }
        return null;
    }

    private static int rand(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        return min + random.nextInt(max - min + 1);
    }
} 
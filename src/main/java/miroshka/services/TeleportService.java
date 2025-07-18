package miroshka.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;

public class TeleportService {
    private static final Random random = new Random(System.currentTimeMillis());
    private final Map<UUID, Location> backLocations = new HashMap<>();
    private final ConfigService configService;
    private static final int MAX_ATTEMPTS = 1000;

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
        String targetWorldName = configService.getTargetWorldName();
        Level targetLevel;

        if (targetWorldName == null || targetWorldName.isEmpty()) {
            targetLevel = player.getLevel();
        } else {
            targetLevel = player.getServer().getLevelByName(targetWorldName);
            if (targetLevel == null) {
                return null; // World not loaded
            }
        }
        return getRandomTeleportLocationInLevel(targetLevel);
    }

    public Position getRandomTeleportLocationInLevel(Level level) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            attempts++;
            int x = rand(-configService.getMaxX(), configService.getMaxX());
            int z = rand(-configService.getMaxZ(), configService.getMaxZ());
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            level.loadChunk(chunkX, chunkZ);

            Position randomPos = new Position(x, 0, z, level);

            Position safePos = getHighestPosition(randomPos);
            if (safePos != null) {                
                if (attempts > 20) {
                    Server.getInstance().getLogger().debug("Найдена безопасная локация после " + attempts + " попыток");
                }
                
                return safePos;
            }
            
            if (attempts % 100 == 0) {
                Server.getInstance().getLogger().debug("Выполнено " + attempts + " попыток найти безопасную локацию");
            }
        }
        Server.getInstance().getLogger().warning("Не удалось найти безопасную локацию после " + MAX_ATTEMPTS + " попыток в мире " + level.getName());
        return null;
    }

    public Position getNearPlayerTeleportLocation(Player player) {
        List<Player> otherPlayers = new ArrayList<>(player.getServer().getOnlinePlayers().values());
        otherPlayers.remove(player);

        if (otherPlayers.isEmpty()) {
            return null;
        }

        Player targetPlayer = otherPlayers.get(random.nextInt(otherPlayers.size()));
        Location targetLocation = targetPlayer.getLocation();
        int radius = configService.getTeleportNearPlayerRadius();

        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            attempts++;
            int x = rand(targetLocation.getFloorX() - radius, targetLocation.getFloorX() + radius);
            int z = rand(targetLocation.getFloorZ() - radius, targetLocation.getFloorZ() + radius);
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            targetLocation.getLevel().loadChunk(chunkX, chunkZ);
            Position randomPos = new Position(x, 0, z, targetLocation.getLevel());

            Position safePos = getHighestPosition(randomPos);
            if (safePos != null) {                
                if (attempts > 20) {
                    Server.getInstance().getLogger().debug("Найдена безопасная локация после " + attempts + " попыток");
                }
                
                return safePos;
            }
            
            if (attempts % 100 == 0) {
                Server.getInstance().getLogger().debug("Выполнено " + attempts + " попыток найти безопасную локацию");
            }
        }
        Server.getInstance().getLogger().warning("Не удалось найти безопасную локацию после " + MAX_ATTEMPTS + " попыток около игрока");
        return null;
    }

    public Position getHighestPosition(Position p) {
        int ex = p.getFloorX();
        int ze = p.getFloorZ();
        Level level = p.getLevel();
        
        boolean isNether = level.getDimension() == Level.DIMENSION_NETHER;
        int maxY = isNether ? 120 : level.getHighestBlockAt(ex, ze);
        int minY = isNether ? 4 : 0;
        
        if (maxY <= minY) return null;

        for (int y = maxY; y >= minY; y--) {
            int blockId = level.getBlockIdAt(ex, y, ze);
            
            if (blockId != 0 && blockId != 8 && blockId != 9 && blockId != 10 && blockId != 11) {
                if (level.getBlockIdAt(ex, y + 1, ze) == 0 && 
                    level.getBlockIdAt(ex, y + 2, ze) == 0) {
                    return new Position(ex + 0.5, y + 1, ze + 0.5, level);
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
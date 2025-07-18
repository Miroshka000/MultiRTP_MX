package miroshka.utils;

import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.Server;
import java.util.Random;

public class LocationUtils {
    public static final Random RANDOM = new Random();
    private static final int MAX_ATTEMPTS = 1000;

    public static Position findRandomSafeLocation(Level level, int maxX, int maxZ) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            attempts++;

            int x = randomInRange(-maxX, maxX);
            int z = randomInRange(-maxZ, maxZ);
            Position randomPos = new Position(x, 0, z, level);

            Position safePos = findHighestSafePosition(randomPos);
            if (safePos != null) {
                logAttemptSuccess(attempts, "random location");
                return safePos;
            }

            logAttemptProgress(attempts, "random location");
        }
        logAttemptFailure("random location");
        return null;
    }

    public static Position findSafeLocationAround(Location center, int radius) {
        Level level = center.getLevel();
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            attempts++;

            int x = randomInRange(center.getFloorX() - radius, center.getFloorX() + radius);
            int z = randomInRange(center.getFloorZ() - radius, center.getFloorZ() + radius);
            Position randomPos = new Position(x, 0, z, level);

            Position safePos = findHighestSafePosition(randomPos);
            if (safePos != null) {
                logAttemptSuccess(attempts, "near player");
                return safePos;
            }

            logAttemptProgress(attempts, "near player");
        }
        logAttemptFailure("near player");
        return null;
    }

    public static Position findHighestSafePosition(Position pos) {
        int x = pos.getFloorX();
        int z = pos.getFloorZ();
        Level level = pos.getLevel();

        level.loadChunk(x >> 4, z >> 4);

        boolean isNether = level.getDimension() == Level.DIMENSION_NETHER;
        int maxY = isNether ? 120 : level.getHighestBlockAt(x, z);
        int minY = isNether ? 4 : 0;

        if (maxY <= minY) return null;

        for (int y = maxY; y >= minY; y--) {
            if (isSafePosition(level, x, y, z)) {
                return new Position(x + 0.5, y + 1, z + 0.5, level);
            }
        }
        return null;
    }

    private static boolean isSafePosition(Level level, int x, int y, int z) {
        int blockId = level.getBlockIdAt(x, y, z);
        boolean isSolid = blockId != 0 && blockId != 8 && blockId != 9 && blockId != 10 && blockId != 11;
        return isSolid &&
                level.getBlockIdAt(x, y + 1, z) == 0 &&
                level.getBlockIdAt(x, y + 2, z) == 0;
    }

    private static int randomInRange(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        return min + RANDOM.nextInt(max - min + 1);
    }

    private static void logAttemptSuccess(int attempts, String locationType) {
        if (attempts > 20) {
            Server.getInstance().getLogger().debug(
                    "Found safe location after " + attempts + " attempts (" + locationType + ")"
            );
        }
    }

    private static void logAttemptProgress(int attempts, String locationType) {
        if (attempts % 100 == 0) {
            Server.getInstance().getLogger().debug(
                    "Attempted " + attempts + " times to find safe location (" + locationType + ")"
            );
        }
    }

    private static void logAttemptFailure(String locationType) {
        Server.getInstance().getLogger().warning(
                "Failed to find safe location after " + MAX_ATTEMPTS + " attempts (" + locationType + ")"
        );
    }
}
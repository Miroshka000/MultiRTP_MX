package miroshka.services;

import cn.nukkit.utils.Config;

public class ConfigService {
    private final Config config;

    public ConfigService(Config config) {
        this.config = config;
    }

    public String getNoBackLocationMessage() {
        return config.getString("ErrorMessages.NoBackLocation");
    }

    public String getNoPermissionMessage() {
        return config.getString("ErrorMessages.NoPermission");
    }

    public String getNotPlayerMessage() {
        return config.getString("ErrorMessages.NotPlayer");
    }

    public String getTeleportFailMessage() {
        return config.getString("TeleportFail");
    }

    public String getTargetWorldName() {
        return config.getString("targetWorldName");
    }

    public int getMaxX() {
        return config.getInt("x");
    }

    public int getMaxZ() {
        return config.getInt("z");
    }

    public String getTitle() {
        return config.getString("Title");
    }

    public String getSubtitle() {
        return config.getString("Subtitle");
    }

    public boolean isWaterCheckEnabled() {
        return config.getInt("Watercheck") == 1;
    }

    public int getWaterBreathingDuration() {
        return config.getInt("Duration");
    }
} 
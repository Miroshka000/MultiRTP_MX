package miroshka;

import cn.nukkit.utils.Config;
import java.util.HashMap;
import java.util.Map;

public class Settings {
    private static final Map<String, String> messages = new HashMap<>();
    private static final Map<String, String> forms = new HashMap<>();
    private static final Map<String, Integer> settings = new HashMap<>();
    private static final Map<String, String> permissions = new HashMap<>();
    private static Config pluginConfig;

    private static boolean formLibraryAvailable = false;

    public static void init(Config config) {
        pluginConfig = config;

        messages.put("NoBackLocation", config.getString("ErrorMessages.NoBackLocation"));
        messages.put("NoPermission", config.getString("ErrorMessages.NoPermission"));
        messages.put("NotPlayer", config.getString("ErrorMessages.NotPlayer"));
        messages.put("TeleportFail", config.getString("ErrorMessages.TeleportFail"));
        messages.put("NoPlayersFound", config.getString("ErrorMessages.NoPlayersFound"));
        messages.put("Title", config.getString("Title"));
        messages.put("Subtitle", config.getString("Subtitle"));
        messages.put("WorldNotLoaded", config.getString("ErrorMessages.WorldNotLoaded"));
        messages.put("InternalError", config.getString("ErrorMessages.InternalError"));

        forms.put("title", config.getString("forms.title"));
        forms.put("content", config.getString("forms.content"));
        forms.put("button.random", config.getString("forms.buttons.random"));
        forms.put("button.near", config.getString("forms.buttons.near"));
        forms.put("worldSelection.title", config.getString("forms.worldSelection.title"));
        forms.put("worldSelection.content", config.getString("forms.worldSelection.content"));
        forms.put("worldSelection.allowOtherWorlds", String.valueOf(config.getBoolean("forms.worldSelection.allowOtherWorlds")));
        settings.put("allowOtherWorlds", config.getBoolean("forms.worldSelection.allowOtherWorlds") ? 1 : 0);

        settings.put("maxX", config.getInt("x"));
        settings.put("maxZ", config.getInt("z"));
        settings.put("waterCheck", config.getInt("Watercheck"));
        settings.put("waterBreathingDuration", config.getInt("Duration"));
        settings.put("teleportNearRadius", config.getInt("teleportNearPlayer.radius"));

        permissions.put("rtp", "miroshka.rtp");
        permissions.put("rtp.form", "miroshka.rtp.form");
        permissions.put("rtp.near", "miroshka.rtp.near");
        permissions.put("back", "miroshka.back");
    }

    public static String getNoBackLocationMessage() {
        return messages.get("NoBackLocation");
    }

    public static String getWorldNotLoadedMessage() {
        return messages.get("WorldNotLoaded");
    }

    public static String getInternalErrorMessage() {
        return messages.get("InternalError");
    }

    public static String getNoPermissionMessage() {
        return messages.get("NoPermission");
    }

    public static String getNotPlayerMessage() {
        return messages.get("NotPlayer");
    }

    public static String getTeleportFailMessage() {
        return messages.get("TeleportFail");
    }

    public static String getNoPlayersFoundMessage() {
        return messages.get("NoPlayersFound");
    }

    public static String getTitle() {
        return messages.get("Title");
    }

    public static String getSubtitle() {
        return messages.get("Subtitle");
    }

    public static String getTargetWorldName() {
        return pluginConfig.getString("targetWorldName");
    }

    public static int getMaxX() {
        return settings.get("maxX");
    }

    public static int getMaxZ() {
        return settings.get("maxZ");
    }

    public boolean isWaterCheckEnabled() {
        return settings.get("waterCheck") == 1;
    }

    public static int getWaterBreathingDuration() {
        return settings.get("waterBreathingDuration");
    }

    public boolean isTeleportNearPlayerEnabled() {
        return pluginConfig.getBoolean("teleportNearPlayer.enabled");
    }

    public static int getTeleportNearPlayerRadius() {
        return settings.get("teleportNearRadius");
    }

    public static boolean isFormsEnabled() {
        return pluginConfig.getBoolean("forms.enabled") && formLibraryAvailable;
    }

    public static void setFormLibraryAvailable(boolean available) {
        formLibraryAvailable = available;
    }

    public static String getFormTitle() {
        return forms.get("title");
    }

    public static String getFormContent() {
        return forms.get("content");
    }

    public static String getFormButtonRandom() {
        return forms.get("button.random");
    }

    public static String getFormButtonNear() {
        return forms.get("button.near");
    }

    public static String getFormWorldSelectionTitle() {
        return forms.get("worldSelection.title");
    }

    public static String getFormWorldSelectionContent() {
        return forms.get("worldSelection.content");
    }

    public static boolean isAllowOtherWorlds() {
        return settings.get("allowOtherWorlds") == 1;
    }

    public static String getRtpPermission() {
        return permissions.get("rtp");
    }

    public static String getRtpFormPermission() {
        return permissions.get("rtp.form");
    }

    public static String getRtpNearPermission() {
        return permissions.get("rtp.near");
    }

    public static String getBackPermission() {
        return permissions.get("back");
    }
}
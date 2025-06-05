package miroshka.services;

import cn.nukkit.utils.Config;

public class ConfigService {
    private final Config config;
    private boolean formLibraryAvailable = false;

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
        return config.getString("ErrorMessages.TeleportFail");
    }

    public String getNoPlayersFoundMessage() {
        return config.getString("ErrorMessages.NoPlayersFound");
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

    public boolean isFormsEnabled() {
        return config.getBoolean("forms.enabled") && formLibraryAvailable;
    }

    public void setFormLibraryAvailable(boolean available) {
        this.formLibraryAvailable = available;
    }

    public boolean isFormLibraryAvailable() {
        return formLibraryAvailable;
    }

    public String getFormTitle() {
        return config.getString("forms.title");
    }

    public String getFormContent() {
        return config.getString("forms.content");
    }

    public String getFormButtonRandom() {
        return config.getString("forms.buttons.random");
    }

    public String getFormButtonNear() {
        return config.getString("forms.buttons.near");
    }

    public String getFormWorldSelectionTitle() {
        return config.getString("forms.worldSelection.title");
    }

    public String getFormWorldSelectionContent() {
        return config.getString("forms.worldSelection.content");
    }

    public boolean isTeleportNearPlayerEnabled() {
        return config.getBoolean("teleportNearPlayer.enabled");
    }

    public int getTeleportNearPlayerRadius() {
        return config.getInt("teleportNearPlayer.radius");
    }

    public String getRtpPermission() {
        return "miroshka.rtp";
    }

    public String getRtpFormPermission() {
        return "miroshka.rtp.form";
    }

    public String getRtpNearPermission() {
        return "miroshka.rtp.near";
    }

    public String getBackPermission() {
        return "miroshka.back";
    }
} 
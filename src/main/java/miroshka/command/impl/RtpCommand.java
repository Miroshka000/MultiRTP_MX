package miroshka.command.impl;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import miroshka.Settings;
import miroshka.command.BaseCommand;
import miroshka.services.TeleportService;
import miroshka.form.RandomTeleportForm;

public class RtpCommand extends BaseCommand {
    private final TeleportService teleportService;
    private final RandomTeleportForm randomTeleportForm;

    public RtpCommand(String name, TeleportService teleportService, RandomTeleportForm randomTeleportForm, Config config) {
        super(name, config.getSection("commands." + name));
        this.teleportService = teleportService;
        this.randomTeleportForm = randomTeleportForm;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Settings.getNotPlayerMessage());
            return true;
        }

        if (!player.hasPermission(Settings.getRtpPermission())) {
            player.sendMessage(Settings.getNoPermissionMessage());
            return true;
        }

        if (Settings.isFormsEnabled()) {
            if (!player.hasPermission(Settings.getRtpFormPermission())) {
                player.sendMessage(Settings.getNoPermissionMessage());
                return true;
            }
            randomTeleportForm.sendMainRtpForm(player);
        } else {
            teleportService.saveBackLocation(player);

            teleportService.teleportToRandomLocation(player).thenAccept(result -> {
                switch (result) {
                    case SUCCESS:
                        player.sendTitle(Settings.getTitle(), Settings.getSubtitle());
                        break;
                    case WORLD_NOT_LOADED:
                        player.sendMessage(Settings.getWorldNotLoadedMessage());
                        break;
                    case INTERNAL_ERROR:
                        player.sendMessage(Settings.getInternalErrorMessage());
                        break;
                    default:
                        player.sendMessage(Settings.getTeleportFailMessage());
                        break;
                }
            });
        }

        return true;
    }
}
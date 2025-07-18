package miroshka.command.impl;

import cn.nukkit.utils.Config;
import miroshka.Settings;
import miroshka.command.BaseCommand;
import miroshka.services.TeleportService;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;

import java.util.UUID;

public class BackCommand extends BaseCommand {
    private final TeleportService teleportService;

    public BackCommand(String name, TeleportService teleportService, Config config) {
        super(name, config.getSection("commands." + name));
        this.teleportService = teleportService;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Settings.getNotPlayerMessage());
            return true;
        }

        UUID playerUuid = player.getUniqueId();

        if (!player.hasPermission(Settings.getBackPermission())) {
            player.sendMessage(Settings.getNoPermissionMessage());
            return true;
        }

        teleportService.teleportBack(playerUuid).thenAcceptAsync(result -> {
            switch (result) {
                case SUCCESS:
                    break;
                case NO_BACK_LOCATION:
                    player.sendMessage(Settings.getNoBackLocationMessage());
                    break;
                case TELEPORT_FAILED:
                default:
                    player.sendMessage(Settings.getTeleportFailMessage());
                    break;
            }
        });

        return true;
    }
}
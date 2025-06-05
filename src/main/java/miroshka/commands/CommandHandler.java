package miroshka.commands;

import miroshka.services.ConfigService;
import miroshka.services.TeleportService;
import miroshka.ui.FormManager;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;

public class CommandHandler {
    private final TeleportService teleportService;
    private final ConfigService configService;
    private final FormManager formManager;

    public CommandHandler(TeleportService teleportService, ConfigService configService, FormManager formManager) {
        this.teleportService = teleportService;
        this.configService = configService;
        this.formManager = formManager;
    }

    public boolean handleCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configService.getNotPlayerMessage());
            return true;
        }

        Player player = (Player) sender;

        switch (cmd.getName()) {
            case "rtp":
                return handleRtpCommand(player);
            case "back":
                return handleBackCommand(player);
            default:
                return false;
        }
    }

    private boolean handleRtpCommand(Player player) {
        if (!player.hasPermission(configService.getRtpPermission())) {
            player.sendMessage(configService.getNoPermissionMessage());
            return true;
        }

        if (configService.isFormsEnabled()) {
            if (!player.hasPermission(configService.getRtpFormPermission())) {
                player.sendMessage(configService.getNoPermissionMessage());
                return true;
            }
            formManager.sendMainRtpForm(player);
        } else {
            teleportService.saveBackLocation(player);
            Position safeLocation = teleportService.getRandomTeleportLocation(player);
        
            if (safeLocation != null) {
                player.teleport(safeLocation);
                player.sendTitle(configService.getTitle(), configService.getSubtitle());
            } else {
                player.sendMessage(configService.getTeleportFailMessage());
            }
        }
        return true;
    }

    private boolean handleBackCommand(Player player) {
        if (!player.hasPermission(configService.getBackPermission())) {
            player.sendMessage(configService.getNoPermissionMessage());
            return true;
        }

        if (teleportService.hasBackLocation(player.getUniqueId())) {
            player.teleport(teleportService.getBackLocation(player.getUniqueId()));
            teleportService.removeBackLocation(player.getUniqueId());
        } else {
            player.sendMessage(configService.getNoBackLocationMessage());
        }
        return true;
    }
} 
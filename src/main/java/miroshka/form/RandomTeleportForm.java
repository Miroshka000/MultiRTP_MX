package miroshka.form;

import com.formconstructor.form.SimpleForm;
import com.formconstructor.form.element.simple.Button;
import com.formconstructor.form.element.simple.ImageType;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import miroshka.Settings;
import miroshka.services.TeleportService;

public class RandomTeleportForm {

    private final Server server;
    private final TeleportService teleportService;

    public RandomTeleportForm(Server server, TeleportService teleportService) {
        this.server = server;
        this.teleportService = teleportService;
    }

    public void sendMainRtpForm(Player player) {
        if (!Settings.isFormsEnabled()) {
            handleTeleport(player);
            return;
        }

        SimpleForm form = new SimpleForm("§l§6" + Settings.getFormTitle());
        form.addContent("§f§o" + Settings.getFormContent());

        form.addButton(new Button("§l§b" + Settings.getFormButtonRandom())
                .setImage(ImageType.PATH, "textures/items/ender_pearl")
                .onClick((p, button) -> sendWorldSelectionForm(p)));

        if (player.hasPermission(Settings.getRtpNearPermission())) {
            form.addButton(new Button("§l§d" + Settings.getFormButtonNear())
                    .setImage(ImageType.PATH, "textures/items/compass_item")
                    .onClick((p, button) -> handleTeleportNearPlayer(p)));
        }

        form.setCloseHandler(p -> {});
        form.send(player);
    }

    public void sendWorldSelectionForm(Player player) {
        if (!Settings.isFormsEnabled()) {
            handleTeleport(player);
            return;
        }

        SimpleForm form = new SimpleForm("§l§a" + Settings.getFormWorldSelectionTitle());
        form.addContent("§f§o" + Settings.getFormWorldSelectionContent());

        for (Level level : server.getLevels().values()) {
            String worldName = level.getName();

            String texturePath;
            if (worldName.toLowerCase().contains("nether")) {
                texturePath = "textures/blocks/netherrack";
            } else if (worldName.toLowerCase().contains("end")) {
                texturePath = "textures/blocks/end_stone";
            } else {
                texturePath = "textures/blocks/grass_side_carried";
            }

            form.addButton(new Button("§l§e" + worldName)
                    .setImage(ImageType.PATH, texturePath)
                    .onClick((p, button) -> handleTeleportToWorld(p)));
        }

        form.setCloseHandler(p -> {});
        form.send(player);
    }

    private void handleTeleport(Player player) {
        teleportService.saveBackLocation(player);

        teleportService.teleportToRandomLocation(player).thenAcceptAsync(result -> {
            switch (result) {
                case SUCCESS:
                    player.sendTitle(Settings.getTitle(), Settings.getSubtitle());
                    break;
                case TELEPORT_FAILED:
                case WORLD_NOT_LOADED:
                case INTERNAL_ERROR:
                default:
                    player.sendMessage(Settings.getTeleportFailMessage());
                    break;
            }
        });
    }

    private void handleTeleportNearPlayer(Player player) {
        teleportService.saveBackLocation(player);

        teleportService.teleportNearPlayer(player).thenAcceptAsync(result -> {
            switch (result) {
                case SUCCESS:
                    player.sendTitle(Settings.getTitle(), Settings.getSubtitle());
                    break;
                case NO_NEAR_PLAYERS:
                    player.sendMessage(Settings.getNoPlayersFoundMessage());
                    break;
                case TELEPORT_FAILED:
                case INTERNAL_ERROR:
                default:
                    player.sendMessage(Settings.getTeleportFailMessage());
                    break;
            }
        });
    }

    private void handleTeleportToWorld(Player player) {
        teleportService.saveBackLocation(player);

        teleportService.teleportToRandomLocation(player).thenAcceptAsync(result -> {
            switch (result) {
                case SUCCESS:
                    player.sendTitle(Settings.getTitle(), Settings.getSubtitle());
                    break;
                case TELEPORT_FAILED:
                case WORLD_NOT_LOADED:
                case INTERNAL_ERROR:
                default:
                    player.sendMessage(Settings.getTeleportFailMessage());
                    break;
            }
        });
    }
}
package miroshka.ui;

import com.formconstructor.form.SimpleForm;
import com.formconstructor.form.element.simple.Button;
import com.formconstructor.form.element.simple.ImageType;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.Task;
import miroshka.services.ConfigService;
import miroshka.services.TeleportService;


public class FormManager {

    private final ConfigService configService;
    private final Server server;
    private final TeleportService teleportService;

    public FormManager(ConfigService configService, Server server, TeleportService teleportService) {
        this.configService = configService;
        this.server = server;
        this.teleportService = teleportService;
    }

    public void sendMainRtpForm(Player player) {
        if (!configService.isFormLibraryAvailable()) {
            teleportService.saveBackLocation(player);
            Position safeLocation = teleportService.getRandomTeleportLocation(player);
            
            if (safeLocation != null) {
                player.teleport(safeLocation);
                player.sendTitle(configService.getTitle(), configService.getSubtitle());
            } else {
                player.sendMessage(configService.getTeleportFailMessage());
            }
            return;
        }
        
        SimpleForm form = new SimpleForm("§l§6" + configService.getFormTitle());
        form.addContent("§f§o" + configService.getFormContent());
        
        form.addButton(new Button("§l§b" + configService.getFormButtonRandom())
            .setImage(ImageType.PATH, "textures/items/ender_pearl")
            .onClick((p, button) -> {
                sendWorldSelectionForm(p);
            }));
        
        if (configService.isTeleportNearPlayerEnabled() && player.hasPermission(configService.getRtpNearPermission())) {
            form.addButton(new Button("§l§d" + configService.getFormButtonNear())
                .setImage(ImageType.PATH, "textures/items/compass_item")
                .onClick((p, button) -> {
                    teleportNearPlayer(p);
                }));
        }
        
        form.setCloseHandler(p -> {
        });
        
        form.send(player);
    }

    public void sendWorldSelectionForm(Player player) {
        if (!configService.isFormLibraryAvailable()) {
            teleportService.saveBackLocation(player);
            Position safeLocation = teleportService.getRandomTeleportLocation(player);
            
            if (safeLocation != null) {
                player.teleport(safeLocation);
                player.sendTitle(configService.getTitle(), configService.getSubtitle());
            } else {
                player.sendMessage(configService.getTeleportFailMessage());
            }
            return;
        }
        
        SimpleForm form = new SimpleForm("§l§a" + configService.getFormWorldSelectionTitle());
        form.addContent("§f§o" + configService.getFormWorldSelectionContent());
        
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
                .onClick((p, button) -> {
                    teleportToWorld(p, level);
                }));
        }
        
        form.setCloseHandler(p -> {
        });
        
        form.send(player);
    }
    
    private void teleportToWorld(Player player, Level level) {
        teleportService.saveBackLocation(player);
        server.getScheduler().scheduleAsyncTask(new AsyncTask() {
            @Override
            public void onRun() {
                Position safeLocation = teleportService.getRandomTeleportLocationInLevel(level);
                server.getScheduler().scheduleTask(new Task() {
                    @Override
                    public void onRun(int currentTick) {
                        if (safeLocation != null) {
                            player.teleport(safeLocation);
                            player.sendTitle(configService.getTitle(), configService.getSubtitle());
                        } else {
                            player.sendMessage(configService.getTeleportFailMessage());
                        }
                    }
                });
            }
        });
    }
    
    private void teleportNearPlayer(Player player) {
        teleportService.saveBackLocation(player);
        server.getScheduler().scheduleAsyncTask(new AsyncTask() {
            @Override
            public void onRun() {
                Position safeLocation = teleportService.getNearPlayerTeleportLocation(player);
                server.getScheduler().scheduleTask(new Task() {
                    @Override
                    public void onRun(int currentTick) {
                        if (safeLocation != null) {
                            player.teleport(safeLocation);
                            player.sendTitle(configService.getTitle(), configService.getSubtitle());
                        } else {
                            player.sendMessage(configService.getNoPlayersFoundMessage());
                        }
                    }
                });
            }
        });
    }
} 
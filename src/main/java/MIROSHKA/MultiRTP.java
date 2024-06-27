package MIROSHKA;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class MultiRTP extends PluginBase implements Listener {
    private static final Random random = new Random(System.currentTimeMillis());
    private Map<UUID, Location> backloc = new HashMap<>();
    private String noBackLocationMessage;
    private String noPermissionMessage;
    private String notPlayerMessage;
    private String teleportFailMessage;
    private String targetWorldName;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.loadConfigMessages();
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    private void loadConfigMessages() {
        this.noBackLocationMessage = this.getConfig().getString("ErrorMessages.NoBackLocation", "§cУ вас нет предыдущего сохраненного местоположения.");
        this.noPermissionMessage = this.getConfig().getString("ErrorMessages.NoPermission", "§cУ вас нет разрешения на использование этой команды.");
        this.notPlayerMessage = this.getConfig().getString("ErrorMessages.NotPlayer", "§cЭту команду может использовать только игрок.");
        this.teleportFailMessage = this.getConfig().getString("TeleportFail", "§cНе удалось найти подходящую локацию для телепортации.");
        this.targetWorldName = this.getConfig().getString("targetWorldName");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(this.notPlayerMessage);
            return true;
        }

        Player player = (Player) sender;
        UUID u = player.getUniqueId();
        Location location = player.getLocation();

        switch (cmd.getName()) {
            case "rtp":
                if (player.hasPermission("miroshka.rtp")) {
                    this.backloc.put(u, location);
                    Position loc = getRandomTeleportLocation(player);
                    if (loc != null) {
                        player.teleport(loc.add(0, 1, 0));
                        player.sendTitle(this.getConfig().getString("Title"), this.getConfig().getString("Subtitle"));
                        this.getServer().getScheduler().scheduleDelayedTask(this, () -> {
                            player.teleport(this.getHighestPosition(player.getPosition()));
                        }, 20);
                    } else {
                        player.sendMessage(this.teleportFailMessage);
                    }
                } else {
                    player.sendMessage(this.noPermissionMessage);
                }
                return true;
            case "b":
                if (player.hasPermission("miroshka.back")) {
                    if (this.backloc.containsKey(u)) {
                        Location deathLocation = this.backloc.get(u);
                        player.teleport(deathLocation);
                        this.backloc.remove(u);
                    } else {
                        player.sendMessage(this.noBackLocationMessage);
                    }
                } else {
                    player.sendMessage(this.noPermissionMessage);
                }
                return true;
            default:
                return false;
        }
    }

    private Position getHighestPosition(Position p) {
        int ex = p.getFloorX();
        int ze = p.getFloorZ();

        for (int y = 256; y >= 0; --y) {
            if (p.getLevel().getBlock(new Position(ex, y, ze)).isSolid()) {
                return new Position(ex + 0.5, y + 1, ze + 0.5, p.getLevel());
            }
        }
        return null;
    }

    private Position getRandomTeleportLocation(Player player) {
        if (player != null && this.targetWorldName != null && !this.targetWorldName.isEmpty()) {
            Position loc = null;
            if (player.getServer().isLevelLoaded(this.targetWorldName)) {
                loc = new Position(rand(-this.getConfig().getInt("x"), this.getConfig().getInt("x")),
                        -70.0,
                        rand(-this.getConfig().getInt("z"), this.getConfig().getInt("z")),
                        player.getServer().getLevelByName(this.targetWorldName));
            }
            return loc;
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

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.RED + "Плагин выключен");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID u = player.getUniqueId();
        Location deathLocation = player.getLocation();
        this.backloc.put(u, deathLocation);
        // player.teleport(deathLocation);
    }

    @Override
    public void saveDefaultConfig() {
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.getDataFolder().mkdirs();
            try (InputStream is = this.getResource("config.yml");
                 FileOutputStream os = new FileOutputStream(configFile)) {
                byte[] buffer = new byte[4096];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                this.getLogger().info("Конфигурационный файл создан.");
            } catch (IOException e) {
                this.getLogger().error("Ошибка при создании конфигурационного файла: " + e.getMessage());
            }
        }
    }
}

package MIROSHKA;

import MIROSHKA.commands.CommandHandler;
import MIROSHKA.services.ConfigService;
import MIROSHKA.services.TeleportService;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

public class MultiRTP extends PluginBase implements Listener {
    private CommandHandler commandHandler;
    private TeleportService teleportService;
    private ConfigService configService;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        
        this.configService = new ConfigService(this.getConfig());
        this.teleportService = new TeleportService(configService);
        this.commandHandler = new CommandHandler(teleportService, configService);
        
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info(TextFormat.GREEN + "MultiRTP успешно загружен!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return commandHandler.handleCommand(sender, command, label, args);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        teleportService.saveBackLocation(player);
    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.RED + "MultiRTP выключен");
    }
}

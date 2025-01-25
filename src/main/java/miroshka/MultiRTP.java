package miroshka;

import miroshka.commands.CommandHandler;
import miroshka.services.ConfigService;
import miroshka.services.TeleportService;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.plugin.PluginBase;

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
}

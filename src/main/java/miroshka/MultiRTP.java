package miroshka;

import miroshka.commands.CommandHandler;
import miroshka.services.ConfigService;
import miroshka.services.TeleportService;
import miroshka.ui.FormManager;
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
    private FormManager formManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        
        this.configService = new ConfigService(this.getConfig());
        this.teleportService = new TeleportService(configService);
        
        boolean formLibraryAvailable = false;
        try {
            Class.forName("com.formconstructor.form.SimpleForm");
            configService.setFormLibraryAvailable(true);
            formLibraryAvailable = true;
            this.getLogger().info(TextFormat.GREEN + "FormConstructor found! Enabling form support.");
        } catch (ClassNotFoundException e) {
            configService.setFormLibraryAvailable(false);
            formLibraryAvailable = false;
            this.getLogger().warning(TextFormat.RED + "FormConstructor not found! UI forms are disabled.");
            this.getLogger().warning(TextFormat.YELLOW + "Install FormConstructor version 3.0.0 from GitHub:");
            this.getLogger().warning(TextFormat.AQUA + "https://github.com/MEFRREEX/FormConstructor/releases/tag/3.0.0");
        }
        
        this.formManager = new FormManager(configService, this.getServer(), teleportService);
        this.commandHandler = new CommandHandler(teleportService, configService, formManager);
        
        this.getServer().getPluginManager().registerEvents(this, this);
        
        this.getLogger().info(TextFormat.GREEN + "MultiRTP " + TextFormat.YELLOW + "v" + this.getDescription().getVersion() + TextFormat.GREEN + " successfully enabled!");
        this.getLogger().info(TextFormat.AQUA + "-----------------------------------");
        this.getLogger().info(TextFormat.YELLOW + "Follow us:");
        this.getLogger().info(TextFormat.BLUE + "• Telegram: " + TextFormat.WHITE + "https://t.me/ForgePlugins");
        this.getLogger().info(TextFormat.BLUE + "• VKontakte: " + TextFormat.WHITE + "https://vk.com/forgeplugin");
        this.getLogger().info(TextFormat.BLUE + "• GitHub: " + TextFormat.WHITE + "https://github.com/Miroshka000/MountKingMX");
        this.getLogger().info(TextFormat.AQUA + "-----------------------------------");
        
        if (this.getConfig().getBoolean("forms.enabled") && !formLibraryAvailable) {
            this.getLogger().warning(TextFormat.RED + "Forms are enabled in config, but FormConstructor not found.");
            this.getLogger().warning(TextFormat.RED + "Teleportation will be performed without forms.");
        }
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

package miroshka;

import cn.nukkit.Server;
import cn.nukkit.command.CommandMap;
import miroshka.command.impl.BackCommand;
import miroshka.command.impl.RtpCommand;
import miroshka.services.TeleportService;
import miroshka.services.impl.TeleportServiceImpl;
import miroshka.form.RandomTeleportForm;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

public class MultiRTP extends PluginBase implements Listener {
    private TeleportService teleportService;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.teleportService = new TeleportServiceImpl();
        Settings.init(this.getConfig());
        
        boolean formLibraryAvailable;
        try {
            Class.forName("com.formconstructor.form.SimpleForm");
            Settings.setFormLibraryAvailable(true);
            formLibraryAvailable = true;
            this.getLogger().info(TextFormat.GREEN + "FormConstructor found! Enabling form support.");
        } catch (ClassNotFoundException e) {
            Settings.setFormLibraryAvailable(false);
            formLibraryAvailable = false;
            this.getLogger().warning(TextFormat.RED + "FormConstructor not found! UI forms are disabled.");
            this.getLogger().warning(TextFormat.YELLOW + "Install FormConstructor version 3.0.0 from GitHub:");
            this.getLogger().warning(TextFormat.AQUA + "https://github.com/MEFRREEX/FormConstructor/releases/tag/3.0.0");
        }

        RandomTeleportForm randomTeleportForm = new RandomTeleportForm(this.getServer(), teleportService);

        CommandMap commandMap = Server.getInstance().getCommandMap();

        commandMap.register("miroshka", new RtpCommand(
                "rtp",
                teleportService,
                randomTeleportForm,
                this.getConfig()
        ));

        commandMap.register("miroshka", new BackCommand(
                "back",
                teleportService,
                this.getConfig()
        ));
        
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

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        teleportService.saveBackLocation(player);
    }
}

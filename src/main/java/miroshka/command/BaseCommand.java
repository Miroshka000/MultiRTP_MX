package miroshka.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.ConfigSection;

public abstract class BaseCommand extends Command {
   public BaseCommand(String name, ConfigSection command) {
      super(command.getString("name"));
      this.setDescription(command.getString("description"));
      this.setAliases(command.getStringList("aliases").toArray(new String[0]));
      this.setPermission("miroshka." + name);
   }
}

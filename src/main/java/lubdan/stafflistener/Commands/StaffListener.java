package lubdan.stafflistener.Commands;

import lubdan.stafflistener.events.Chat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StaffListener implements CommandExecutor {

    private lubdan.stafflistener.StaffListener plugin;

    public StaffListener(lubdan.stafflistener.StaffListener instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("StaffListener.WriteReport")) {
            if (args.length == 2 && args[0].equals("getreport")) {
                if (args[1].equals("*")) {
                    if (plugin.dumpDatabase()) {
                        sender.sendMessage(ChatColor.GREEN + "Log file has been created; please check the StaffListener folder for the dump.");
                    } else {
                        sender.sendMessage("There was an error.. I'd check console if I were you!");
                    }
                    return true;
                } else {

                    if (plugin.dumpPlayer(args[1])) {
                        sender.sendMessage(ChatColor.GREEN + "Log file has been created; please check the StaffListener folder for the dump.");
                    } else {
                        sender.sendMessage("There was an error.. I'd check console if I were you!");
                    }
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /stafflistener getreport [NAME]");
                return false;
            }
        } else {
            sender.sendMessage(ChatColor.GREEN + "Server is running StaffListener 2.0 by Lubdan.");
            return false;
        }
    }
}

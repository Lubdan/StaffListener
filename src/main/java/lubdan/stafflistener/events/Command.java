package lubdan.stafflistener.events;

import lubdan.stafflistener.StaffListener;
import lubdan.stafflistener.util.Type;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Command implements Listener {

   private StaffListener plugin;

    public Command(StaffListener instance){
        this.plugin = instance;
    }

    @EventHandler
    private void onCommand(PlayerCommandPreprocessEvent event){
        if(event.getPlayer().hasPermission("StaffListener.log")){
            Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    Location loc = event.getPlayer().getLocation();
                    LocalDateTime ldt = LocalDateTime.now();
                    String datetime = ldt.format(DateTimeFormatter.ISO_DATE_TIME).replace("T"," ").substring(0,16);;
                    plugin.writeLog(Type.Command,event.getMessage(),event.getPlayer().getUniqueId().toString(),event.getPlayer().getName(),loc.getX(),loc.getY(),loc.getZ(),datetime);
                }
            });
        }
    }


}

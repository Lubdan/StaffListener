package lubdan.stafflistener.events;

import lubdan.stafflistener.StaffListener;
import lubdan.stafflistener.util.Type;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Teleport implements Listener {

    private StaffListener plugin;

    public Teleport(StaffListener instance){
        this.plugin = instance;
    }

    @EventHandler
    private void onTeleport(PlayerTeleportEvent event){
        if(event.getPlayer().hasPermission("StaffListener.log")){
            Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    Location loc = event.getPlayer().getLocation();
                    Location to = event.getTo();
                    Location from = event.getFrom();
                    LocalDateTime ldt = LocalDateTime.now();
                    String datetime = ldt.format(DateTimeFormatter.ISO_DATE_TIME).replace("T"," ").substring(0,16);
                    String context = event.getPlayer().getName() + " teleported from X/Y/Z: " + from.getX() + "/"+from.getY() +"/"+from.getZ() + " to X/Y/Z: "+ to.getX() + "/" + to.getY() + "/" + to.getZ()+".";
                    plugin.writeLog(Type.Teleport,context,event.getPlayer().getUniqueId().toString(),event.getPlayer().getName(),loc.getX(),loc.getY(),loc.getZ(),datetime);
                }
            });
        }
    }

}

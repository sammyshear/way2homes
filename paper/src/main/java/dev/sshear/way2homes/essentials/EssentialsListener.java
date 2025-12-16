package dev.sshear.way2homes.essentials;

import dev.sshear.payloads.DelHomePayloadSender;
import dev.sshear.payloads.HomePayloadSender;
import dev.sshear.way2homes.*;
import net.essentialsx.api.v2.events.HomeModifyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EssentialsListener implements Listener {

    @EventHandler
    public void setHomeEvent(HomeModifyEvent event) {
        var owner = event.getHomeOwner();
        var newLoc = event.getNewLocation();
        var name = event.getNewName();
        if (newLoc != null) {
            var home = new Home(newLoc.getBlockX(), newLoc.getBlockY(), newLoc.getBlockZ(), name);
            try {
                var homeData = new HomeData(home.getName(), home.getX(), home.getY(), home.getZ());
                HomePayloadSender.sendHomeData(owner.getBase(), homeData);
            } catch (Exception e) {
                Constants.LOG.error("Failed to send home data for home: {}", home.getName(), e);
            }
        } else {
            var oldLoc = event.getOldLocation();
            var home = new Home(oldLoc.getBlockX(), oldLoc.getBlockY(), oldLoc.getBlockZ(), event.getOldName());
            try {
                var homeData = new HomeData(home.getName(), home.getX(), home.getY(), home.getZ());
                DelHomePayloadSender.sendHomeData(owner.getBase(), homeData);
            } catch (Exception e) {
                Constants.LOG.error("Failed to send home data for home: {}", home.getName(), e);
            }
        }
    }
}
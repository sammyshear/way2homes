package dev.sshear.way2homes.essentials;

import dev.sshear.way2homes.payloads.DelHomePayloadSender;
import dev.sshear.way2homes.payloads.HomePayloadSender;
import dev.sshear.way2homes.*;
import net.essentialsx.api.v2.events.HomeModifyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EssentialsListener implements Listener {

    @EventHandler
    public void setHomeEvent(HomeModifyEvent event) {
        var owner = event.getHomeOwner();
        var newLoc = event.getNewLocation();
        var oldLoc = event.getOldLocation();
        var newName = event.getNewName();
        var oldName = event.getOldName();

        // If the location has changed, send delete for old location first
        if (oldLoc != null && newLoc != null &&
                (oldLoc.getBlockX() != newLoc.getBlockX() ||
                        oldLoc.getBlockY() != newLoc.getBlockY() ||
                        oldLoc.getBlockZ() != newLoc.getBlockZ())) {
            var oldHome = new Home(oldLoc.getBlockX(), oldLoc.getBlockY(), oldLoc.getBlockZ(), oldName);
            try {
                var oldHomeData = new HomeData(oldHome.getName(), oldHome.getX(), oldHome.getY(), oldHome.getZ());
                DelHomePayloadSender.sendHomeData(owner.getBase(), oldHomeData);
            } catch (Exception e) {
                Constants.LOG.error("Failed to send delete data for old home:  {}", oldHome.getName(), e);
            }
        }

        // Send the new/updated home data
        if (newLoc != null) {
            var home = new Home(newLoc.getBlockX(), newLoc.getBlockY(), newLoc.getBlockZ(), newName);
            try {
                var homeData = new HomeData(home.getName(), home.getX(), home.getY(), home.getZ());
                HomePayloadSender.sendHomeData(owner.getBase(), homeData);
            } catch (Exception e) {
                Constants.LOG.error("Failed to send home data for home: {}", home.getName(), e);
            }
        } else if (oldLoc != null) {
            // This is a pure delete (newLoc is null)
            var home = new Home(oldLoc.getBlockX(), oldLoc.getBlockY(), oldLoc.getBlockZ(), oldName);
            try {
                var homeData = new HomeData(home.getName(), home.getX(), home.getY(), home.getZ());
                DelHomePayloadSender.sendHomeData(owner.getBase(), homeData);
            } catch (Exception e) {
                Constants.LOG.error("Failed to send delete data for home: {}", home.getName(), e);
            }
        }
    }
}
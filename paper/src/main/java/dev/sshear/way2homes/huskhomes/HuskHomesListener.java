package dev.sshear.way2homes.huskhomes;

import dev.sshear.way2homes.payloads.DelHomePayloadSender;
import dev.sshear.way2homes.payloads.HomePayloadSender;
import dev.sshear.way2homes.Constants;
import dev.sshear.way2homes.HomeData;
import net.william278.huskhomes.event.HomeCreateEvent;
import net.william278.huskhomes.event.HomeDeleteEvent;
import net.william278.huskhomes.event.HomeEditEvent;
import net.william278.huskhomes.position.Home;
import net.william278.huskhomes.position.Position;
import net.william278.huskhomes.user.User;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HuskHomesListener implements Listener {

    @EventHandler
    public void onModifyHome(HomeEditEvent event) {
        var home = event.getHome();
        var owner = home.getOwner();
        var oldPos = event.getOriginalHome();
        deleteHomeData(home, owner, oldPos);
        newHomeData(owner, home, home.getName());
    }

    private void deleteHomeData(Home home, User owner, Home oldPos) {
        try {
            var homeData = new HomeData(oldPos.getName(), (int) oldPos.getX(), (int) oldPos.getY(), (int) oldPos.getZ());
            DelHomePayloadSender.sendHomeData(Bukkit.getPlayer(owner.getUuid()), homeData);
        } catch (Exception e) {
            Constants.LOG.error("Failed to send home data for home: {}", home.getName(), e);
        }
    }

    @EventHandler
    public void onCreateHome(HomeCreateEvent event) {
        var owner = event.getOwner();
        var loc = event.getPosition();
        newHomeData(owner, loc, event.getName());
    }

    private void newHomeData(User owner, Position loc, String name) {
        try {
            var homeData = new HomeData(name, (int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
            HomePayloadSender.sendHomeData(Bukkit.getPlayer(owner.getUuid()), homeData);
        } catch (Exception e) {
            Constants.LOG.error("Failed to send home data for home: {}", name, e);
        }
    }

    @EventHandler
    public void onDeleteHome(HomeDeleteEvent event) {
        var home = event.getHome();
        var owner = home.getOwner();
        deleteHomeData(home, owner, home);
    }
}
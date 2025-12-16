package dev.sshear.way2homes.huskhomes;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.sshear.way2homes.Constants;
import dev.sshear.way2homes.Way2Homes;
import net.william278.huskhomes.event.HomeCreateEvent;
import net.william278.huskhomes.event.HomeDeleteEvent;
import net.william278.huskhomes.event.HomeEditEvent;
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
        newHomeData(owner, home, home.getName());
    }

    @EventHandler
    public void onCreateHome(HomeCreateEvent event) {
        var owner = event.getOwner();
        var loc = event.getPosition();
        newHomeData(owner, loc, event.getName());
    }

    private void newHomeData(User owner, Position loc, String name) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(name);
        out.writeInt(((int) loc.getX()));
        out.writeInt((int) loc.getY());
        out.writeInt((int) loc.getZ());
        Bukkit.getPlayer(owner.getUuid()).sendPluginMessage(Way2Homes.getPlugin(Way2Homes.class),
                "way2homes:home_data", out.toByteArray());
    }

    @EventHandler
    public void onDeleteHome(HomeDeleteEvent event) {
        var home = event.getHome();
        var owner = home.getOwner();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(home.getName());
        out.writeInt(((int) home.getX()));
        out.writeInt((int) home.getY());
        out.writeInt((int) home.getZ());
        Bukkit.getPlayer(owner.getUuid()).sendPluginMessage(Way2Homes.getPlugin(Way2Homes.class),
                "way2homes:del_home_data", out.toByteArray());
    }
}
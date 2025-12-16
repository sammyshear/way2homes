package dev.sshear.way2homes.jmap;

import dev.sshear.way2homes.Constants;
import dev.sshear.way2homes.Home;
import dev.sshear.way2homes.payloads.DelHomeDataPayload;
import dev.sshear.way2homes.payloads.HomeDataPayload;
import journeymap.api.v2.client.IClientAPI;
import journeymap.api.v2.common.event.ClientEventRegistry;
import journeymap.api.v2.common.waypoint.Waypoint;
import journeymap.api.v2.common.waypoint.WaypointFactory;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class JmapEventListener {

    private IClientAPI jmApi;

    Map<String, Home> homes = new HashMap<>();
    Map<String, Home> delHomes = new HashMap<>();

    Map<String, Waypoint> waypoints = new HashMap<>();

    public JmapEventListener(IClientAPI jmApi) {
        this.jmApi = jmApi;
        ClientEventRegistry.MAPPING_EVENT.subscribe(Constants.MOD_ID, event -> {
            ClientPlayNetworking.registerGlobalReceiver(HomeDataPayload.TYPE, (payload, context) -> {
                var data = payload.data();
                Constants.LOG.debug("{}", data);
                Home home = new Home(data.x(), data.y(), data.z(), data.name());
                homes.put(data.name(), home);
                syncWaypoints();
            });
            ClientPlayNetworking.registerGlobalReceiver(DelHomeDataPayload.TYPE, (payload, context) -> {
                var data = payload.data();
                Constants.LOG.debug("{}", data);
                Home home = new Home(data.x(), data.y(), data.z(), data.name());
                homes.remove(home.getName());
                delHomes.put(data.name(), home);
                syncDelWaypoints();
            });
        });
    }

    private void syncDelWaypoints() {
        for (Home home : delHomes.values()) {
            Waypoint waypoint = waypoints.get(home.getName());
            homes.remove(home.getName());
            waypoints.remove(home.getName());
            jmApi.removeWaypoint(Constants.MOD_ID, waypoint);
        }

        delHomes.clear();
    }

    private void syncWaypoints() {
        for (Home home : homes.values()) {
            var waypoint = WaypointFactory.createClientWaypoint(Constants.MOD_ID, new BlockPos(home.getX(), home.getY(), home.getZ()),
                    home.getName(), Minecraft.getInstance().level.dimension(), true);
            jmApi.addWaypoint(Constants.MOD_ID, waypoint);
            waypoints.put(home.getName(), waypoint);
        }
    }
}

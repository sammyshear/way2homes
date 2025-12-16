package dev.sshear.way2homes;

import dev.sshear.way2homes.payloads.DelHomeDataPayload;
import dev.sshear.way2homes.payloads.HomeDataPayload;
import dev.sshear.way2homes.platform.Services;
import dev.sshear.way2homes.platform.services.IPlatformHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.waypoint.WaypointColor;

import java.util.HashMap;
import java.util.Map;

public class Way2Homes implements ClientModInitializer {

    Map<String, Home> homes = new HashMap<>();
    Map<String, Home> delHomes = new HashMap<>();

    @Override
    public void onInitializeClient() {
        Constants.LOG.info("Hello from Way2Homes.");
        IPlatformHelper platformHelper = Services.PLATFORM;
        PayloadTypeRegistry.playS2C().register(HomeDataPayload.TYPE, HomeDataPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(DelHomeDataPayload.TYPE, DelHomeDataPayload.STREAM_CODEC);
        if (platformHelper.isModLoaded("xaerominimap")) {
            Constants.LOG.info("Xaero's Minimap detected");
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
        }
    }

    private void syncWaypoints() {
        for (Home home : homes.values()) {
            var mgr = BuiltInHudModules.MINIMAP.getCurrentSession().getWorldManager();

            var waypointSet = mgr.getCurrentWorld().getCurrentWaypointSet();
            var waypoint = new Waypoint(home.getX(), home.getY(), home.getZ(), home.getName(), home.getName().substring(0, 1),
                    WaypointColor.GRAY);
            waypointSet.add(waypoint);
        }
    }

    private void syncDelWaypoints() {
        for (Home home : delHomes.values()) {
            var mgr = BuiltInHudModules.MINIMAP.getCurrentSession().getWorldManager();

            var waypointSet = mgr.getCurrentWorld().getCurrentWaypointSet();
            for (Waypoint waypoint : waypointSet.getWaypoints()) {
                if (waypoint.getName().equals(home.getName()) && waypoint.getX() == home.getX()
                        && waypoint.getY() == home.getY() && waypoint.getZ() == home.getZ()) {
                    waypointSet.remove(waypoint);
                    break;
                }
            }
        }
        delHomes.clear();
    }
}
